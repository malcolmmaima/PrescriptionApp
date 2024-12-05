plugins {
    id("prescription.library")
    id("prescription.library.compose")
    id("prescription.testing")
}

android {
    namespace = "com.prescription.utils"
}

dependencies {
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
