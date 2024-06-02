plugins {
    id("shared-library-plugin")
    alias(shared.plugins.protobuf)
}

android {
    namespace = "com.inwords.expenses.feature.settings"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

protobuf {
    protoc {
        artifact = shared.protobuf.protoc.get().toString()
    }
    generateProtoTasks {
        all().configureEach {
            builtins {
                create("java") {
                    option("lite")
                }
                create("kotlin") {
                    option("lite")
                }
            }
        }
    }
}

dependencies {
    implementation(project(":shared:core:utils"))

    implementation(shared.protobuf.kotlin.lite)

    implementation(shared.datastore.android)
}