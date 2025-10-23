import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(buildSrc.plugins.android.application)
    alias(buildSrc.plugins.kotlin.android)
    alias(shared.plugins.compose.compiler)
    alias(shared.plugins.android.junit5)
    alias(shared.plugins.sentry.android.gradle)
    alias(shared.plugins.androidx.baselineprofile)
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)

        freeCompilerArgs.add("-Xcontext-parameters")
        freeCompilerArgs.add("-Xdata-flow-based-exhaustiveness")
        extraWarnings.set(true)
    }
}

android {
    namespace = "ru.commonex"
    compileSdk = 36

    defaultConfig {
        applicationId = "ru.commonex"
        minSdk = 26
        targetSdk = 36
        versionCode = 3
        versionName = "2025.10.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["clearPackageData"] = "true"

        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = if (System.getenv("CI") == "true") {
                signingConfigs.create("release") {
                    storeFile = file("keystore.jks")
                    storePassword = System.getenv("KEYSTORE_PASSWORD")
                    keyAlias = System.getenv("KEY_ALIAS")
                    keyPassword = System.getenv("KEY_PASSWORD")
                }
            } else {
                signingConfigs.getByName("debug")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    testOptions {
        animationsDisabled = true
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":shared:core:utils"))
    implementation(project(":shared:core:ui-design"))
    implementation(project(":shared:integration:base"))

    // network
    implementation(shared.cronet.embedded)

    // compose
    val composeBom = platform(shared.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation(shared.compose.ui)
    implementation(shared.compose.material3)
    implementation(shared.compose.ui.tooling)

    // misc androidx stuff
    implementation(shared.core.ktx)
    implementation(shared.activity.compose)

    implementation(shared.work.runtime.ktx)

    implementation(shared.profileinstaller)

    testImplementation(shared.junit.jupiter.api)
    testRuntimeOnly(shared.junit.jupiter.engine)
    androidTestImplementation(shared.junit.jupiter.api)
    androidTestImplementation(shared.androidx.test.ext.junit)
    androidTestImplementation(shared.androidx.test.runner)
    androidTestImplementation(shared.androidx.test.espresso.core)
    androidTestUtil(shared.androidx.test.orchestrator)
    androidTestImplementation(shared.androidx.compose.ui.test.android)

    baselineProfile(project(":baselineprofile"))
}

sentry {
    org.set("inwords")
    projectName.set("commonex")

    authToken.set(System.getenv("SENTRY_AUTH_TOKEN"))

    val uploadMapping = System.getenv("CI") == "true"
    autoUploadProguardMapping.set(uploadMapping)

    tracingInstrumentation {
        enabled.set(true)

        features.set(emptySet())
    }

    autoInstallation {
        enabled.set(false)
    }

    includeDependenciesReport = false

    telemetry.set(false)
}

baselineProfile {
    dexLayoutOptimization = true
}
