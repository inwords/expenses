import com.inwords.expenses.plugins.SharedKmmLibraryPlugin.Companion.applyKmmDefaults

plugins {
    id("shared-kmm-library-plugin")
    alias(shared.plugins.atomicfu)
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

                implementation(shared.datastore.core.okio)
                implementation("org.jetbrains.kotlinx:atomicfu:0.26.1") // TODO remove when atomicfu plugin is fixed

                implementation(shared.ionspin.kotlin.bignum)
            }
        }
    }
}
