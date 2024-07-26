import com.inwords.expenses.plugins.SharedKmmLibraryPlugin.Companion.applyKmmDefaults

plugins {
    id("shared-kmm-library-plugin")
}

android {
    namespace = "com.inwords.expenses.core.utils"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

kotlin {
    applyKmmDefaults("shared-core-utils")

    sourceSets {
        commonMain {
            dependencies {
                implementation(shared.coroutines.core)

                implementation(shared.kotlinx.collections.immutable)

                implementation(shared.ionspin.kotlin.bignum)
            }
        }
    }
}
