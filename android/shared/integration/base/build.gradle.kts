import com.inwords.expenses.plugins.SharedKmmLibraryPlugin.Companion.applyKmmDefaults

plugins {
    id("shared-kmm-library-plugin")
    alias(shared.plugins.compose.compiler)
    alias(shared.plugins.compose.multiplatform.compiler)
    alias(shared.plugins.sentry.kotlin.multiplatform)
}

android {
    namespace = "com.inwords.expenses.integration.base"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        compose = true
    }
}

kotlin {
    applyKmmDefaults("shared-integration-base")

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":shared:core:utils"))
                implementation(project(":shared:core:locator"))
                implementation(project(":shared:core:storage-utils"))
                api(project(":shared:core:navigation"))
                implementation(project(":shared:core:network"))
                implementation(project(":shared:feature:events"))
                implementation(project(":shared:feature:expenses"))
                implementation(project(":shared:feature:sync"))
                implementation(project(":shared:feature:share"))
                implementation(project(":shared:feature:settings"))
                implementation(project(":shared:feature:menu"))
                implementation(project(":shared:integration:databases"))

                implementation(shared.coroutines.core)

                implementation(shared.ktor.client.core)

                implementation(shared.lifecycle.runtime.compose.multiplatform)

                implementation(shared.compose.ui.multiplatform)
                implementation(shared.compose.material3.multiplatform)
                implementation(shared.compose.ui.tooling.preview.multiplatform)

                api(shared.navigation.compose.multiplatform)
                implementation(shared.navigation3.ui.multiplatform)
                implementation(shared.lifecycle.viewmodel.navigation3.multiplatform)

                implementation(shared.kotlinx.atomicfu) // TODO remove when atomicfu plugin is fixed
            }
        }
        androidMain {
            dependencies {
                api(shared.navigation.compose)
            }
        }
    }

    compilerOptions {
        // Common compiler options applied to all Kotlin source sets
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
}