plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("shared-library-plugin") {
            id = "shared-library-plugin"
            implementationClass = "com.inwords.expenses.plugins.SharedLibraryPlugin"
        }
        register("shared-kmm-library-plugin") {
            id = "shared-kmm-library-plugin"
            implementationClass = "com.inwords.expenses.plugins.SharedKmmLibraryPlugin"
        }
    }
}

dependencies {

    implementation(buildSrc.kotlin.gradle.plugin)
    implementation(buildSrc.agp)
}