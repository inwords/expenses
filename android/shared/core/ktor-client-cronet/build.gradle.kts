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
    implementation(shared.ktorClientCore)

    implementation(shared.cronet)

}