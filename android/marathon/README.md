# Marathon (Android Instrumented Tests)

Marathon is the test runner used for Android instrumented tests in this project. The Marathon library can be extracted into this folder manually to run Marathon; library files are excluded from git to save space.

Run from the android/ directory.

## Run (Windows / CMD)

```cmd
rem Build APKs for Marathon
.\gradlew :app:assembleAutotest :app:assembleAutotestAndroidTest -Dcom.android.tools.r8.disableApiModeling

rem Ensure Android SDK path is set
set ANDROID_SDK_ROOT=C:\Users\vasia\AppData\Local\Android\Sdk

rem Run Marathon using the local distribution
.\marathon\bin\marathon
```

## Run (bash)

```bash
# Build APKs for Marathon
./gradlew :app:assembleAutotest :app:assembleAutotestAndroidTest -Dcom.android.tools.r8.disableApiModeling

# Ensure Android SDK path is set
export ANDROID_SDK_ROOT="/path/to/Android/Sdk"

# Run Marathon using the local distribution
./marathon/bin/marathon
```