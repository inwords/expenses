plugins {
    id("shared-library-plugin")
    id(shared.plugins.ksp.get().pluginId)
}

android {
    namespace = "com.inwords.expenses.databases"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }
}

dependencies {
    implementation(project(":shared:core:utils"))
    implementation(project(":shared:core:storage-utils"))
    implementation(project(":shared:feature:expenses"))
    implementation(project(":shared:feature:events"))

    // db
    implementation(shared.roomRuntime)
    ksp(shared.roomCompiler)

    implementation(shared.annotation)

    implementation(shared.coroutinesAndroid)

    implementation(shared.kotlinxDatetime)

    // compose
    val composeBom = platform(shared.composeBom)
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation(shared.composeRuntime) // TODO doesn't work without this - seems to be a Room KSP bug
}