package ch.ubique.gradle.poeditor

import ch.ubique.gradle.poeditor.config.PoEditorPluginConfig
import ch.ubique.gradle.poeditor.task.PoEditorPullTask
import org.gradle.api.Plugin
import org.gradle.api.Project

abstract class PoEditorPlugin : Plugin<Project> {

	override fun apply(project: Project) {
		val extension = project.extensions.create("poeditor", PoEditorPluginConfig::class.java, project)

		project.tasks.register(
			"poeditorPull",
			PoEditorPullTask::class.java
		) { task ->
			task.apiKey = extension.apiKey.orNull
			task.projectId = extension.projectId.get()
			task.fileType = extension.fileType.get()
			task.defaultLanguage = extension.defaultLanguage.get()
			task.fallbackLanguage = extension.fallbackLanguage.orNull
			task.resourceDir = extension.resourceDir.get().also { it.mkdirs() }
			task.resourceType = extension.resourceType.get()
			task.filename = extension.fileName.get()
			task.exportOptions = extension.exportOptions.orNull
		}

	}

}
