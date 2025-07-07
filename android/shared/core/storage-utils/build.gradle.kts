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
                implementation(shared.kotlinx.atomicfu) // TODO remove when atomicfu plugin is fixed

                implementation(shared.ionspin.kotlin.bignum)
            }
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlin.time.ExperimentalTime")
    }
}
