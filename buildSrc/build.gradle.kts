plugins {
  kotlin("jvm") version "1.8.10"
  `kotlin-dsl`
}

repositories {
  mavenLocal()
  mavenCentral()
  gradlePluginPortal()
  maven {
    name = "GitHubPackages"
    url = uri("https://maven.pkg.github.com/doki-theme/doki-build-source-jvm")
    credentials {
      username = System.getenv("GITHUB_ACTOR") ?: ""
      password = System.getenv("GITHUB_TOKEN") ?: ""
    }
  }
}

dependencies {
  implementation("org.jsoup:jsoup:1.17.2")
  implementation("com.google.code.gson:gson:2.10.1")
  implementation("io.unthrottled.doki.build.jvm:doki-build-source-jvm:88.0.6")
}
