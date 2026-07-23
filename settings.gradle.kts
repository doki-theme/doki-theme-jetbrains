rootProject.name = "doki-theme-jetbrains"

plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

includeBuild("doki-build-plugin")
includeBuild("doki-build-source-jvm")