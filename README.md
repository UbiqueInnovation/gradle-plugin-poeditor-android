# Ubique PoEditor Gradle Plugin

[![Build](https://github.com/UbiqueInnovation/gradle-plugin-poeditor-android/actions/workflows/build.yml/badge.svg)](https://github.com/UbiqueInnovation/gradle-plugin-poeditor-android/actions/workflows/build.yml)
[![Release](https://github.com/UbiqueInnovation/gradle-plugin-poeditor-android/actions/workflows/publish.yml/badge.svg)](https://github.com/UbiqueInnovation/gradle-plugin-poeditor-android/actions/workflows/publish.yml)
[![Maven Central](https://img.shields.io/maven-central/v/ch.ubique.gradle/poeditor.svg?label=Maven%20Central)](https://search.maven.org/artifact/ch.ubique.gradle/poeditor)

Gradle Plugin to sync translations with PoEditor.

## Usage

```kotlin
plugins {
    id("ch.ubique.gradle.poeditor") version "1.0.0"
}
```

### Configuration

Gradle configuration for the PoEditor plugin in your module's `build.gradle`:

```kotlin
poeditor {
    // your API key for the PoEditor API, required
    apiKey = "***"
    
    // ID of the PoEditor project to pull translations from, required
    projectId = "123456"
    
    // type/format of the strings file to export, optional, defaults to ANDROID_STRINGS
    fileType = StringsFileType.ANDROID_STRINGS
    
    // language code to use as the default language, optional, defaults to "en"
    defaultLanguage = "en"
    
    // language code to use as the fallback language, i.e. when translations are missing, optional, defaults to the project's fallback language
    fallbackLanguage = "de"
    
    // resource base directory to export the translations to, optional, defaults to "src/main/res"
    resourceDir = "src/main/res"
    
    // resource type to export the translations to, optional, defaults to "values"
    resourceType = "values"
    
    // filename of the strings file to export, optional, defaults to "strings.xml"
    fileName = "strings.xml"
    
    // additional PoEditor export options as a JSON string, optional, defaults to "[{\"unquoted\":1}]"
    exportOptions = "[{\"unquoted\":1}]"
}
```

### Pull translations

To export the strings files from PoEditor, run the following Gradle task:

```shell
./gradlew poeditorPull
```

### Add term

To add a new term with translations to PoEditor, run the following Gradle task:

```shell
./gradlew poeditorAddTerm
```

Then enter the term and the translation as prompted, leave empty to skip.

```text
Term identifier:
Term context (description):
Translation for English (en):
Translation for German (de):
```

Type `y` to add another term or anything else to finish.

---

## Deployment

Create a [Release](https://github.com/UbiqueInnovation/gradle-plugin-poeditor-android/releases), 
setting the Tag to the desired version prefixed with a `v`.

Each release on Github will be deployed to Maven Central.
