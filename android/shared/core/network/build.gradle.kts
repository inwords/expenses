plugins {
    id("shared-library-plugin")
}

android {
    namespace = "com.inwords.expenses.core.network"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    implementation(project(":shared:core:utils"))
    implementation(project(":shared:core:ktor-client-cronet"))

    implementation(shared.ktor.client.okhttp)
    implementation(shared.ktor.client.content.negotiation)
    implementation(shared.ktor.serialization.kotlinx.json)
    implementation(shared.ktor.client.logging.jvm)

    implementation(shared.cronet.api)

}