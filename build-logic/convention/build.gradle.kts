plugins {
    `kotlin-dsl`
}
group = "com.prescription.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

    dependencies {
        compileOnly(libs.android.gradle)
        compileOnly(libs.kotlin.gradle)
        compileOnly(libs.ksp.gradlePlugin)
    }

}

gradlePlugin {
    plugins {
        register("androidApp"){
            id = "prescription.app"
            implementationClass = "AndroidApplicationConventionPlugin"
        }

        register("androidLibrary"){
            id = "prescription.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }

        register("androidHilt") {
            id = "prescription.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }

        register("androidApplicationCompose") {
            id = "prescription.app.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }

        register("androidApplicationNetwork") {
            id = "prescription.app.network"
            implementationClass = "AndroidApplicationNetworkConventionPlugin"
        }

        register("androidLibraryNetwork") {
            id = "prescription.library.network"
            implementationClass = "AndroidLibraryNetworkConventionPlugin"
        }

        register("androidLibraryCompose") {
            id = "prescription.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidTesting") {
            id = "prescription.testing"
            implementationClass = "TestingConventionPlugin"
        }
        register("androidRoom") {
            id = "prescription.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }

    }
}