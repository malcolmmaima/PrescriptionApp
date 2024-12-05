plugins {
    id("prescription.library")
    id("prescription.hilt")
    id("prescription.library.network")
}

android {
    namespace = "com.prescription.core.features.networking"
}

dependencies {
    implementation(project(":core:utils"))
}
