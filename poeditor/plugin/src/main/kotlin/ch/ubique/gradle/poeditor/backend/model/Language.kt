package ch.ubique.gradle.poeditor.backend.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class LanguagesResult(
	val languages: List<Language>,
)

@JsonClass(generateAdapter = true)
internal data class Language(
	val name: String,
	val code: String,
	val translations: Int,
	val percentage: Double,
	val updated: String,
)
