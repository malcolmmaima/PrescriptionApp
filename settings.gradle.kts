pluginManagement {
    includeBuild("build-logic")
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
    }
}

rootProject.name = "Prescription"
include(":app")
include(":features")
include(":features:prescription")
include(":core")
include(":core:networking")
include(":core:design")
include(":core:utils")
include(":core:database")
