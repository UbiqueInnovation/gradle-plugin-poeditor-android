package ch.ubique.gradle.poeditor.backend.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class ExportResult(
	val url: String,
)
