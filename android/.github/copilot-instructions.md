# Copilot Instructions for Expenses (CommonEx) Android Project

## Project Overview

This is a **Kotlin Multiplatform Mobile (KMM)** expenses management application that targets both Android and iOS platforms. The project uses **Jetpack Compose** for UI, **Ktor** for networking, **Room** for local database storage, and follows a modular architecture with feature-based organization.

**Key Technologies:**
- Kotlin 2.2.20 with Compose compiler plugin
- Jetpack Compose with Material3 design system
- Ktor client for networking with Cronet backend (Android) and Darwin backend (iOS)
- Room database with KSP for code generation
- Gradle 8.14.3 with Kotlin DSL and version catalogs
- Android Gradle Plugin 8.13.0
- Target: Android API 36, Min API 26

**Project Size:** ~50 modules across shared core libraries, feature modules, and platform-specific implementations.

**App Features:**
- Event creation and management with person tracking
- Expense recording with currency conversion
- Debt calculation and split management
- Sync functionality with background workers
- Deep linking support (commonex.ru domain)

## Build Instructions

### Prerequisites
- JDK 11+ (Project uses JVM target 11)
- Android SDK with API 36
- Gradle 8.14.3 (use wrapper)

### Essential Commands

**Always use `.\gradlew.bat` (Windows) for all operations. On MacOS use `./gradlew`.**

#### Clean and Build
```powershell
# Clean project (7 seconds)
.\gradlew.bat clean

# Build debug APK (28 seconds from clean)
.\gradlew.bat assembleDebug

# Build release APK (longer, includes obfuscation)
.\gradlew.bat assembleRelease
```

#### Testing
```powershell
# Run all unit tests (25 seconds)
.\gradlew.bat test

# Run specific variant tests
.\gradlew.bat testDebugUnitTest
.\gradlew.bat testReleaseUnitTest

# Run instrumented tests (requires device/emulator)
.\gradlew.bat connectedAndroidTest
```

#### Code Quality
```powershell
# Run lint analysis (48 seconds)
.\gradlew.bat lint --continue

# Apply lint auto-fixes
.\gradlew.bat lintFix

# Generate lint reports at: app/build/reports/lint-results-debug.html
```

#### Dependency Management
```powershell
# Check for dependency updates (5+ minutes)
.\gradlew.bat dependencyUpdates --refresh-dependencies -Drevision=release

# Report location: build/dependencyUpdates/report.txt
```

### Build Warnings and Issues

**Expected Warnings (safe to ignore):**
- `WARNING: The option setting 'android.r8.optimizedResourceShrinking=true' is experimental`
- Cronet namespace warnings in manifest merger
- Redundant visibility modifier warnings from generated Room code
- Native library stripping warnings for specific .so files
- `OpenJDK 64-Bit Server VM warning: Sharing is only supported for boot loader classes` during unit tests

**Build Process Notes:**
- Configuration cache is enabled and may show "incubating feature" warnings
- First build after clean takes ~28 seconds
- Incremental builds are much faster due to Gradle caching
- KSP generates code for Room DAOs and may show redundant modifier warnings
- Dependency updates command may take 5+ minutes and should not be interrupted

## Project Architecture

### Module Structure
```
app/                          # Android application module
shared/                       # Kotlin Multiplatform shared code
  ├── core/                   # Core utilities and infrastructure
  │   ├── ui-design/          # Design system and themes
  │   ├── navigation/         # Navigation components with deep linking
  │   ├── network/            # HTTP client configuration (Ktor + Cronet)
  │   ├── locator/            # Dependency injection container
  │   ├── utils/              # Common utilities
  │   ├── storage-utils/      # Database utilities
  │   ├── ui-utils/           # Compose UI utilities
  │   └── ktor-client-cronet/ # Custom Cronet Engine implementation for Ktor
  ├── feature/                # Feature modules
  │   ├── events/             # Event management (create, join, person management)
  │   ├── expenses/           # Expense tracking (recording, debts, splits)
  │   ├── settings/           # App settings
  │   ├── menu/               # Navigation menu
  │   ├── share/              # Sharing functionality
  │   └── sync/               # Background sync with WorkManager
  └── integration/            # Platform integration
      ├── base/               # Main navigation host and app setup
      └── databases/          # Room database implementation
iosApp/                       # iOS application (SwiftUI)
baselineprofile/              # Android performance profiling
buildSrc/                     # Build logic and plugins
gradle/                       # Version catalogs and properties
```

### Key Configuration Files
- `gradle/shared.versions.toml` - Shared dependency versions
- `gradle/buildSrc.versions.toml` - Build plugin versions  
- `buildSrc/src/main/kotlin/` - Custom Gradle plugins
- `app/proguard-rules.pro` - R8/ProGuard configuration
- `gradle.properties` - Build optimization settings

### Custom Gradle Plugins
- `shared-library-plugin` - Android library module defaults
- `shared-kmm-library-plugin` - KMM module configuration

### Important File Locations
- **Main Activity:** `app/src/main/kotlin/ru/commonex/ui/MainActivity.kt`
- **App Application:** `app/src/main/kotlin/ru/commonex/App.kt` 
- **iOS App:** `iosApp/iosApp/iOSApp.swift`
- **Manifest:** `app/src/main/AndroidManifest.xml` (includes deep linking config)
- **ProGuard:** `app/proguard-rules.pro` (minimal rules for Cronet and protobuf)

### Version Catalog Structure
- **shared.versions.toml:** Main dependencies (Kotlin, Compose, Room, Ktor, etc.)
- **buildSrc.versions.toml:** Build plugins (AGP, Kotlin compiler)
- Centralized version management prevents conflicts across 50+ modules

