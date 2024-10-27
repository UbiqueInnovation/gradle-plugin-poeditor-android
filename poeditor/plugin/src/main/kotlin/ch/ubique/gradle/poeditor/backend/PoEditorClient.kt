package ch.ubique.gradle.poeditor.backend

import ch.ubique.gradle.poeditor.backend.model.ApiResponse
import ch.ubique.gradle.poeditor.backend.model.Language
import ch.ubique.gradle.poeditor.backend.model.Project
import okhttp3.OkHttpClient
import okhttp3.Request
import org.gradle.api.GradleException
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File

internal class PoEditorClient(
	private val apiKey: String,
	host: String = HOST,
) {

	companion object {
		const val HOST: String = "https://api.poeditor.com/v2/"
	}

	private val httpClient = OkHttpClient.Builder().build()
	private val service = Retrofit.Builder()
		.baseUrl(host)
		.client(httpClient)
		.addConverterFactory(MoshiConverterFactory.create())
		.build()
		.create(PoEditorService::class.java)

	fun getProject(projectId: String): Project {
		return service.projectsView(apiKey, projectId).requireResult().project
	}

	fun getLanguages(projectId: String): List<Language> {
		return service.languagesList(apiKey, projectId).requireResult().languages
	}

	fun export(projectId: String, code: String, fileType: String, fallbackLanguage: String?, options: String?, file: File) {
		val url = service.export(apiKey, projectId, code, fileType, fallbackLanguage, options).requireResult().url
		val request = Request.Builder().url(url).build()
		httpClient.newCall(request).execute().use { response ->
			if (!response.isSuccessful) {
				throw GradleException("Failed to download translations with response ${response.code} ${response.message}")
			}
			file.outputStream().use { output ->
				response.body!!.byteStream().use { input ->
					input.copyTo(output)
				}
			}
		}
	}

	private inline fun <reified T> Call<ApiResponse<T>>.requireResult(): T {
		val typeName = T::class.java.simpleName
		val response = runCatching { execute() }
			.mapCatching { if (it.isSuccessful) it else throw HttpException(it) }
			.getOrElse { throw GradleException("PoEditor API request failed for $typeName", it) }
		val body = response.body() ?: throw GradleException("PoEditor API request failed for $typeName with no response body")
		val apiResponse = body.response
		require(apiResponse.code == "200") { throw GradleException("PoEditor API request failed for $typeName " +
				"with API response: ${apiResponse.status} ${apiResponse.code} ${apiResponse.message}") }
		return body.result ?: throw GradleException("PoEditor API request failed for $typeName with no result object")
	}

}
