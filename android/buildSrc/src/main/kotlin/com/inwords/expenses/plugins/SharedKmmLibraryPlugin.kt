package com.inwords.expenses.plugins

import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class SharedKmmLibraryPlugin : Plugin<Project> {

    @Suppress("UnstableApiUsage")
    override fun apply(project: Project) {
        project.plugins.apply("com.android.library")
        project.plugins.apply("kotlin-multiplatform")

        val android = project.extensions.getByType<LibraryExtension>()
        val kotlin = project.extensions.getByType<KotlinMultiplatformExtension>()

        android.apply {
            compileSdk = 35
            defaultConfig {
                minSdk = 26
            }
            testOptions {
                targetSdk = 35
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8
            }
        }

        kotlin.androidTarget {
            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_1_8)
            }
        }
    }

    companion object {

        fun KotlinMultiplatformExtension.applyKmmDefaults(iosBaseName: String) {
            listOf(
                iosX64(),
                iosArm64(),
                iosSimulatorArm64()
            ).forEach {
                it.binaries.framework {
                    baseName = iosBaseName
                }
            }

            sourceSets.apply {
                iosArm64()
                iosX64()
                iosSimulatorArm64()
            }
        }
    }
}