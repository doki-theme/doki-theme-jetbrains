import io.unthrottled.doki.build.plugin.ThemeVariant
import org.jetbrains.changelog.date
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  id("io.unthrottled.doki.build.plugin.dokibuildplugin")
  alias(libs.plugins.kotlin)
  id("java") // Java support
  alias(libs.plugins.intellij.platform)
  alias(libs.plugins.changelog)
}

group = providers.gradleProperty("pluginGroup").get()
version = providers.gradleProperty("pluginVersion").get()

// Set the JVM language level used to build the project.
kotlin {
  jvmToolchain(25)
  compilerOptions {
    jvmTarget.set(JvmTarget.JVM_25)
  }
}

// Configure project's dependencies
repositories {
  mavenCentral()

  // IntelliJ Platform Gradle Plugin Repositories Extension - read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-repositories-extension.html
  intellijPlatform {
    defaultRepositories()
  }
}

// Dependencies are managed with Gradle version catalog - read more: https://docs.gradle.org/current/userguide/platforms.html#sub:version-catalog
dependencies {
  implementation(libs.commons.io)
  implementation(libs.javassist)

  // IntelliJ Platform Gradle Plugin Dependencies Extension - read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-dependencies-extension.html
  intellijPlatform {
    create(providers.gradleProperty("platformType"), providers.gradleProperty("platformVersion"))

    // Plugin Dependencies. Uses `platformBundledPlugins` property from the gradle.properties file for bundled IntelliJ Platform plugins.
    bundledPlugins(providers.gradleProperty("platformBundledPlugins").map { it.split(',') })

    // Plugin Dependencies. Uses `platformPlugins` property from the gradle.properties file for plugin from JetBrains Marketplace.
    plugins(providers.gradleProperty("platformPlugins").map { it.split(',') })
  }
}

// Configure IntelliJ Platform Gradle Plugin - read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-extension.html
intellijPlatform {
  buildSearchableOptions.set(false)
  pluginConfiguration {
    name = providers.gradleProperty("pluginName")
    version = providers.gradleProperty("pluginVersion")
    description = provider {
      """
    <p>A large collection of cute anime character themes.</p>
    <p>Built with love and care! 💖</p>
    <br/>
    <p>
    See the <a href="https://github.com/ZimCodes/doki-theme-jetbrains#documentation">documentation</a> for more details.
    </p>
    <br/>
    <p><img alt="Doki Themes" src="https://raw.githubusercontent.com/ZimCodes/doki-theme-jetbrains/refs/heads/main/assets/screenshots/fate/gray_dark_code.png" width="400"/></p>
    <br/>
    """.trimIndent()
    }
    ideaVersion {
      sinceBuild = providers.gradleProperty("pluginSinceBuild")
      untilBuild = providers.gradleProperty("pluginUntilBuild")
    }
  }

  signing {
    certificateChain = providers.environmentVariable("CERTIFICATE_CHAIN")
    privateKey = providers.environmentVariable("PRIVATE_KEY")
    password = providers.environmentVariable("PRIVATE_KEY_PASSWORD")
  }

  publishing {
    token = providers.environmentVariable("PUBLISH_TOKEN")
    // The pluginVersion is based on the SemVer (https://semver.org) and supports pre-release labels, like 2.1.7-alpha.3
    // Specify pre-release label to publish the plugin in a custom Release Channel automatically. Read more:
    // https://plugins.jetbrains.com/docs/intellij/deployment.html#specifying-a-release-channel
    channels = providers.gradleProperty("pluginVersion")
      .map { listOf(it.substringAfter('-', "").substringBefore('.').ifEmpty { "default" }) }
  }

  pluginVerification {
    ides {
      recommended()
    }
  }
}

changelog {
  path = file("changelog/CHANGELOG.md").canonicalPath
  introduction = provider {
    """
    All notable changes to this project will be documented in this file.

    The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
    and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).
  """.trimIndent()
  }
  repositoryUrl = "https://github.com/ZimCodes/doki-theme-jetbrains"
  header = "${version.get()} - ${date()}"
}

tasks {
  wrapper {
    gradleVersion = providers.gradleProperty("gradleVersion").get()
  }

  patchPluginXml {
    dependsOn("buildThemes")
  }

  // NOTE: To generate a variant: gradlew genTemplates -Pvariant=islands
  buildPlugin {
    val variantName = findProperty("variant") as? String ?: ThemeVariant.DARCULA.lowercase
    archiveBaseName.set("doki-theme-$variantName")
  }
}
