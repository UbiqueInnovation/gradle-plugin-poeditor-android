package ch.ubique.gradle.poeditor.task

import ch.ubique.gradle.poeditor.api.StringsFileType
import ch.ubique.gradle.poeditor.backend.PoEditorClient
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.RegularFile
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Internal
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
	abstract var projectId: String

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

	@get:Internal
	abstract var flavorPrefixes: Map<String, RegularFile>

	@get:Input
	abstract var flavorPrefixSeparator: String

	private val projectDir = project.projectDir

	// Matches a single <string> element and captures its `name`. Assumes each element is on
	// its own line: multi-line elements (e.g. <plurals>, <string-array>) are not matched and
	// are therefore always kept verbatim in the base file, never filtered or moved to a flavor.
	private val stringNameRegex = Regex("""<string[^>]*name="([^"]+)"[^>]*>.*?</string>""")

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

		languages.forEach { language ->
			val localePostfix = if (language.code == defaultLanguage) "" else "-" + language.code.toAndroidLocale()
			val localeDir = "$resourceType$localePostfix"

			// Get all translations as xml
			val xmlContent = client.exportToXml(projectId, language.code, fileType.name.lowercase(), fallbackLang, exportOptions)

			// Filter out the prefixes for the base xml
			val baseXml = filterMapXmlByStringName(xmlContent, { name ->
				if (flavorPrefixes.keys.any { prefix -> name.startsWith(prefix + flavorPrefixSeparator) })
					null
				else name
			})
			val path = File(resourceDir, localeDir)
			path.mkdirs()
			val file = File(path, filename)
			file.writeText(baseXml)

			// Now write only the strings associated with the prefix
			flavorPrefixes.forEach { (prefix, dir) ->
				val xml = filterMapXmlByStringName(xmlContent, { name ->
					if (name.startsWith(prefix + flavorPrefixSeparator))
						name.removePrefix(prefix + flavorPrefixSeparator)
					else null
				})

				val path = File(dir.asFile, localeDir)
				path.mkdirs()
				val file = File(path, filename)
				file.writeText(xml)
				println("Exported ${language.name} ($prefix) to ${file.toRelativeString(projectDir)}")
			}
			println("Exported ${language.name} (base) to ${file.toRelativeString(projectDir)}")
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

	/**
	 * Filters and maps the XML lines using the callback. The callback receives the "name" value.
	 * If the callback returns null, the line is dropped.
	 * If it returns a String, the line is kept and the "name" attribute is replaced.
	 * Lines without a valid match are kept as a default.
	 *
	 * Note: this operates line-by-line and only recognizes single-line <string> elements.
	 * Multi-line elements such as <plurals> and <string-array> are always passed through
	 * unchanged (never filtered by prefix nor moved into a flavor output).
	 */
	private fun filterMapXmlByStringName(xml: String, callback: (String) -> String?): String =
		xml.lines()
			.mapNotNull { line ->
				// Find the regex match. If it doesn't exist, keep the line as is.
				val match = stringNameRegex.find(line) ?: return@mapNotNull line

				// Execute the callback. If it returns null, drop the line.
				val newName = callback(match.groupValues[1]) ?: return@mapNotNull null

				// Replace only the specific characters where the old name was found.
				val nameRange = match.groups[1]!!.range
				line.replaceRange(nameRange, newName)
			}
			.joinToString("\n")
}
