import com.inwords.expenses.plugins.SharedKmmLibraryPlugin.Companion.applyKmmDefaults

plugins {
    id("shared-kmm-library-plugin")
}

android {
    namespace = "com.inwords.expenses.core.storage.utils"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

kotlin {
    applyKmmDefaults("shared-core-storage-utils")

    sourceSets {
        commonMain {
            dependencies {
                implementation(shared.kotlinx.datetime)

                implementation(shared.room.runtime)

                implementation(shared.ionspin.kotlin.bignum)
            }
        }
    }
}
