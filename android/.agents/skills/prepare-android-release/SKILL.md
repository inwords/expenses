---
name: prepare-android-release
description: "Prepare a CommonEx Android release by following the SOP: bump version, generate baseline profiles, and create/push a release tag. Use when the user asks to prepare an Android release or run the release SOP. Requires DATE, RELEASE_N, PATCH, RUN_FROM (repo-root or android/), and SHELL."
---

# Prepare Android Release

## Overview

Follow the Android release SOP exactly with minimal inputs and deterministic steps.

## Required inputs (ask only for missing values)

- DATE: YYYY-MM-DD (use local system date at execution time; confirm with the user if unsure)
- RELEASE_N: integer release number for the month (1, 2, 3, ...)
- PATCH: integer patch number for the tag (1 for normal release; increment only for hotfixes)
- RUN_FROM: repo-root or android/
- SHELL: PowerShell or bash

## Derived values

- VERSION_NAME = YYYY.MM.N (from DATE + RELEASE_N)
- VERSION_CODE = CURRENT_VERSION_CODE + 1
- TAG = release/YYYY-MM-N/P (VERSION_NAME with dots replaced by hyphens + PATCH)
- PATH_PREFIX = "" if RUN_FROM = android/, otherwise "android/"
- GRADLEW_CMD:
    - repo-root + PowerShell: .\\android\\gradlew -p android
    - repo-root + bash: ./android/gradlew -p android
    - android/ + PowerShell: .\\gradlew
    - android/ + bash: ./gradlew

## Preconditions (stop if any fail)

- On branch main and up to date with origin/main
- All Android changes are committed (non-Android changes may remain)
- Android SDK and a managed device for baseline profiles are available

## Workflow

### Step 1: Bump version

1) Read PATH_PREFIXapp/build.gradle.kts for CURRENT_VERSION_CODE.
2) Edit PATH_PREFIXapp/build.gradle.kts:
    - versionCode = VERSION_CODE
    - versionName = "VERSION_NAME"
3) Commit (exact message format):
    - git add PATH_PREFIXapp/build.gradle.kts
    - git commit -m "Bump version to VERSION_NAME"

### Step 2: Generate baseline/startup profiles

1) Run:
    - GRADLEW_CMD :app:generateBaselineProfile
2) Verify:
    - Build succeeds ("BUILD SUCCESSFUL")
    - Files updated:
        - PATH_PREFIXapp/src/release/generated/baselineProfiles/baseline-prof.txt
        - PATH_PREFIXapp/src/release/generated/baselineProfiles/startup-prof.txt
        - PATH_PREFIXapp/src/autotest/generated/baselineProfiles/baseline-prof.txt
        - PATH_PREFIXapp/src/autotest/generated/baselineProfiles/startup-prof.txt
3) Commit (exact message format):
    - git add PATH_PREFIXapp/src/release/generated/baselineProfiles/ PATH_PREFIXapp/src/autotest/generated/baselineProfiles/
    - git commit -m "Update baseline and startup profiles for version VERSION_NAME"

### Step 3: Tag and push

1) Create tag locally:
    - git tag TAG
2) Push commits:
    - git push origin main
3) Ask for user confirmation before pushing tag. The tag does not trigger deployment; push only after manual deployment:
    - git push origin TAG

## Stop conditions

- If any command fails, stop and report the error; do not proceed.

## Troubleshooting (short)

- Profile generation fails: verify RUN_FROM and GRADLEW_CMD, ensure the emulator is available, then re-run.
- Tag conflict: list existing tags with git tag -l "release/*" and bump PATCH or RELEASE_N as needed.