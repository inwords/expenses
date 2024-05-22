plugins {
    `kotlin-dsl`
}

repositories {
    google()
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

    implementation(buildSrc.kotlinGradlePlugin)
    implementation(buildSrc.agp)
}