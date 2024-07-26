import com.inwords.expenses.plugins.SharedKmmLibraryPlugin.Companion.applyKmmDefaults

plugins {
    id("shared-kmm-library-plugin")
}

android {
    namespace = "com.inwords.expenses.kmmsharedmodule"
}

kotlin {
    applyKmmDefaults("kmmsharedmodule")

    sourceSets {
        commonMain {
            dependencies {
                implementation(shared.coroutines.core)
            }
        }
        androidMain {
            dependencies {
                implementation(shared.coroutines.android)
            }
        }
    }
}
