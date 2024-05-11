pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("shared") {
            from(files("gradle/shared.versions.toml"))
        }
        create("buildSrc") {
            from(files("gradle/buildSrc.versions.toml"))
        }
    }
}
rootProject.name = "Expenses"

include(":app")
include(":kmmsharedmodule")

include(":shared")
include(":shared:core")
include(":shared:core:navigation")
include(":shared:core:network")
include(":shared:core:ktor-client-cronet")
include(":shared:core:utils")
include(":shared:core:storage-utils")
include(":shared:core:ui-utils")

include("shared:feature")
include("shared:feature:events")
include("shared:feature:expenses")

include("shared:integration")
include("shared:integration:databases")