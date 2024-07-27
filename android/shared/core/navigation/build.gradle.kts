import com.inwords.expenses.plugins.SharedKmmLibraryPlugin.Companion.applyKmmDefaults

plugins {
    id("shared-kmm-library-plugin")
    alias(shared.plugins.compose.compiler)
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
                implementation(shared.lifecycle.viewmodel.compose.experimantal)
                implementation(shared.navigation.compose.experimental)
            }
        }
    }
}
