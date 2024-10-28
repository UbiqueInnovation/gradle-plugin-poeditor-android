package ch.ubique.gradle.poeditor.config

import ch.ubique.gradle.poeditor.api.StringsFileType
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import javax.inject.Inject

abstract class PoEditorPluginConfig
@Inject
constructor(project: Project) {
	private val objects = project.objects

	/**
	 * The API key for the PoEditor API.
	 */
	val apiKey: Property<String?> = objects.property(String::class.java)

	/**
	 * The ID of the PoEditor project to pull translations from.
	 */
	val projectId: Property<String> = objects.property(String::class.java)

	/**
	 * The type/format of the strings file to export, defaults to ANDROID_STRINGS.
	 */
	val fileType: Property<StringsFileType> = objects.property(StringsFileType::class.java).apply { set(StringsFileType.ANDROID_STRINGS) }

	/**
	 * The language code to use as the default language, defaults to "en".
	 */
	val defaultLanguage: Property<String> = objects.property(String::class.java).apply { set("en") }

	/**
	 * The language code to use as the fallback language, i.e. when translations are missing, defaults to the project's fallback language.
	 */
	val fallbackLanguage: Property<String?> = objects.property(String::class.java)

	/**
	 * The resource base directory to export the translations to, defaults to "src/main/res".
	 */
	val resourceDir: RegularFileProperty = objects.fileProperty().apply { set(project.file("src/main/res")) }

	/**
	 * The resource type to export the translations to, defaults to "values".
	 */
	val resourceType: Property<String> = objects.property(String::class.java).apply { set("values") }

	/**
	 * The filename of the strings file to export, defaults to "strings.xml".
	 */
	val fileName: Property<String> = objects.property(String::class.java).apply { set("strings.xml") }

	/**
	 * Additional PoEditor export options as a JSON string, defaults to "[{\"unquoted\":1}]".
	 */
	val exportOptions: Property<String?> = objects.property(String::class.java).apply { set("[{\"unquoted\":1}]") }

}
