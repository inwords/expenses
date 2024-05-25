plugins {
    id("shared-library-plugin")
}

android {
    namespace = "com.inwords.expenses.core.utils"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    // coroutines
    implementation(shared.coroutines.android)
}