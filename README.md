# Ubique PoEditor Gradle Plugin

[![Build](https://github.com/UbiqueInnovation/gradle-plugin-poeditor-android/actions/workflows/build.yml/badge.svg)](https://github.com/UbiqueInnovation/gradle-plugin-poeditor-android/actions/workflows/build.yml)
[![Release](https://github.com/UbiqueInnovation/gradle-plugin-poeditor-android/actions/workflows/publish.yml/badge.svg)](https://github.com/UbiqueInnovation/gradle-plugin-poeditor-android/actions/workflows/publish.yml)
[![Maven Central](https://img.shields.io/maven-central/v/ch.ubique.gradle/poeditor.svg?label=Maven%20Central)](https://search.maven.org/artifact/ch.ubique.gradle/poeditor)

Gradle Plugin to sync translations with PoEditor.

## PoEditor configuration

TODO

## Usage

```kotlin
plugins {
	id("ch.ubique.gradle.poeditor") version "1.0.0"
}
```

## Deployment

Create a [Release](https://github.com/UbiqueInnovation/gradle-plugin-poeditor-android/releases), 
setting the Tag to the desired version prefixed with a `v`.

Each release on Github will be deployed to Maven Central.
