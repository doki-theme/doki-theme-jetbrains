import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.kordamp.gradle.plugin.markdown.tasks.MarkdownToHtmlTask

fun properties(key: String) = project.findProperty(key).toString()

plugins {
  // Custom plugin for building all the themes
  id("doki-theme-plugin")
  // Kotlin support
  kotlin("jvm") version "1.8.22"
  // gradle-intellij-plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
  id("org.jetbrains.intellij") version "1.13.3"
  // detekt linter - read more: https://detekt.github.io/detekt/gradle.html
  id("io.gitlab.arturbosch.detekt") version "1.22.0"
  // ktlint linter - read more: https://github.com/JLLeitschuh/ktlint-gradle
  id("org.jlleitschuh.gradle.ktlint") version "11.3.2"
  id("org.kordamp.gradle.markdown") version "2.2.0"
  id("org.jetbrains.qodana") version "0.1.13"
}

group = properties("pluginGroup")
version = properties("pluginVersion")

// Configure project's dependencies
repositories {
  mavenCentral()
  mavenLocal()
}

dependencies {
  detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.22.0")
  implementation("commons-io:commons-io:2.11.0")
  implementation("org.javassist:javassist:3.29.2-GA")
  implementation("io.sentry:sentry:6.18.1")
  testImplementation("org.assertj:assertj-core:3.24.2")
  testImplementation("io.mockk:mockk:1.13.5")
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
  version.set(properties("platformVersion"))
  type.set(properties("platformType"))
  downloadSources.set(properties("platformDownloadSources").toBoolean())
  updateSinceUntilBuild.set(true)

  // Plugin Dependencies. Uses `platformPlugins` property from the gradle.properties file.
  plugins.set(
    properties("platformPlugins").split(',')
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

qodana {
  cachePath.set(projectDir.resolve(".qodana").canonicalPath)
  reportPath.set(projectDir.resolve("build/reports/inspections").canonicalPath)
  saveReport.set(true)
  showReport.set(System.getenv("QODANA_SHOW_REPORT").toBoolean())
}

tasks {
  // Set the JVM compatibility versions
  properties("javaVersion").let {
    withType<JavaCompile> {
      sourceCompatibility = it
      targetCompatibility = it
    }
    withType<KotlinCompile> {
      kotlinOptions.jvmTarget = it
    }
  }

  withType<Detekt> {
    jvmTarget = "17"
  }

  withType<MarkdownToHtmlTask> {
    sourceDir = file("$projectDir/changelog")
    outputDir = file("$projectDir/build/html")
  }

  runIde {
    maxHeapSize = "2g"
    enabled = environment.getOrDefault("SHOULD_DOKI_THEME_RUN", "true") == "true"
    val idePath = properties("idePath")
    if (idePath.isNotEmpty()) {
      ideDir.set(file(idePath))
      systemProperty("idea.platform.prefix", properties("idePrefix"))
    }
  }

  runPluginVerifier {
    ideVersions.set(properties("pluginVerifierIdeVersions").split(',').map(String::trim).filter(String::isNotEmpty))
  }

  patchPluginXml {
    version.set(properties("pluginVersion"))
    sinceBuild.set(properties("pluginSinceBuild"))
    untilBuild.set(properties("pluginUntilBuild"))

    val releaseNotes = file("$projectDir/build/html/RELEASE-NOTES.html")
    if (releaseNotes.exists()) {
      changeNotes.set(releaseNotes.readText())
    }

    dependsOn("markdownToHtml", "buildThemes")
  }

  signPlugin {
    certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
    privateKey.set(System.getenv("PRIVATE_KEY"))
    password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
  }
}
