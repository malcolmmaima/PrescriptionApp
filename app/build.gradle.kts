import java.io.ByteArrayOutputStream
import org.gradle.api.GradleException

plugins {
    id("prescription.app")
    id("prescription.hilt")
    id("prescription.app.compose")
    id("prescription.app.network")
}

android {
    namespace = "com.prescription.app"

    defaultConfig {
        applicationId = "com.prescription.app"

        val fallbackVersionCode = 1
        var versionCode: Int
        val bytes = ByteArrayOutputStream()
        try {
            project.exec {
                commandLine = "git rev-list HEAD --count".split(" ")
                standardOutput = bytes
            }
            versionCode = String(bytes.toByteArray()).trim().toInt() + 1
        } catch (e: Exception) {
            logger.warn("Failed to get Git commit count: ${e.message}. Using fallback version code $fallbackVersionCode.")
            versionCode = fallbackVersionCode
        }

        versionName = "0.0.$versionCode"

        vectorDrawables {
            useSupportLibrary = true
        }

        buildTypes {
            debug {
                versionNameSuffix = "-debug"
                applicationIdSuffix = ".debug"
            }
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":core:design"))
    implementation(project(":features:prescription"))
    implementation(project(":core:utils"))
    implementation(project(":core:database"))
}
