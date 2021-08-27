import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.kordamp.gradle.plugin.markdown.tasks.MarkdownToHtmlTask

fun properties(key: String) = project.findProperty(key).toString()

plugins {
  // Custom plugin for building all of the themes
  id("doki-theme-plugin")
  // Kotlin support
  id("org.jetbrains.kotlin.jvm") version "1.5.21"
  // gradle-intellij-plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
  id("org.jetbrains.intellij") version "1.1.4"
  // detekt linter - read more: https://detekt.github.io/detekt/gradle.html
  id("io.gitlab.arturbosch.detekt") version "1.18.0"
  // ktlint linter - read more: https://github.com/JLLeitschuh/ktlint-gradle
  id("org.jlleitschuh.gradle.ktlint") version "10.0.0"
  id("org.kordamp.gradle.markdown") version "2.2.0"
}

// Import variables from gradle.properties file
val pluginGroup: String by project
val pluginVersion: String by project
val pluginSinceBuild: String by project
val pluginUntilBuild: String by project

val platformType: String by project
val platformVersion: String by project
val platformPlugins: String by project
val platformDownloadSources: String by project

group = pluginGroup
version = pluginVersion

// Configure project's dependencies
repositories {
  mavenCentral()
  jcenter()
  mavenLocal()
}

dependencies {
  detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.18.0")
  implementation("commons-io:commons-io:2.11.0")
  implementation("org.javassist:javassist:3.28.0-GA")
  implementation("io.sentry:sentry:5.1.2")
  testImplementation("org.assertj:assertj-core:3.20.2")
  testImplementation("io.mockk:mockk:1.12.0")
}

configurations {
  implementation.configure {
    // sentry brings in a slf4j that breaks when
    // with the platform slf4j
    exclude("org.slf4j")
  }
}

// Configure gradle-intellij-plugin plugin.
// Read more: https://github.com/JetBrains/gradle-intellij-plugin
intellij {
  version.set(platformVersion)
  type.set(platformType)
  downloadSources.set(platformDownloadSources.toBoolean())
  updateSinceUntilBuild.set(true)

  // Plugin Dependencies. Uses `platformPlugins` property from the gradle.properties file.
  plugins.set(
    platformPlugins.split(',')
      .map(String::trim)
      .filter(String::isNotEmpty)
  )
}

// Configure detekt plugin.
// Read more: https://detekt.github.io/detekt/kotlindsl.html
detekt {
  config = files("./detekt-config.yml")
  buildUponDefaultConfig = true
  autoCorrect = true

  reports {
    html.enabled = false
    xml.enabled = false
    txt.enabled = false
  }
}

tasks {
  // Set the compatibility versions to 1.8
  withType<JavaCompile> {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
  }
  listOf("compileKotlin", "compileTestKotlin").forEach {
    getByName<KotlinCompile>(it) {
      kotlinOptions.jvmTarget = "1.8"
    }
  }

  withType<Detekt> {
    jvmTarget = "1.8"
  }

  withType<MarkdownToHtmlTask> {
    sourceDir = file("$projectDir/changelog")
    outputDir = file("$projectDir/build/html")
  }

  runIde {
    val idePath = properties("idePath")
    if (idePath.isNotEmpty()) {
      ideDir.set(file(idePath))
    }
  }

  runPluginVerifier {
    ideVersions.set(listOf("IC-2020.3.1", "IC-2021.1.3", "WS-2021.2"))
  }

  patchPluginXml {
    version.set(pluginVersion)
    sinceBuild.set(pluginSinceBuild)
    untilBuild.set(pluginUntilBuild)

    val releaseNotes = file("$projectDir/build/html/RELEASE-NOTES.html")
    if (releaseNotes.exists()) {
      changeNotes.set(releaseNotes.readText())
    }

    dependsOn("markdownToHtml", "buildThemes")
  }
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
  jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
  jvmTarget = "1.8"
}
