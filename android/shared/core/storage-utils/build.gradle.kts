plugins {
    id("shared-library-plugin")
}

android {
    namespace = "com.inwords.expenses.core.storage.utils"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    implementation(shared.kotlinx.datetime)

    implementation(shared.room.runtime)

    implementation(shared.annotation)
}