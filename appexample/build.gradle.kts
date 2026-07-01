import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	alias(libs.plugins.androidApplication)
	id("ch.ubique.gradle.poeditor")
}

android {
	namespace = "ch.ubique.poeditor.example"
	compileSdk = 36

	defaultConfig {
		applicationId = "ch.ubique.poeditor.example"
		minSdk = 26
		targetSdk = 36
		versionCode = 1
		versionName = project.version.toString()

		testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
	}

	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}
}

tasks.withType<KotlinCompile> {
	compilerOptions.jvmTarget = JvmTarget.JVM_17
}

dependencies {
	implementation(libs.androidx.appcompat)
}

poeditor {
	apiKey = System.getenv("UBIQUE_POEDITOR_API_KEY") ?: extra["ubiquePoEditorAPIKey"] as? String
	projectId = "234253"
	defaultLanguage = "en"
	fallbackLanguage = "de"
	resourceDir = layout.buildDirectory.file("poeditor-output")
	fileName = "strings.xml"
	exportOptions = null
	flavorPrefixes = mapOf(
		"onboarding" to layout.buildDirectory.file("poeditor-output-onboarding").get(),
		"gafor" to layout.buildDirectory.file("poeditor-output-gafor").get(),
	)
	flavorPrefixSeparator = "_"
}
