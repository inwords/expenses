import com.inwords.expenses.plugins.SharedKmmLibraryPlugin.Companion.applyKmmDefaults

plugins {
    id("shared-kmm-library-plugin")
}

android {
    namespace = "com.inwords.expenses.core.network"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

kotlin {
    applyKmmDefaults("shared-core-network")

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":shared:core:utils"))

                implementation(shared.ktor.client.content.negotiation)
                implementation(shared.ktor.client.logging)
                implementation(shared.ktor.serialization.kotlinx.json)
            }
        }
        androidMain {
            dependencies {
                implementation(project(":shared:core:ktor-client-cronet"))

                implementation(shared.ktor.client.logging.jvm)
                implementation(shared.cronet.api)
            }
        }
        iosMain.dependencies {
            implementation(shared.ktor.client.darwin)
        }
    }

    compilerOptions {
        // Common compiler options applied to all Kotlin source sets
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
}