# Gradle Versions Plugin Usage

GutHub: [Gradle Versions Plugin](https://github.com/ben-manes/gradle-versions-plugin)

To check fore dependency updates run the following command:
`./gradlew dependencyUpdates --refresh-dependencies -Drevision=release`

Report will be generated in the following location:
`./build/dependency-updates/report.txt`

To update JDK, see the [gradle-daemon-jvm.properties](gradle-daemon-jvm.properties) 
and [android.yml](../../.github/workflows/android.yml).
Available distributions are: https://github.com/actions/setup-java?tab=readme-ov-file#supported-distributions