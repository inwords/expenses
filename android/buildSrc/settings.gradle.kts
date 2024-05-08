dependencyResolutionManagement {
    versionCatalogs {
        create("buildSrc") {
            from(files("../gradle/buildSrc.versions.toml"))
        }
    }
}