import com.inwords.expenses.plugins.SharedKmmLibraryPlugin.Companion.applyKmmDefaults

plugins {
    id("shared-kmm-library-plugin")
    alias(shared.plugins.kotlin.serialization)
    alias(shared.plugins.compose.compiler)
    alias(shared.plugins.compose.multiplatform.compiler)
}

android {
    namespace = "com.inwords.expenses.feature.expenses"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        compose = true
    }
}

kotlin {
    applyKmmDefaults("shared-expenses")

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":shared:core:utils"))
                implementation(project(":shared:core:storage-utils"))
                implementation(project(":shared:core:ui-utils"))
                implementation(project(":shared:core:ui-design"))
                implementation(project(":shared:core:navigation"))
                implementation(project(":shared:core:network"))
                implementation(project(":shared:feature:events"))
                implementation(project(":shared:feature:menu"))
                implementation(project(":shared:feature:settings"))

                implementation(shared.annotation)

                implementation(shared.coroutines.core)

                implementation(shared.kotlinx.serialization.json)
                implementation(shared.kotlinx.datetime)
                implementation(shared.kotlinx.collections.immutable)

                implementation(shared.lifecycle.runtime.compose.multiplatform)
                implementation(shared.lifecycle.viewmodel.compose.multiplatform)

                implementation(shared.room.runtime)

                implementation(shared.ktor.client.core)

                implementation(compose.ui)
                implementation(compose.material3)
                implementation(compose.components.uiToolingPreview)

                implementation(shared.navigation.compose.multiplatform)
                implementation(shared.compose.material.icons.core)

                implementation(shared.ionspin.kotlin.bignum)
            }
        }
        androidMain {
            dependencies {
                implementation(shared.compose.ui.tooling)
            }
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlin.time.ExperimentalTime")
    }
}
