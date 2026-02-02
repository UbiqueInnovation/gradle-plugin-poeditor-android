package ch.ubique.gradle.poeditor.backend.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class ProjectResult(
	val project: Project,
)

@JsonClass(generateAdapter = true)
internal data class Project(
	val id: String,
	val name: String,
	val description: String? = null,
	@param:Json(name = "public") val isPublic: Int,
	val open: Int,
	@param:Json(name = "reference_language") val referenceLanguage: String,
	@param:Json(name = "fallback_language") val fallbackLanguage: String,
	val created: String,
)
