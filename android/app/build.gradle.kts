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
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}

android {
    namespace = "ru.commonex"
    compileSdk = 36

    defaultConfig {
        applicationId = "ru.commonex"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

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

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
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

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.13.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.13.1")
    androidTestImplementation("org.junit.jupiter:junit-jupiter-api:5.13.1")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test:runner:1.6.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestUtil("androidx.test:orchestrator:1.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-android:1.8.3")

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
