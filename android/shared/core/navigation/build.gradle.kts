import com.inwords.expenses.plugins.SharedKmmLibraryPlugin.Companion.applyKmmDefaults

plugins {
    id("shared-kmm-library-plugin")
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

                // for BottomSheetScene
                implementation(shared.compose.material3.multiplatform)

                implementation(shared.lifecycle.viewmodel.compose.multiplatform)
                implementation(shared.navigation3.ui.multiplatform)
            }
        }
    }
}
