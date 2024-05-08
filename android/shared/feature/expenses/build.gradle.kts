plugins {
    id("shared-library-plugin")
    id(shared.plugins.kotlin.serialization.get().pluginId)
}

android {
    namespace = "com.inwords.expenses.feature.expenses"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = shared.versions.composeCompilerExtension.get()
    }
}

dependencies {
    implementation(project(":shared:core:utils"))
    implementation(project(":shared:core:storage-utils"))
    implementation(project(":shared:core:ui-utils"))
    implementation(project(":shared:core:navigation"))
    implementation(project(":shared:feature:events"))

    implementation(shared.coroutinesAndroid)

    implementation(shared.kotlinxSerializationJson)
    implementation(shared.kotlinxDatetime)

    implementation(shared.lifecycleRuntimeCompose)
    implementation(shared.lifecycleViewModelCompose)

    implementation(shared.roomRuntime)

    implementation(shared.navigationCompose)

    val composeBom = platform(shared.composeBom)
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation(shared.composeUi)
    implementation(shared.composeMaterial3)
    implementation(shared.composeUiToolingPreview)
}