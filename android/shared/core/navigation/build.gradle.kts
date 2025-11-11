import com.inwords.expenses.plugins.SharedKmmLibraryPlugin.Companion.applyKmmDefaults

plugins {
    id("shared-kmm-library-plugin")
    alias(shared.plugins.kotlin.serialization)
    alias(shared.plugins.compose.compiler)
    alias(shared.plugins.compose.multiplatform.compiler)
}

android {
    namespace = "com.inwords.expenses.core.navigation"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        compose = true
    }
}

kotlin {
    applyKmmDefaults("shared-core-navigation")

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":shared:core:utils"))

                // TODO the whole ktor for URL parsing
                implementation(shared.ktor.client.core)

                implementation(shared.kotlinx.serialization.json)

                // for BottomSheetScene
                implementation(shared.compose.material3.multiplatform)

                implementation(shared.lifecycle.viewmodel.compose.multiplatform)
                implementation(shared.navigation3.ui.multiplatform)
            }
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
}
