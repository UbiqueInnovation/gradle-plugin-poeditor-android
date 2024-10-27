package ch.ubique.gradle.poeditor.backend.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class TermsResult(
	val terms: List<Term>,
)

@JsonClass(generateAdapter = true)
internal data class Term(
	val term: String,
	val context: String,
	val reference: String = "",
	val plural: String = "",
	val comment: String = "",
	val tags: List<String> = emptyList(),
)

@JsonClass(generateAdapter = true)
internal data class TermsChangedResult(
	val terms: TermsMutation,
)

@JsonClass(generateAdapter = true)
internal data class TermsMutation(
	val parsed: Int = 0,
	val added: Int = 0,
	val updated: Int = 0,
	val deleted: Int = 0,
)
