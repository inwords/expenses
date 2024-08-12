import com.inwords.expenses.plugins.SharedKmmLibraryPlugin.Companion.applyKmmDefaults

plugins {
    id("shared-kmm-library-plugin")
    alias(shared.plugins.atomicfu)
}

android {
    namespace = "com.inwords.expenses.core.locator"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

kotlin {
    applyKmmDefaults("shared-core-locator")

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":shared:core:utils"))

                implementation("org.jetbrains.kotlinx:atomicfu:0.25.0") // TODO remove when atomicfu plugin is fixed
            }
        }
    }
}
