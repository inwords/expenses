plugins {
    id("shared-library-plugin")
}

android {
    namespace = "com.inwords.expenses.core.ktor_client_cronet"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    implementation(shared.ktor.client.core)

    implementation(shared.cronet.api)
}