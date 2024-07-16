plugins {
    id("shared-library-plugin")
    alias(shared.plugins.compose.compiler)
}

android {
    namespace = "com.inwords.expenses.core.navigation"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        compose = true
    }

    composeCompiler {
        enableStrongSkippingMode = true
    }
}

dependencies {
    implementation(shared.navigation.compose)
    implementation(shared.lifecycle.viewmodel.compose)
}