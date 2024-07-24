import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag

plugins {
    id("shared-library-plugin")
    alias(shared.plugins.kotlin.serialization)
    alias(shared.plugins.compose.compiler)
}

android {
    namespace = "com.inwords.expenses.feature.events"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":shared:core:utils"))
    implementation(project(":shared:core:storage-utils"))
    implementation(project(":shared:core:ui-utils"))
    implementation(project(":shared:core:navigation"))
    implementation(project(":shared:feature:settings"))

    implementation(shared.coroutines.android)

    implementation(shared.kotlinx.serialization.json)
    implementation(shared.kotlinx.datetime)

    implementation(shared.lifecycle.runtime.compose)
    implementation(shared.lifecycle.viewmodel.compose)

    implementation(shared.room.runtime)

    implementation(shared.navigation.compose)

    val composeBom = platform(shared.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation(shared.compose.ui)
    implementation(shared.compose.material3)
    implementation(shared.compose.ui.tooling)
}