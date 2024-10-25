package ch.ubique.gradle.poeditor.task

import ch.ubique.gradle.poeditor.api.StringsFileType
import ch.ubique.gradle.poeditor.backend.PoEditorClient
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault
import java.io.File

@DisableCachingByDefault
abstract class PoEditorPullTask : DefaultTask() {

	@get:Input
	@get:Optional
	abstract var apiKey: String?
	@get:Input
	abstract var projectId: Int
	@get:Input
	abstract var defaultLanguage: String
	@get:Input
	@get:Optional
	abstract var fallbackLanguage: String?
	@get:Input
	abstract var fileType: StringsFileType
	@get:InputDirectory
	abstract var resourceDir: File
	@get:Input
	abstract var resourceType: String
	@get:Input
	abstract var filename: String
	@get:Input
	@get:Optional
	abstract var exportOptions: String?

	init {
		group = "poeditor"
		description = "Download translations from PoEditor"
	}

	@TaskAction
	fun poeditorPull() {
		val apiKey = apiKey ?: throw GradleException("PoEditor API key not set.\npoeditor {\n  apiKey = \"***\"\n}")
		val client = PoEditorClient(apiKey)

		val projectDetails = client.getProject(projectId)
		println("Downloading terms for project ${projectDetails.name} (ID ${projectDetails.id})")

		val fallbackLang = fallbackLanguage ?: projectDetails.fallbackLanguage

		val languages = client.getLanguages(projectId)

		languages.parallelStream().forEach { language ->
			val localePostfix = if (language.code == defaultLanguage) "" else "-" + language.code.toAndroidLocale()
			val path = File(resourceDir, "$resourceType$localePostfix")
			path.mkdirs()
			val file = File(path, filename)
			client.export(projectId, language.code, fileType.name.lowercase(), fallbackLang, exportOptions, file)
		}
	}

	private fun String.toAndroidLocale(): String {
		val parts = split('-')
		return if (parts.size <= 1) {
			lowercase()
		} else {
			parts[0].lowercase() + "-r" + parts[1].uppercase()
		}
	}
}
