plugins {
    `kotlin-dsl`
}

repositories {
    google {
        content {
            includeGroupByRegex("com\\.android.*")
            includeGroupByRegex("com\\.google.*")
            includeGroupByRegex("androidx.*")
        }
    }
    mavenCentral()
    gradlePluginPortal()
}

gradlePlugin {
    plugins {
        register("shared-library-plugin") {
            id = "shared-library-plugin"
            implementationClass = "com.inwords.expenses.plugins.SharedLibraryPlugin"
        }
    }
}

dependencies {

    implementation(buildSrc.kotlin.gradle.plugin)
    implementation(buildSrc.agp)
}