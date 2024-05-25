plugins {
    id("shared-library-plugin")
    alias(shared.plugins.compose.compiler)
}

android {
    namespace = "com.inwords.expenses.feature.events"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    implementation(shared.coroutines.android)

    implementation(shared.roomRuntime)

    implementation(shared.kotlinxDatetime)

    implementation(shared.navigationCompose)

    val composeBom = platform(shared.composeBom)
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation(shared.composeUi)
    implementation(shared.composeMaterial3)
    implementation(shared.compose.ui.tooling)
}