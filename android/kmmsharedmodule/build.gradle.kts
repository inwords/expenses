import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform")
    id(buildSrc.plugins.android.library.get().pluginId)
}
afterEvaluate {
    tasks.withType<JavaCompile>().configureEach {
        sourceCompatibility = JavaVersion.VERSION_11.toString()
        targetCompatibility = JavaVersion.VERSION_11.toString()
    }
}
kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "kmmsharedmodule"
        }
    }

    jvmToolchain(11)

    sourceSets {
        commonMain {
            dependencies {
                implementation(shared.coroutines.core)
            }
        }
        androidMain {
            dependencies {
                implementation(shared.coroutines.android)
            }
        }

        iosArm64()
        iosX64()
        iosSimulatorArm64()
    }
}

android {
    namespace = "com.inwords.expenses.kmmsharedmodule"
    compileSdk = 34
    defaultConfig {
        minSdk = 26
    }
}