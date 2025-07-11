import com.vanniktech.maven.publish.GradlePublishPlugin
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm")
	`java-gradle-plugin`
	alias(libs.plugins.pluginPublish)
	alias(libs.plugins.vanniktech)
	alias(libs.plugins.ksp)
}

dependencies {
	implementation(kotlin("stdlib"))
	implementation(gradleApi())
	implementation(libs.kotlin.gradle)
	implementation(libs.okhttp)
	implementation(libs.retrofit)
	implementation(libs.moshi)
	implementation(libs.moshi.kotlin)
	implementation(libs.retrofit.converter.moshi)
	implementation(libs.moshi.adapters)
	ksp(libs.moshi.kotlin.codegen)

	testImplementation(libs.junit)
}

java {
	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile> {
	compilerOptions.jvmTarget = JvmTarget.JVM_17
}

gradlePlugin {
	plugins {
		create(property("ID").toString()) {
			id = property("ID").toString()
			implementationClass = property("IMPLEMENTATION_CLASS").toString()
			version = project.version
		}
	}
}

mavenPublishing {
	configure(GradlePublishPlugin())
	coordinates(property("GROUP").toString(), property("ARTIFACT_ID").toString(), project.version.toString())
	publishToMavenCentral(true)
	signAllPublications()
}
