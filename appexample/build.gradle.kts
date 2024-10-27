import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	alias(libs.plugins.androidApplication)
	alias(libs.plugins.kotlinAndroid)
	id("ch.ubique.gradle.poeditor")
}

android {
	namespace = "ch.ubique.poeditor.example"
	compileSdk = 34

	defaultConfig {
		applicationId = "ch.ubique.poeditor.example"
		minSdk = 26
		targetSdk = 34
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
	kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
}

dependencies {
	implementation(libs.androidx.appcompat)
	implementation(libs.androidx.lifecycle.viewmodelKtx)
}

poeditor {
	apiKey = System.getenv("UBIQUE_POEDITOR_API_KEY") ?: extra["ubiquePoEditorAPIKey"] as? String
	projectId = "505095"
	defaultLanguage = "en"
	fallbackLanguage = "de"
	resourceDir = layout.buildDirectory.file("poeditor-output")
	fileName = "strings.xml"
}
