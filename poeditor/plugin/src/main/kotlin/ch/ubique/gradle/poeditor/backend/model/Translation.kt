package ch.ubique.gradle.poeditor.backend.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class Translation(
	val term: String,
	val context: String,
	val translation: TranslationContent,
)

@JsonClass(generateAdapter = true)
internal data class TranslationContent(
	val content: String,
)

@JsonClass(generateAdapter = true)
internal data class TranslationsChangedResult(
	val translations: TranslationsMutation,
)

@JsonClass(generateAdapter = true)
internal data class TranslationsMutation(
	val parsed: Int = 0,
	val added: Int = 0,
	val updated: Int = 0,
	val deleted: Int = 0,
)
