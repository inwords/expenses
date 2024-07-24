import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag

plugins {
    alias(buildSrc.plugins.android.application)
    alias(buildSrc.plugins.kotlin.android)
    alias(shared.plugins.compose.compiler)
    alias(shared.plugins.kotlin.serialization)
}

android {
    namespace = "com.inwords.expenses"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.inwords.expenses"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(11))
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }

    composeCompiler {
        featureFlags.add(ComposeFeatureFlag.StrongSkipping)
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":shared:core:utils"))
    implementation(project(":shared:core:storage-utils"))
    implementation(project(":shared:core:ui-utils"))
    implementation(project(":shared:core:network"))
    implementation(project(":shared:core:navigation"))
    implementation(project(":shared:feature:settings"))
    implementation(project(":shared:feature:expenses"))
    implementation(project(":shared:feature:events"))
    implementation(project(":shared:integration:databases"))
    implementation(project(":kmmsharedmodule"))

    // network
    implementation(platform(shared.okhttp.bom))
    implementation(shared.okhttp)
    implementation(shared.cronet)

    // coroutines
    implementation(shared.coroutines.android)

    // serialization
    implementation(shared.kotlinx.serialization.json)

    //datetime
    implementation(shared.kotlinx.datetime)

    // db
    implementation(shared.room.runtime)

    // compose
    val composeBom = platform(shared.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation(shared.compose.ui)
    implementation(shared.compose.ui.tooling)
    implementation(shared.compose.material3)

    // other UI-related staff
    implementation(shared.coil.base)
    implementation(shared.coil.compose.base)

    implementation(shared.navigation.compose)

    // lifecycle
    implementation(shared.lifecycle.runtime.ktx)
    implementation(shared.lifecycle.runtime.compose)
    implementation(shared.lifecycle.viewmodel.compose)

    // misc androidx stuff
    implementation(shared.core.ktx)
    implementation(shared.activity.compose)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    implementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

}