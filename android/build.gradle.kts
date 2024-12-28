import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

// Top-level build file where configuration options common to all sub-projects/modules can be added.
buildscript {
    dependencies {
        classpath(buildSrc.kotlin.gradle.plugin)
    }
}

plugins {
    id("shared-library-plugin") apply false
    alias(buildSrc.plugins.android.application) apply false
    alias(buildSrc.plugins.android.library) apply false
    alias(buildSrc.plugins.kotlin.android) apply false
    alias(shared.plugins.compose.compiler) apply false
    alias(shared.plugins.compose.multiplatform.compiler) apply false
    alias(shared.plugins.kotlin.serialization) apply false
    alias(shared.plugins.ksp) apply false
    alias(shared.plugins.room) apply false
    alias(shared.plugins.wire) apply false
    alias(shared.plugins.atomicfu) apply false
    alias(shared.plugins.gradle.versions.checker) apply true
}

// https://github.com/ben-manes/gradle-versions-plugin
tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        isNonStable(candidate.version) && !isNonStable(currentVersion)
    }
}

private fun isNonStable(version: String): Boolean {
    val preReleaseKeywords = listOf("alpha", "beta", "rc", "snapshot", "eap", "preview", "+dev", "-dev")

    val hasPreReleaseMarker = preReleaseKeywords.any { version.contains(it, ignoreCase = true) }

    return hasPreReleaseMarker
}