## Development Guidelines

### Making Changes
1. **Always run tests after changes:** `.\gradlew.bat test`
2. **Check lint issues:** `.\gradlew.bat lint --continue`
3. **For KMM modules:** Changes in `shared/` affect both Android and iOS
4. **Generated code:** Room DAOs are auto-generated by KSP, don't edit manually

### Code Generation
- **Room database:** Uses KSP for DAO generation (expect redundant visibility warnings)
- **Wire protocol buffers:** Used for settings serialization
- **Compose compiler:** Enabled for all modules with Compose UI

### Common Issues and Solutions
- **Build fails after dependency changes:** Run `.\gradlew.bat clean` first
- **KSP errors:** Usually resolved by clean build
- **Version conflicts:** Check `gradle/shared.versions.toml` for centralized versions
- **iOS build issues:** Ensure Xcode is properly configured for the `iosApp` module

### Package Structure
- **Main package:** `ru.commonex` (Android), `com.inwords.expenses` (shared)
- **Namespace pattern:** Feature-based organization (`com.inwords.expenses.feature.{feature-name}`)
- **MainActivity:** `ru.commonex.ui.MainActivity`

### Coding Patterns
- **Dependency Injection:** Uses custom locator pattern in `shared:core:locator`
- **Navigation:** Compose Navigation with deep linking support
- **State Management:** ViewModels with StateFlow for UI state
- **Database:** Room with KMP support, entities in feature modules
- **Network:** Ktor client with Cronet backend for Android
- **Async Operations:** Coroutines with structured concurrency

### Performance Considerations
- **Baseline profiles:** Module at `baselineprofile/` for Android startup optimization
- **R8 optimization:** Enabled for release builds with custom ProGuard rules
- **Cronet networking:** Using Chrome's network stack for better performance

### Testing Strategy
- **Unit tests:** JUnit 5 with test extensions for Android
- **Instrumented tests:** Android Test with Compose testing
- **Device testing:** Managed devices configured in `pixel6Api34*` tasks

## Common Development Tasks

### Adding a New Feature Module
1. Create module under `shared/feature/`
2. Apply `shared-kmm-library-plugin` in build.gradle.kts
3. Add to `settings.gradle.kts` includes
4. Follow naming pattern: `com.inwords.expenses.feature.{feature-name}`

### Working with Database
- Entities are defined in feature modules (e.g., `shared/feature/events/src/commonMain/kotlin/.../data/db/entities/`)
- DAOs are generated by Room with KSP, do not edit generated files
- Database setup is in `shared/integration/databases`

### Adding Dependencies
- Update version catalogs in `gradle/shared.versions.toml` or `gradle/buildSrc.versions.toml`
- Use catalog references in build files: `implementation(shared.some.library)`
- Maintain KMP compatibility for shared modules

### UI Development
- Use Material3 design system from `shared:core:ui-design`
- Compose Multiplatform for shared UI components
- Platform-specific implementations in `androidMain`/`iosMain` source sets

### Known TODOs and Technical Debt
- Several "TODO mvp" comments indicate MVP-level implementations that need improvement
- Atomicfu plugin issues mentioned in multiple build files
- Some String vs Double type inconsistencies in network DTOs
- User agent configuration needs finalization in HTTP client

## Debugging and Troubleshooting

### Build Issues
- **Clean builds solve most KSP issues:** `.\gradlew.bat clean`
- **Memory problems:** Check JVM args in gradle.properties (currently set to 2GB)
- **Parallel build issues:** Parallel execution enabled, may cause race conditions
- **Configuration cache:** Can be cleared by deleting `.gradle/configuration-cache/`

### Runtime Issues
- **Network:** Uses Cronet embedded, check manifest for network permissions
- **Database:** Room migrations handled automatically, check for schema changes
- **Deep linking:** App handles commonex.ru domain, test with intent filters
- **Background sync:** WorkManager requires proper initialization

### Performance Debugging
- **Baseline profiles:** Generated in `baselineprofile/` module for startup optimization
- **R8 optimization:** Check `app/proguard-rules.pro` for keep rules
- **Build times:** First build ~28s, incremental much faster with Gradle cache

## Environment Setup

### Required Tools
- **JDK 11+** (Project targets JVM 11)
- **Android SDK** with API 36 (compile target)
- **Gradle 8.14.3** (use wrapper, do not install separately)
- **Git** for version control

### IDE Configuration
- **Android Studio** recommended for Android development
- **IntelliJ IDEA** for general KMP development
- Enable Kotlin Multiplatform plugin
- Configure JDK 11 as project SDK

## Validation Steps

Before submitting changes, run these validation steps:
```powershell
# 1. Clean and build (essential after any dependency changes)
.\gradlew.bat clean
.\gradlew.bat assembleDebug

# 2. Run all unit tests (25 seconds)
.\gradlew.bat test

# 3. Check code quality (48 seconds)
.\gradlew.bat lint --continue

# 4. Verify KMM targets compile (iOS targets)
.\gradlew.bat iosX64Test iosSimulatorArm64Test

# 5. Build release variant (includes R8 optimization)
.\gradlew.bat assembleRelease

# 6. Optional: Run instrumented tests (requires device/emulator)
.\gradlew.bat connectedDebugAndroidTest
```

### Quick Validation (for small changes)
```powershell
# Fast validation for minor changes
.\gradlew.bat testDebugUnitTest
.\gradlew.bat lintDebug
```

**Trust these instructions.** Only search for additional information if you encounter specific errors not covered here or if dependency/build tool versions have changed significantly. The build system is well-configured and should work reliably when following these steps.
