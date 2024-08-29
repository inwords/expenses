import com.inwords.expenses.plugins.SharedKmmLibraryPlugin.Companion.applyKmmDefaults
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("shared-kmm-library-plugin")
    alias(shared.plugins.kotlin.serialization)
    alias(shared.plugins.compose.compiler)
    alias(shared.plugins.compose.multiplatform.compiler)
}

android {
    namespace = "com.inwords.expenses.feature.events"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        compose = true
    }
}

kotlin {
    applyKmmDefaults("shared-events")

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":shared:core:utils"))
                implementation(project(":shared:core:storage-utils"))
                implementation(project(":shared:core:ui-utils"))
                implementation(project(":shared:core:navigation"))
                implementation(project(":shared:core:network"))
                implementation(project(":shared:feature:settings"))

                implementation(shared.coroutines.core)

                implementation(shared.kotlinx.serialization.json)
                implementation(shared.kotlinx.datetime)

                implementation(shared.lifecycle.runtime.compose.multiplatform)
                implementation(shared.lifecycle.viewmodel.compose.multiplatform)

                implementation(shared.room.runtime)

                implementation(shared.ktor.client.core)

                implementation(compose.ui)
                implementation(compose.material3)

                implementation(shared.navigation.compose.multiplatform)
            }
        }
        androidMain {
            dependencies {
                implementation(shared.lifecycle.viewmodel.compose)
                implementation(shared.navigation.compose)

                val composeBom = project.dependencies.platform(shared.compose.bom)
                implementation(composeBom)
                implementation(shared.compose.ui)
                implementation(shared.compose.material3)
                implementation(shared.compose.ui.tooling)
            }
        }
    }

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        // Common compiler options applied to all Kotlin source sets
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
}
