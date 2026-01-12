import com.inwords.expenses.plugins.SharedKmmLibraryPlugin.Companion.applyKmmDefaults

plugins {
    id("shared-kmm-library-plugin")
    alias(shared.plugins.ksp)
    alias(shared.plugins.room)
}

kotlin {
    android {
        namespace = "com.inwords.expenses.integration.databases"

        @Suppress("UnstableApiUsage")
        optimization {
            consumerKeepRules.files.add(file("consumer-rules.pro"))
        }
    }

    applyKmmDefaults("sharedIntegrationDatabases")

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":shared:core:utils"))
                implementation(project(":shared:core:storage-utils"))
                implementation(project(":shared:feature:expenses"))
                implementation(project(":shared:feature:events"))

                // db
                implementation(shared.room.runtime)
                implementation(shared.sqlite.bundled)

                implementation(shared.annotation)

                implementation(shared.coroutines.core)

                implementation(shared.kotlinx.datetime)

                implementation(shared.ionspin.kotlin.bignum)
            }
        }
        androidMain {
            dependencies {
                // FIXME: remove https://github.com/google/ksp/issues/1896
                val composeBom = project.dependencies.platform(shared.compose.bom)
                implementation(composeBom)
                implementation(shared.compose.runtime)
            }
        }
    }

    compilerOptions {
        // Common compiler options applied to all Kotlin source sets
        freeCompilerArgs.add("-Xexpect-actual-classes")
        freeCompilerArgs.add("-opt-in=kotlin.time.ExperimentalTime")
    }
}

dependencies {
    // TODO is there a better way to do this?
    kspAndroid(shared.room.compiler)
    add("kspIosArm64", shared.room.compiler)
    add("kspIosSimulatorArm64", shared.room.compiler)
}

room {
    schemaDirectory("$projectDir/schemas")
}
