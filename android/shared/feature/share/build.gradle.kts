import com.inwords.expenses.plugins.SharedKmmLibraryPlugin.Companion.applyKmmDefaults

plugins {
    id("shared-kmm-library-plugin")
    alias(shared.plugins.compose.compiler)
    alias(shared.plugins.compose.multiplatform.compiler)
}

kotlin {
    android {
        namespace = "com.inwords.expenses.feature.share"

        @Suppress("UnstableApiUsage")
        optimization {
            consumerKeepRules.files.add(file("consumer-rules.pro"))
        }

        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
    }

    applyKmmDefaults("shared-share")

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":shared:core:utils"))
                implementation(project(":shared:core:ui-utils"))

                implementation(shared.compose.runtime.multiplatform)
                implementation(shared.compose.components.resources.multiplatform)
            }
        }
    }

    compilerOptions {
        // Common compiler options applied to all Kotlin source sets
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
}

compose.resources {
    // FIXME: use textFixtures ScreenObjects for tests
    publicResClass = true
}
