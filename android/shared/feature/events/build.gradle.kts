plugins {
    id("shared-library-plugin")
}

android {
    namespace = "com.inwords.expenses.feature.events"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    implementation(shared.coroutinesAndroid)

    implementation(shared.roomRuntime)

    implementation(shared.kotlinxDatetime)

    implementation(shared.navigationCompose)

    val composeBom = platform(shared.composeBom)
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation(shared.composeUi)
    implementation(shared.composeMaterial3)
    implementation(shared.composeUiToolingPreview)
}