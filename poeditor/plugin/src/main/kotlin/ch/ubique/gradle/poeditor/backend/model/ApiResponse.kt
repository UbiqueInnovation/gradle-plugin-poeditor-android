package ch.ubique.gradle.poeditor.backend.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class ApiResponse<T>(
	val response: ResponseStatus,
	val result: T?,
)

@JsonClass(generateAdapter = true)
internal data class ResponseStatus(
	val status: String,
	val code: String,
	val message: String,
)
