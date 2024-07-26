plugins {
    id("shared-library-plugin")
    alias(shared.plugins.kotlin.serialization)
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
}

dependencies {
    implementation(project(":shared:core:utils"))
    implementation(project(":shared:core:storage-utils"))
    implementation(project(":shared:core:ui-utils"))
    implementation(project(":shared:core:navigation"))
    implementation(project(":shared:feature:events"))
    implementation(project(":shared:feature:settings"))

    implementation(shared.coroutines.android)

    implementation(shared.kotlinx.serialization.json)
    implementation(shared.kotlinx.datetime)
    implementation(shared.kotlinx.collections.immutable.jvm)

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

    implementation(shared.ionspin.kotlin.bignum)
}