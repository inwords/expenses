package com.inwords.expenses.plugins

import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class SharedLibraryPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.plugins.apply("com.android.library")
        project.plugins.apply("kotlin-android")

        val android = project.extensions.getByType<LibraryExtension>()
        val kotlin = project.extensions.getByType<KotlinAndroidProjectExtension>()

        android.apply {
            compileSdk = 36
            defaultConfig {
                minSdk = 26
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }
            testOptions {
                targetSdk = 36
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_11
                targetCompatibility = JavaVersion.VERSION_11
            }
        }

        kotlin.compilerOptions.jvmTarget.set(JvmTarget.JVM_11)
    }
}