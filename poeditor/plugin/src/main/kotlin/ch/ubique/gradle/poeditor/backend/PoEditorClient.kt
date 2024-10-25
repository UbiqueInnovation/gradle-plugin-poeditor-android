package ch.ubique.gradle.poeditor.backend

import ch.ubique.gradle.poeditor.backend.model.ApiResponse
import ch.ubique.gradle.poeditor.backend.model.Language
import ch.ubique.gradle.poeditor.backend.model.Project
import okhttp3.OkHttpClient
import okhttp3.Request
import org.gradle.api.GradleException
import retrofit2.Call
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

	fun getProject(projectId: Int): Project {
		return service.projectsView(apiKey, projectId).requireResult().project
	}

	fun getLanguages(projectId: Int): List<Language> {
		return service.languagesList(apiKey, projectId).requireResult().languages
	}

	fun export(projectId: Int, code: String, fileType: String, fallbackLanguage: String?, options: String?, file: File) {
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
		val body = execute().body() ?: throw GradleException("PoEditor API request failed for ${T::class.java.simpleName} with no response body")
		val response = body.response
		if (response.code != "200") {
			throw GradleException("PoEditor API request failed for ${T::class.java.simpleName} with response: ${response.status} ${response.code} ${response.message}")
		}
		return body.result ?: throw GradleException("PoEditor API request failed for ${T::class.java.simpleName} with no result object")
	}

}
