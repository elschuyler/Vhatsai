pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // For Android-FilePicker
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "Vhatsai"
include(":app")
