import com.inwords.expenses.plugins.SharedKmmLibraryPlugin.Companion.applyKmmDefaults

plugins {
    id("shared-kmm-library-plugin")
    alias(shared.plugins.compose.compiler)
    alias(shared.plugins.compose.multiplatform.compiler)
}

kotlin {
    android {
        namespace = "com.inwords.expenses.core.ui.utils"

        @Suppress("UnstableApiUsage")
        optimization {
            consumerKeepRules.files.add(file("consumer-rules.pro"))
        }
    }

    applyKmmDefaults("sharedCoreUiUtils")

    sourceSets {
        commonMain {
            dependencies {
                implementation(shared.coroutines.core)
                implementation(shared.kotlinx.datetime)

                implementation(shared.compose.ui.multiplatform)
                implementation(shared.compose.components.resources.multiplatform)
            }
        }
    }
}
