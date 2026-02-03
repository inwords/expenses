# Gradle Tooling

## Gradle Versions Plugin Usage

GitHub: [Gradle Versions Plugin](https://github.com/ben-manes/gradle-versions-plugin)

To check for dependency updates run:
`./gradlew dependencyUpdates --refresh-dependencies -Drevision=release`

Report location:
`./build/dependency-updates/report.txt`

To update JDK, see [gradle-daemon-jvm.properties](gradle-daemon-jvm.properties)
and [android.yml](../../.github/workflows/android.yml).
Available distributions are listed at:
https://github.com/actions/setup-java?tab=readme-ov-file#supported-distributions

## Gradle Build Benchmarks (Gradle Profiler)

Gradle Profiler is used to benchmark build performance.

Locations:

- Scenario file: `android/gradle/performance.scenarios` (run using `gradle/performance.scenarios` from `android/`)
- Profiler distribution: `android/gradle/profiler/`

The Gradle Profiler library must be extracted into `android/gradle/profiler/` manually;
its binaries are excluded from git to save space.

Download: https://github.com/gradle/gradle-profiler/releases

Run from the `android/` directory.

### CI/CD benchmark (clean build)

## Run (Windows / CMD)

```cmd
rem CI/CD benchmark
.\gradle\profiler\bin\gradle-profiler --benchmark --project-dir . --scenario-file gradle\performance.scenarios clean_build --no-daemon
```

## Run (bash)

```bash
# CI/CD benchmark
./gradle/profiler/bin/gradle-profiler --benchmark --project-dir . --scenario-file gradle/performance.scenarios clean_build --no-daemon
```

### Local debug benchmark

## Run (Windows / CMD)

```cmd
rem Local debug benchmark
.\gradle\profiler\bin\gradle-profiler --benchmark --project-dir . --scenario-file gradle\performance.scenarios debug_build
```

## Run (bash)

```bash
# Local debug benchmark
./gradle/profiler/bin/gradle-profiler --benchmark --project-dir . --scenario-file gradle/performance.scenarios debug_build
```