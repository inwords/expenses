plugins {
    id("shared-library-plugin")
}

android {
    namespace = "com.inwords.expenses.core.ui.utils"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    implementation(shared.coroutines.core)
}