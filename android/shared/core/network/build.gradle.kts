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
    implementation(shared.ktorClientOkHttp)
    implementation(shared.ktorClientContentNegotiation)
    implementation(shared.ktorSerializationKotlinxJson)
    implementation(shared.ktorClientLoggingJvm)
}