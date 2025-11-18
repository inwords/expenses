package com.inwords.expenses.plugins

import com.android.build.api.dsl.androidLibrary
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class SharedKmmLibraryPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.pluginManager.apply("com.android.kotlin.multiplatform.library")
        project.pluginManager.apply("org.jetbrains.kotlin.multiplatform")

        val kotlin = project.extensions.getByType<KotlinMultiplatformExtension>()

        kotlin.apply {
            androidLibrary {
                compileSdk = 36
                minSdk = 26

                compilations.configureEach {
                    compileTaskProvider.configure {
                        compilerOptions {
                            jvmTarget.set(JvmTarget.JVM_11)
                        }
                    }
                }

                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_11)
                }
            }

            compilerOptions {
                extraWarnings.set(true)
                freeCompilerArgs.add("-Xdata-flow-based-exhaustiveness")
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