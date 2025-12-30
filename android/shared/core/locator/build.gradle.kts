import com.inwords.expenses.plugins.SharedKmmLibraryPlugin.Companion.applyKmmDefaults

plugins {
    id("shared-kmm-library-plugin")
}

kotlin {
    android {
        namespace = "com.inwords.expenses.core.locator"

        @Suppress("UnstableApiUsage")
        optimization {
            consumerKeepRules.files.add(file("consumer-rules.pro"))
        }
    }
    applyKmmDefaults("sharedCoreLocator")

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":shared:core:utils"))

                implementation(shared.kotlinx.atomicfu) // TODO remove when atomicfu plugin is fixed
            }
        }
    }
}
