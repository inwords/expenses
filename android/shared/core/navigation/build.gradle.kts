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
    implementation(shared.navigationCompose)
    implementation(shared.lifecycleViewModelCompose)
}