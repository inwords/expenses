# Android Application Release Standard Operating Procedure (SOP)

## Purpose
This SOP defines the standardized process for releasing new versions of the Android expenses application. It ensures consistent versioning, proper profile generation, and reliable tagging for production deployments.

## Prerequisites
- [ ] Access to the project repository with write permissions
- [ ] Local development environment with Android SDK configured
- [ ] Gradle wrapper (`gradlew`) executable permissions
- [ ] Current branch is `main`
- [ ] All necessary changes already committed
- [ ] Current version information from `app/build.gradle.kts`

## Version Naming Convention
- **Format**: `YYYY.MM.N` where:
  - `YYYY` = Current year (4 digits)
  - `MM` = Current month (2 digits, zero-padded)
  - `N` = Release number for the month (starting at 1)
- **Example**: `2025.12.1` (first release of December 2025)

## Release Process

### Step 1: Version Bump
**Objective**: Update application version identifiers

1. **Locate version configuration**:
   - Open `app/build.gradle.kts`
   - Find the `defaultConfig` block containing `versionCode` and `versionName`

2. **Update version values**:
   - **versionCode**: Increment by 1 (e.g., `3` → `4`)
   - **versionName**: Update to new release version following naming convention
   
   Example change:
   ```kotlin
   versionCode = 4  // was 3
   versionName = "2025.12.1"  // was "2025.10.1"
   ```

3. **Commit the version change**:
   ```bash
   git add app/build.gradle.kts
   git commit -m "Bump version to YYYY.MM.N"
   ```

### Step 2: Generate Performance Profiles
**Objective**: Create optimized baseline and startup profiles for the new version

1. **Execute profile generation**:
   ```bash
   ./gradlew :app:generateBaselineProfile
   ```

2. **Verify profile generation**:
   - Check that the command completes successfully
   - Confirm new/updated profile files are generated
   - Review any warnings or errors in the output

3. **Commit profile updates**:
   ```bash
   git add app/src/release/generated/baselineProfiles/
   git commit -m "Update baseline profiles for version YYYY.MM.N"
   ```

### Step 3: Create Release Tag
**Objective**: Tag the release for deployment tracking

1. **Determine tag name**:
   - **Format**: `release/YYYY-MM-N/P`
   - `P` = Patch number (starting at 1 for each release)
   - **Example**: `release/2025-12-1/1`

2. **Create and push tag** (manual trigger when ready):
   ```bash
   git tag release/YYYY-MM-N/P
   git push origin release/YYYY-MM-N/P
   ```

## Validation Checklist
- [ ] `versionCode` incremented correctly in `build.gradle.kts`
- [ ] `versionName` follows `YYYY.MM.N` format
- [ ] Version bump commit uses exact format: "Bump version to YYYY.MM.N"
- [ ] Baseline profile generation completed without errors
- [ ] Profile commit uses exact format: "Update baseline and startup profiles for version YYYY.MM.N"
- [ ] Tag created with correct `release/YYYY-MM-N/P` format
- [ ] All commits are pushed to repository

## Troubleshooting

### Profile Generation Fails
- **Issue**: `./gradlew :app:generateBaselineProfile` fails
- **Solution**: 
  1. Review error logs for specific issues
  2. Check device/emulator availability for profile benchmarks

### Version Conflicts
- **Issue**: Version already exists or conflicts
- **Solution**:
  1. Check existing tags: `git tag -l "release/*"`
  2. Increment patch number (e.g., `/1` → `/2`)
  3. For same-month releases, increment release number (e.g., `2025.12.1` → `2025.12.2`)

### Commit Message Errors
- **Issue**: Incorrect commit message format
- **Solution**: 
  1. Amend last commit: `git commit --amend -m "Correct message"`
  2. Force push if already pushed: `git push --force-with-lease`

## Notes for AI Assistants
- Always verify current version before making changes
- Follow exact commit message formats
- Ensure all steps complete successfully before proceeding
- Tag creation should be triggered manually when deployment is ready
