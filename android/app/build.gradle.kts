plugins {
    id(buildSrc.plugins.android.application.get().pluginId)
    id(buildSrc.plugins.kotlin.android.get().pluginId)
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
        enableStrongSkippingMode = true
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
    implementation(platform(shared.okHttpBom))
    implementation(shared.okHttp)
    implementation(shared.cronet)

    // coroutines
    implementation(shared.coroutines.android)

    // serialization
    implementation(shared.kotlinxSerializationJson)

    //datetime
    implementation(shared.kotlinxDatetime)

    // db
    implementation(shared.roomRuntime)

    // compose
    val composeBom = platform(shared.composeBom)
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation(shared.composeUi)
    implementation(shared.compose.ui.tooling)
    implementation(shared.composeMaterial3)

    // other UI-related staff
    implementation(shared.coilBase)
    implementation(shared.coilComposeBase)

    implementation(shared.navigationCompose)

    // lifecycle
    implementation(shared.lifecycleRuntimeKtx)
    implementation(shared.lifecycleRuntimeCompose)
    implementation(shared.lifecycleViewModelCompose)

    // misc androidx stuff
    implementation(shared.coreKtx)
    implementation(shared.activityCompose)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    implementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

}