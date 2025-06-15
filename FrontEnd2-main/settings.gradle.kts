pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        id("com.android.application") version "8.10.0" apply false
        id("org.jetbrains.kotlin.android") version "2.0.21" apply false
        id("org.jetbrains.kotlin.kapt") version "2.0.21" apply false
        id("com.google.dagger.hilt.android") version "2.48" apply false
        id("org.jetbrains.kotlin.plugin.compose") version "2.0.21" apply false
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    /*
    versionCatalogs {
        create("libs") {
            from(files("./gradle/libs.versions.toml"))
        }
    }*/
}
rootProject.name = "test_2025_05_19_k"
include(":app")
