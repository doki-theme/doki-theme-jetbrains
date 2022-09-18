plugins {
  `kotlin-dsl`
}

repositories {
  mavenLocal()
  mavenCentral()
  maven {
    name = "GitHubPackages"
    url = uri("https://maven.pkg.github.com/doki-theme/doki-build-source-jvm")
  }
}

dependencies {
  implementation("org.jsoup:jsoup:1.13.1")
  implementation("io.unthrottled.doki.build.jvm:doki-build-source-jvm:88.0.4")
}
