// Top-level build file where configuration options common to all sub-projects/modules can be added.
buildscript {
    dependencies {
        classpath(buildSrc.kotlinGradlePlugin)
    }
}

plugins {
    // TODO figure out the right way
    id("shared-library-plugin") apply false
    id(buildSrc.plugins.android.application.get().pluginId) apply false
    id(buildSrc.plugins.android.library.get().pluginId) apply false
    id(buildSrc.plugins.kotlin.android.get().pluginId) apply false
    alias(shared.plugins.compose.compiler) apply false
    alias(shared.plugins.kotlin.serialization) apply false
    alias(shared.plugins.ksp) apply false
    alias(shared.plugins.protobuf) apply false
}
