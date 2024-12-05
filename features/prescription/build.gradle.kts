plugins {
    id("prescription.library")
    id("prescription.hilt")
    id("prescription.library.network")
    id("prescription.library.compose")
    id("prescription.testing")
}

android {
    namespace = "com.prescription.features"
}

dependencies {
    implementation(project(":core:networking"))
    implementation(project(":core:design"))
    implementation(project(":core:utils"))
    implementation(project(":core:database"))
}
