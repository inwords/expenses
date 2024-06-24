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
    implementation(shared.ktorClientCore)

    implementation(shared.cronetApi)
}