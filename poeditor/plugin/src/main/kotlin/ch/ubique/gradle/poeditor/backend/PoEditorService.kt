package ch.ubique.gradle.poeditor.backend

import ch.ubique.gradle.poeditor.backend.model.ApiResponse
import ch.ubique.gradle.poeditor.backend.model.ExportResult
import ch.ubique.gradle.poeditor.backend.model.LanguagesResult
import ch.ubique.gradle.poeditor.backend.model.ProjectResult
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * PoEditor API service
 *
 * Documentation: [https://poeditor.com/docs/api](https://poeditor.com/docs/api)
 */
internal interface PoEditorService {

	@FormUrlEncoded
	@POST("projects/view")
	fun projectsView(
		@Field("api_token") token: String,
		@Field("id") projectId: String,
	): Call<ApiResponse<ProjectResult>>

	@FormUrlEncoded
	@POST("languages/list")
	fun languagesList(
		@Field("api_token") token: String,
		@Field("id") projectId: String,
	): Call<ApiResponse<LanguagesResult>>

	@FormUrlEncoded
	@POST("projects/export")
	fun export(
		@Field("api_token") token: String,
		@Field("id") projectId: String,
		@Field("language") language: String,
		@Field("type") fileType: String,
		@Field("fallback_language") fallbackLanguage: String? = null,
		@Field("options") options: String? = null,
	): Call<ApiResponse<ExportResult>>

}
