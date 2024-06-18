plugins {
    id("shared-library-plugin")
    id(shared.plugins.kotlin.serialization.get().pluginId)
    alias(shared.plugins.compose.compiler)
}

android {
    namespace = "com.inwords.expenses.feature.expenses"

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
    implementation(project(":shared:core:utils"))
    implementation(project(":shared:core:storage-utils"))
    implementation(project(":shared:core:ui-utils"))
    implementation(project(":shared:core:navigation"))
    implementation(project(":shared:feature:events"))
    implementation(project(":shared:feature:settings"))

    implementation(shared.coroutines.android)

    implementation(shared.kotlinxSerializationJson)
    implementation(shared.kotlinxDatetime)
    implementation(shared.kotlinx.collections.immutable.jvm)

    implementation(shared.lifecycleRuntimeCompose)
    implementation(shared.lifecycleViewModelCompose)

    implementation(shared.roomRuntime)

    implementation(shared.navigationCompose)

    val composeBom = platform(shared.composeBom)
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation(shared.composeUi)
    implementation(shared.composeMaterial3)
    implementation(shared.compose.ui.tooling)
}