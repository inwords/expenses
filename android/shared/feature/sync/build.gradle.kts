import com.inwords.expenses.plugins.SharedKmmLibraryPlugin.Companion.applyKmmDefaults
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("shared-kmm-library-plugin")
}

android {
    namespace = "com.inwords.expenses.feature.sync"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

kotlin {
    applyKmmDefaults("shared-sync")

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":shared:core:utils"))
                implementation(project(":shared:core:locator"))
                implementation(project(":shared:feature:events"))
                implementation(project(":shared:feature:expenses"))

                implementation(shared.coroutines.core)
            }
        }
        androidMain {
            dependencies {
                implementation(shared.work.runtime.ktx)
            }
        }
    }

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        // Common compiler options applied to all Kotlin source sets
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
}
