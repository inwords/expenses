plugins {
    id("shared-library-plugin")
    alias(shared.plugins.ksp)
}

android {
    namespace = "com.inwords.expenses.integration.databases"

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
    implementation(shared.room.runtime)
    ksp(shared.room.compiler)

    implementation(shared.annotation)

    implementation(shared.coroutines.android)

    implementation(shared.kotlinx.datetime)

    // compose
    val composeBom = platform(shared.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation(shared.compose.runtime) // TODO doesn't work without this - seems to be a Room KSP bug

    implementation(shared.ionspin.kotlin.bignum)
}