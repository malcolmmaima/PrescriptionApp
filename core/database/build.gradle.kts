plugins {
    id("prescription.library")
    id("prescription.room")
    id("prescription.hilt")
}

android {
    namespace = "com.prescription.database"
}

dependencies {
    implementation(project(":core:networking"))
}
