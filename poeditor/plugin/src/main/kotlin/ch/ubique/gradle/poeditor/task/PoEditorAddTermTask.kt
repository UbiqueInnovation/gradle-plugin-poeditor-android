package ch.ubique.gradle.poeditor.task

import ch.ubique.gradle.poeditor.backend.PoEditorClient
import ch.ubique.gradle.poeditor.backend.model.Term
import ch.ubique.gradle.poeditor.backend.model.Translation
import ch.ubique.gradle.poeditor.backend.model.TranslationContent
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault

@DisableCachingByDefault
abstract class PoEditorAddTermTask : DefaultTask() {

	@get:Input
	@get:Optional
	abstract var apiKey: String?

	@get:Input
	abstract var projectId: String

	@get:Input
	abstract var defaultLanguage: String

	@get:Input
	@get:Optional
	abstract var fallbackLanguage: String?

	init {
		group = "poeditor"
		description = "Add term with translations to PoEditor"
	}

	@TaskAction
	fun poeditorAddTerm() {
		val apiKey = apiKey ?: throw GradleException("PoEditor API key not set.\npoeditor {\n  apiKey = \"***\"\n}")
		val client = PoEditorClient(apiKey)

		println("Loading project details...")
		val projectDetails = client.getProject(projectId)

		val importantLanguages = listOfNotNull(projectDetails.fallbackLanguage, fallbackLanguage, defaultLanguage)
		val languages = client.getLanguages(projectId)
		val languagesSorted = languages.sortedBy { importantLanguages.indexOf(it.code).takeIf { it >= 0 } ?: Int.MAX_VALUE }

		val existingTerms = client.getTerms(projectId).map { it.term }.toMutableSet()

		println("Please provide term details.")

		val termPattern = Regex("[a-zA-Z][a-zA-Z0-9_]*")

		do {
			val termId = prompt("Term identifier: ").trim()
			if (!termId.matches(termPattern)) {
				throw GradleException("Invalid term identifier '$termId'. Must match ${termPattern.pattern}")
			}

			if (termId in existingTerms) {
				throw GradleException("Term '$termId' already exists")
			}
			existingTerms += termId

			val context = prompt("Term context (description): ").trim()

			val termData = Term(term = termId, context = context)
			val termsChanged = client.addTerm(projectId, termData)
			if (termsChanged.added != 1) {
				throw GradleException("Failed to register term '$termId': $termsChanged")
			}

			for (language in languagesSorted) {
				val translation = prompt("Translation for ${language.name} (${language.code}): ").trim()
				if (translation.isNotBlank()) {
					val translationData =
						Translation(term = termId, context = context, translation = TranslationContent(translation))
					val translationChanged = client.addTranslations(projectId, language.code, translationData)
					if (translationChanged.added != 1) {
						throw GradleException("Failed to add translation for term '$termId' in language '${language.code}': $translationChanged")
					}
				}
			}

			println("Term '$termId' added successfully!")

			val another = prompt("Add another term? (y): ").trim().lowercase()
		} while (another == "y")
	}

	private fun prompt(message: String): String {
		print(message)
		System.out.flush()
		return readln()
	}

}
