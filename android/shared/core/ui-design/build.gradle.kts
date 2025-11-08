import com.inwords.expenses.plugins.SharedKmmLibraryPlugin.Companion.applyKmmDefaults

plugins {
    id("shared-kmm-library-plugin")
    alias(shared.plugins.compose.compiler)
    alias(shared.plugins.compose.multiplatform.compiler)
}

android {
    namespace = "com.inwords.expenses.core.ui.design"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        compose = true
    }
}

kotlin {
    applyKmmDefaults("shared-core-ui-design")

    sourceSets {
        commonMain {
            dependencies {
                implementation(shared.compose.ui.multiplatform)
                implementation(shared.compose.foundation.multiplatform)
                implementation(shared.compose.material3.multiplatform)
                implementation(shared.compose.ui.tooling.preview.multiplatform)

                implementation(shared.compose.material.icons.core)
            }
        }
        androidMain {
            dependencies {
                implementation(shared.core.ktx)

                implementation(shared.compose.ui.tooling)
            }
        }
    }
}
