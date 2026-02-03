---
applyTo: "android/**"
---

# Agent Instructions for Expenses (CommonEx) Android Project

## Project Overview

This is a **Kotlin Multiplatform Mobile (KMM)** expenses management application that targets both Android and iOS platforms. The project uses **Jetpack Compose** for UI, **Ktor** for networking, **Room** for local database storage, and follows a modular architecture with feature-based organization.

**Key Technologies:**

- Kotlin 2.3.0 with Compose compiler plugin
- Jetpack Compose with Material3 design system
- Ktor client for networking with Cronet backend (Android) and Darwin backend (iOS)
- Room database with KSP for code generation
- Kotlin Coroutines and Flow for asynchronous programming
- Dependency Injection via custom locator pattern
- Navigation using Android Navigation 3 library
- Multiplatform-resources for resource management
- WorkManager for background sync tasks
- Protocol Buffers (Wire) for settings serialization
- Gradle 9.2.1 with Kotlin DSL and version catalogs
- Android Gradle Plugin 9.0.0-rc03
- Target: Android API 36, Min API 26

**Project Size:** ~50 modules across shared core libraries, feature modules, and platform-specific implementations.

**App Features:**

- Event creation and deletion
- Person management within events (create initially / add to existing events)
- Joining events via invite links
- Expense recording with currency conversion
- Debt calculation and split management
- Sync functionality with background workers
- Deep linking support (commonex.ru domain)

## Standard Operating Procedures and Skills

- Release workflow: use the `prepare-android-release` skill at `android/.agents/skills/prepare-android-release` for version bump, baseline profiles, and tagging.

## Build Instructions

### Prerequisites

- JDK 22+ (Project uses JVM target 17, daemon configured for JDK 22/Temurin)
- Android SDK with API 36
- Gradle 9.2.1 (use wrapper)

### Essential Commands

**Always use `.\gradlew` (Windows) for all operations. On MacOS use `./gradlew`.**

#### Clean and Build

```powershell
# Clean project (7 seconds)
.\gradlew clean

# Build debug APK (28 seconds from clean, 5 seconds incremental)
.\gradlew assembleDebug

# Build release APK (longer, includes obfuscation)
.\gradlew assembleRelease
```

#### Testing

```powershell
# Run all unit tests (25 seconds)
.\gradlew test

# Run all tests across all targets with aggregated report (includes KMM)
.\gradlew allTests

# Run KMM host tests (~15 seconds)
.\gradlew testHostTest

# Run instrumented tests (requires device/emulator)
.\gradlew :app:connectedAutotestAndroidTest

# Run device tests (requires device/emulator) (includes Room tests)
.\gradlew connectedAndroidDeviceTest

# Run instrumented tests with Gradle Managed Devices
./gradlew :app:pixel6Api35AtdAutotestAndroidTest -Dcom.android.tools.r8.disableApiModeling

# Run device tests with Gradle Managed Devices (includes Room tests)
./gradlew :app:pixel6Api35AtdAndroidDeviceTest

# Run instrumented tests with Marathon (requires marathon CLI installed)
# First build APKs, then run marathon from the android/ directory
.\gradlew :app:assembleAutotest :app:assembleAutotestAndroidTest -Dcom.android.tools.r8.disableApiModeling
marathon
```

#### Marathon Test Runner

[Marathon](https://docs.marathonlabs.io/) is a cross-platform test runner optimized for stability and performance. It provides:
- Intelligent test sharding and batching
- Automatic retry of flaky tests
- Parallel execution across multiple devices
- Detailed HTML reports with screenshots/video

**Installation (Windows):**
```powershell
# Download latest release from https://github.com/MarathonLabs/marathon/releases
# Extract and add to PATH, e.g.:
$env:Path += ";C:\tools\marathon-0.10.4\bin"
```

**Running tests with Marathon:**
```powershell
# Ensure emulator/device is running and visible via `adb devices`
# Build APKs first
.\gradlew :app:assembleAutotest :app:assembleAutotestAndroidTest -Dcom.android.tools.r8.disableApiModeling

# Run from android/ directory (where Marathonfile is located)
marathon

# Reports are generated in build/reports/marathon/
```

**Marathonfile configuration:** Located at `android/Marathonfile`. Key settings:
- `applicationApk` / `testApplicationApk`: Paths to APKs
- `autoGrantPermission`: Auto-grant runtime permissions
- `testParserConfiguration`: Use `type: "local"` for JUnit 4 bytecode analysis
- `retryStrategy`: Configure retry behavior for flaky tests
- `batchingStrategy`: Group tests for efficient execution

**Critical: JUnit 4 requirement for Marathon CLI:**
Marathon's local test parser uses bytecode analysis to discover tests. It only recognizes `org.junit.Test` (JUnit 4) annotations, **not** `org.junit.jupiter.api.Test` (JUnit 5). This is why instrumented tests use JUnit 4 while unit tests can use JUnit 5.

If you see `NoTestCasesFoundException` with Marathon, verify:
1. Test class uses `@RunWith(AndroidJUnit4::class)`
2. Test methods use `org.junit.Test` (not `org.junit.jupiter.api.Test`)
3. Marathonfile has `testParserConfiguration: type: "local"`

See [Marathon documentation](https://docs.marathonlabs.io/runner/android/configure) for all configuration options.

#### Code Quality

```powershell
# Run lint analysis (40 seconds)
.\gradlew lint --continue

# Run lint on specific build variants
.\gradlew lintDebug
.\gradlew lintRelease
.\gradlew lintBenchmarkRelease

# Apply lint auto-fixes
.\gradlew lintFix

# Update lint baseline (if using lint baseline files)
.\gradlew updateLintBaseline

# Generate lint reports at: app/build/reports/lint-results-debug.html
```

#### Dependency Management

```powershell
# Check for dependency updates (5+ minutes)
.\gradlew dependencyUpdates --refresh-dependencies -Drevision=release

# Report location: build/dependencyUpdates/report.txt
```

### Build Warnings and Issues

**Expected Warnings (safe to ignore):**

- `WARNING: The option setting 'android.r8.optimizedResourceShrinking=true' is experimental`
- `Calculating task graph as configuration cache cannot be reused because file 'gradle\buildSrc.versions.toml' has changed`
- Cronet namespace warnings in manifest merger
- Redundant visibility modifier warnings from generated Room code
- Native library stripping warnings for specific .so files
- `OpenJDK 64-Bit Server VM warning: Sharing is only supported for boot loader classes` during unit tests
- `Parallel Configuration Cache is an incubating feature` warnings

**Build Process Notes:**

- Configuration cache is enabled and may show "incubating feature" warnings
- First build after clean takes ~28 seconds
- Incremental builds are much faster due to Gradle caching
- KSP generates code for Room DAOs and may show redundant modifier warnings
- Dependency updates command may take 5+ minutes and should not be interrupted
- PowerShell users: Use `;` instead of `&&` for command chaining

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
  │   ├── events/             # Event management (create, join, add participants to existing event or during event creation)
  │   │   └── ui/
  │   │       ├── add_persons/        # Add participants during event creation
  │   │       ├── add_participants/   # Add participants to existing event
  │   │       ├── choose_person/      # Choose current person (participant)
  │   │       ├── create/             # Create new event
  │   │       ├── join/               # Join existing event
  │   │       └── ...
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
- **ProGuard:** `app/proguard-rules-autotest.pro` (rules for android tests)
- **ProGuard:** `app/proguard-test-rules.pro` (rules for android tests)
- **Marathonfile:** `android/Marathonfile` (Marathon CLI configuration)

### Version Catalog Structure

- **shared.versions.toml:** Main dependencies (Kotlin, Compose, Room, Ktor, etc.)
- **buildSrc.versions.toml:** Build plugins (AGP, Kotlin compiler)
- Centralized version management prevents conflicts across 50+ modules

## Development Guidelines

### Making Changes

1. **Always run tests after changes:** `.\gradlew test`
2. **Check lint issues:** `.\gradlew lint --continue`
3. **For KMM modules:** Changes in `shared/` affect both Android and iOS
4. **Generated code:** Room DAOs are auto-generated by KSP, don't edit manually

### Code Generation

- **Room database:** Uses KSP for DAO generation (expect redundant visibility warnings)
- **Wire protocol buffers:** Used for settings serialization
- **Compose compiler:** Enabled for all modules with Compose UI

### Common Issues and Solutions

- **Build fails after dependency changes:** Run `.\gradlew clean` first
- **KSP errors:** Usually resolved by clean build
- **Version conflicts:** Check `gradle/shared.versions.toml` for centralized versions
- **iOS build issues:** Ensure Xcode is properly configured for the `iosApp` module
- **Marathon finds no tests:** Verify tests use JUnit 4 annotations (`@RunWith(AndroidJUnit4::class)`, `org.junit.Test`)

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

### ViewModel Patterns

#### Job Guard Pattern
Prevent duplicate concurrent operations using nullable `Job?` with null check:
```kotlin
private var shareJob: Job? = null

fun onShareClicked() {
    if (shareJob != null) return  // Guard against concurrent execution

    shareJob = viewModelScope.launch {
        // ... async work
    }.apply {
        invokeOnCompletion { shareJob = null }  // Clear on completion (success or failure)
    }
}
```
Use `invokeOnCompletion` for cleanup instead of try-finally to handle both success and cancellation.

#### StateFlow.update for Atomic Updates
Use `update {}` instead of direct assignment for thread-safe state transitions:
```kotlin
state.update { currentState ->
    currentState.copy(joining = EventJoiningState.Error(errorMessage))
}
```
Use `updateAndGet` when you need both the operation and the updated value:
```kotlin
val state = state.updateAndGet { it.copy(joining = EventJoiningState.Joining) }
```

#### Property with Backing Field
Expose immutable `StateFlow` while using mutable `MutableStateFlow` internally:
```kotlin
val state: StateFlow<UiModel>
    field = MutableStateFlow(initialState)
```

#### State Change Detection in Combine
Track previous values to detect changes and trigger side effects:
```kotlin
val state = run {
    var lastServerId: String? = null
    
    combine(
        eventFlow.onEach { event ->
            val serverId = event?.serverId
            if (lastServerId != serverId) {
                lastServerId = serverId
                shareState.value = ShareState.Idle(serverId)  // Reset on change
            }
        },
        shareState
    ) { event, shareState -> ... }
}
```

### Compose UI Patterns

#### State-Driven Side Effects (Clipboard, Share)
Trigger platform operations via state changes rather than direct callbacks:
```kotlin
// ViewModel: Set state to trigger UI-side operation
shareState.value = ShareState.PendingClipboardCopy(shareText)

// Composable: React to state and perform operation
(state.shareState as? ShareState.PendingClipboardCopy)?.let { pendingCopy ->
    LaunchedEffect(pendingCopy.shareText) {
        clipboard.setClipEntry(clipEntryOf(state.eventName, pendingCopy.shareText))
        onTextCopied()  // Callback to transition state
    }
}

// ViewModel: Transition to next state after operation
fun onTextCopied() {
    shareState.update { if (it is PendingClipboardCopy) Ready(it.shareText) else it }
}
```

#### Accessibility-Safe Loading Swap
When swapping between loading indicator and content, and it might cause UI jumping, preserve layout size and hide from screen readers:
```kotlin
Box(contentAlignment = Alignment.Center) {
    // Always render content to preserve intrinsic size for any font scale
    Text(
        modifier = Modifier
            .alpha(if (isLoading) 0f else 1f)
            .then(if (isLoading) Modifier.clearAndSetSemantics {} else Modifier)
            .clickable(enabled = !isLoading, onClick = onClick),
        text = stringResource(Res.string.action)
    )
    if (isLoading) {
        LoadingIndicator(modifier = Modifier.size(24.dp))
    }
}
```
- `alpha(0f)` hides visually but preserves layout
- `clearAndSetSemantics {}` removes from accessibility tree when invisible
- Never use fixed `dp` sizes for dynamic text content (breaks with font scale settings)

#### Derived Values Without Remember
Simple derived values from state parameters don't need `remember`:
```kotlin
val isLoading = state.shareState is ShareState.Loading  // Correct: recalculates on recomposition
```
Use `remember` only for expensive computations or mutable state that must survive recomposition.

### UI State Modeling

#### Sealed Interface with Computed Properties
Define polymorphic behavior via extension properties in companion:
```kotlin
sealed interface ShareState {
    data class Idle(val serverId: String?, val pinCode: String) : ShareState
    data object Loading : ShareState
    data class Ready(val shareText: String) : ShareState
    data class PendingClipboardCopy(val shareText: String) : ShareState

    companion object {
        val ShareState.canShare: Boolean // Use computed property if it's clearly derived from state
            get() = when (this) {
                is Idle -> serverId != null
                Loading -> false
                is PendingClipboardCopy, is Ready -> true
            }
    }
}
```

#### States Carrying Context Data
Different states carry different data relevant to that state:
- `Idle(serverId, pinCode)` - data needed to initiate action
- `Ready(shareText)` - result of completed action
- `PendingClipboardCopy(shareText)` - transient state triggering UI operation

### Combining Data States with UI States

Complex screens often need to combine data from repositories/use cases with transient UI states (refresh indicators, recently deleted items, etc.). Use layered combination to avoid recalculating expensive transformations.

#### Layered State Combination Pattern
Separate data flows from UI state flows and combine them at different layers:
```kotlin
// Layer 1: UI state flows (cheap, frequently changing)
private val recentlyRemovedEventName = MutableStateFlow<String?>(null)
private val isRefreshingFlow = currentEventFlow
    .flatMapLatestNoBuffer { event ->
        if (event == null) flowOf(false)
        else pullToRefreshStateManager.isEventRefreshing(event.id)
    }
    .shareInWhileSubscribed(scope = viewModelScope, replay = 1)

// Layer 2: Data flows combined with some UI state
private val localEventsState = flow {
    combine(
        getEventsUseCase.getEvents(),           // Data
        eventDeletionStateManager.state,        // Data
        recentlyRemovedEventName,               // UI state (cheap)
    ) { events, deletionState, removedName ->
        // Transform to UI model
    }.let { emitAll(it) }
}

// Layer 3: Final state - combine expensive data flow with cheap UI state last
val state: StateFlow<ScreenState> = combine(
    expensesDetailsFlow,
    settingsRepository.getCurrentPersonId(),
) { details, personId ->
    // Expensive transformation here
    details to personId
}.flatMapLatestNoBuffer { (details, personId) ->
    // Build UI model from data
    flowOf(buildUiModel(details, personId))
}
    .combine(isRefreshingFlow) { state, isRefreshing ->
        // Late combination: only update isRefreshing field
        if (state is Success && state.data.isRefreshing != isRefreshing) {
            state.copy(data = state.data.copy(isRefreshing = isRefreshing))
        } else {
            state
        }
    }
    .stateInWhileSubscribed(scope = viewModelScope, initialValue = Loading)
```

Key principles:
- **Separate cheap UI state from expensive data** - `isRefreshingFlow` changes frequently but shouldn't trigger data recalculation
- **Combine UI state last** - After expensive transformations, so changes to UI state only update the final field
- **Conditional updates** - Check if value actually changed before copying to avoid unnecessary emissions
- **Use shareInWhileSubscribed for shared flows** - When the same flow is used in multiple combines

#### Transient UI State with Auto-Clear
For temporary UI states like "recently deleted" notifications:
```kotlin
private val recentlyRemovedEventName = MutableStateFlow<String?>(null)
private var recentlyRemovedEventJob: Job? = null

private fun handleEventRemoval(removedEvent: Event) {
    recentlyRemovedEventName.value = null
    recentlyRemovedEventJob?.cancel()
    
    recentlyRemovedEventJob = viewModelScope.launch {
        recentlyRemovedEventName.value = removedEvent.name
        delay(3000) // Show for 3 seconds
        recentlyRemovedEventName.value = null
    }
}
```

### Form Input Patterns

#### Multiple Input Flows Combined
For forms with many inputs, use separate `MutableStateFlow` for each input and combine them:
```kotlin
private val selectedCurrencyCode = MutableStateFlow<String?>(null)
private val selectedPersonId = MutableStateFlow<Long?>(null)
private val inputDescription = MutableStateFlow("")
private val inputAmount = MutableStateFlow(AmountModel(null, ""))
private val inputEqualSplit = MutableStateFlow(true)

private val _state = combine(
    dataFlow,
    selectedCurrencyCode,
    selectedPersonId,
    inputDescription,
    inputAmount,
    inputEqualSplit,
) { data, currency, person, description, amount, equalSplit ->
    // Build screen model from all inputs
}
```

#### Input with Fallback to Settings
Use `flatMapLatestNoBuffer` to provide default from settings when user hasn't selected:
```kotlin
private val selectedPersonId = MutableStateFlow<Long?>(null)

// In combine:
selectedPersonId.flatMapLatestNoBuffer { selected ->
    selected?.let { flowOf(it) } ?: settingsRepository.getCurrentPersonId()
}
```

#### Lazy Set Initialization
Initialize sets/collections from current state only on first interaction:
```kotlin
private val selectedIds = MutableStateFlow<Set<Long>?>(null) // null = all selected

fun onItemClicked(id: Long) {
    selectedIds.update { current ->
        val ids = if (current == null) {
            // Initialize from state on first click
            (_state.value as? Success)?.data?.items
                ?.mapTo(HashSet()) { it.id }
                ?: return@update current
        } else {
            current
        }
        
        if (ids.contains(id)) ids - id else ids + id
    }
}
```

#### Raw + Parsed Input Model
Store both raw string and parsed value for inputs requiring validation:
```kotlin
data class AmountModel(
    val amount: BigDecimal?,  // Parsed value for calculations (null if invalid)
    val amountRaw: String,    // Raw string to preserve user input in UI
)

fun onAmountChanged(input: String) {
    val trimmed = input.trim()
    inputAmount.value = AmountModel(
        amount = trimmed.toBigDecimalOrNull(),
        amountRaw = trimmed
    )
}
```
This allows validation (`amount != null`) while preserving exactly what user typed.

#### Private Domain Model + Public UI Model
Separate internal domain model from UI model for complex screens:
```kotlin
// Internal model with domain objects
private val _state: StateFlow<SimpleScreenState<AddExpenseScreenModel>> = combine(...) { ... }

// Public UI model for composables (no domain objects)
val state: StateFlow<SimpleScreenState<AddExpensePaneUiModel>> = _state
    .map { state ->
        when (state) {
            is SimpleScreenState.Success -> SimpleScreenState.Success(state.data.toUiModel())
            SimpleScreenState.Loading -> SimpleScreenState.Loading
            SimpleScreenState.Error -> SimpleScreenState.Error
            SimpleScreenState.Empty -> SimpleScreenState.Empty
        }
    }
    .stateInWhileSubscribed(viewModelScope, initialValue = SimpleScreenState.Loading)
```
Benefits:
- Internal model can reference domain entities (Person, Currency)
- UI model uses only primitive types and IDs
- `toUiModel()` transformation is explicit and testable

#### Pre-filled Forms from Navigation
Initialize input flows from navigation arguments:
```kotlin
class AddExpenseViewModel(
    private val replenishment: Replenishment?,  // Navigation argument
) {
    private val selectedExpenseType = MutableStateFlow(
        replenishment?.let { ExpenseType.Replenishment } ?: ExpenseType.Spending
    )
    private val inputAmount = MutableStateFlow(
        if (replenishment == null) {
            AmountModel(null, "")
        } else {
            AmountModel(replenishment.amount.toBigDecimalOrNull(), replenishment.amount)
        }
    )
}
```

### Performance Considerations

- **Baseline profiles:** Module at `baselineprofile/` for Android startup optimization
- **R8 optimization:** Enabled for release builds with custom ProGuard rules
- **Cronet networking:** Using Chrome's network stack for better performance

### Testing Strategy

- **Unit tests:** JUnit 6 for host/JVM tests
- **Instrumented tests (non-UI):** Android Tests with JUnit 6
- **Instrumented tests (Compose UI tests):** Android Tests with JUnit 4 and MArathon. `ComposeTestRule` with context receivers pattern.
- **Room tests:** androidx.room:room-testing (example `MigrationTest.kt` in `androidDeviceTest` source set)
- **Device testing:** Managed devices configured in `pixel6Api35*` tasks
- **Marathon runner:** Cross-platform test runner for CI with retries and sharding

### Instrumented Test Architecture

The instrumented tests use a **Page Object / Screen Object pattern** with Kotlin context receivers:

```
app/src/androidTest/kotlin/ru/commonex/
├── BasicInstrumentedTest.kt      # Main test class with @RunWith(AndroidJUnit4::class)
├── ConnectivityRule.kt           # JUnit 4 Rule for network control (@Offline annotation)
├── ConnectivityManager.kt        # Shell commands for wifi/data control
├── testUtils.kt                  # runTest utility for reducing boilerplate
└── screens/                      # Screen objects using context receivers
    ├── BaseScreen.kt             # Base class with common wait/assert helpers
    ├── ExpensesScreen.kt
    ├── LocalEventsScreen.kt
    └── ...
```

**Key patterns:**

1. **Context receivers for ComposeTestRule:** Screen methods use `context(rule: ComposeTestRule)` to access the test rule without explicit parameter passing:
   ```kotlin
   context(rule: ComposeTestRule)
   suspend fun clickCreateEvent(): CreateEventScreen {
       rule.onNodeWithText(label).performClick()
       return CreateEventScreen()
   }
   ```

2. **Test structure with RuleChain:** Tests use `RuleChain` to order rules correctly:
   ```kotlin
   private val composeRule = createAndroidComposeRule<MainActivity>()
   private val connectivityRule = ConnectivityRule()

   @get:Rule
   val ruleChain: RuleChain = RuleChain
       .outerRule(connectivityRule)
       .around(composeRule)
   ```

3. **Utility for test execution:** Tests use `runTest` extension from `testUtils.kt` to reduce boilerplate:
   ```kotlin
   @Test
   fun testSomeFlow() = composeRule.runTest {
       LocalEventsScreen()
           .clickCreateEvent()
           .enterEventName("Test")
           // ...
   }
   ```
   This utility wraps the test in `runBlocking` and provides `ComposeTestRule` as a context receiver.
   
   **Note:** `TestScope`/`StandardTestDispatcher` cannot be used with Compose because UI operations must run on the main thread. For instrumented tests, `runBlocking` is appropriate since the device/emulator runs in real-time anyway.

4. **@Offline annotation:** Custom annotation + `ConnectivityRule` for tests requiring network control:
   ```kotlin
   @Offline
   @Test
   fun testOfflineFlow() = runBlocking { ... }
   ```

**Important:** Instrumented tests must use **JUnit 4** (not JUnit 5) for Marathon CLI compatibility. Marathon's local test parser only recognizes `org.junit.Test` annotations, not `org.junit.jupiter.api.Test`.

## Common Development Tasks

### Adding a New Feature Module

1. Create module under `shared/feature/`
2. Apply `shared-kmm-library-plugin` in build.gradle.kts
3. Add to `settings.gradle.kts` includes
4. Follow naming pattern: `com.inwords.expenses.feature.{feature-name}`

### Adding a New Entity (within a feature module)
1. Create entity data models in the feature module's data layer (e.g., `shared/feature/{feature-name}/src/commonMain/kotlin/.../data/db/entities/`)
2. Add Room entity annotations if database persistence is needed
3. Create repository interfaces in the domain/data layer
4. Implement repositories with Room DAOs (DAOs are auto-generated by KSP)
5. Update database schema in `shared/integration/databases` if needed
6. Add data models and DTOs for network communication if the entity is synced with backend

### Working with Database

- Entities are defined in feature modules (e.g., `shared/feature/events/src/commonMain/kotlin/.../data/db/entities/`)
- DAOs are generated by Room with KSP, do not edit generated files
- Database setup is in `shared/integration/databases`
- **Database migrations** are in `shared/integration/databases/src/commonMain/kotlin/.../data/migrations/`
- **Initial data seeding** is in `RoomOnCreateCallback` for new installs

#### Database Migrations

When adding new data or schema changes for existing users:

1. **Create migration constant** in `shared/integration/databases/src/commonMain/kotlin/com/inwords/expenses/integration/databases/data/migrations/`
   - Name pattern: `Migration{N}To{N+1}.kt` (e.g., `Migration1To2.kt`)
   - Use `internal val MIGRATION_{N}_{N+1} = object : Migration(startVersion, endVersion) { ... }` pattern
   - Override `migrate(connection: SQLiteConnection)` method
   - Use `connection.execSQL()` for SQL operations
   - **Make migrations idempotent**: Check for existence before inserting data to avoid duplicates

2. **Update database version** in `AppDatabase.kt`:
   - Increment `version` in `@Database` annotation
   - Import the migration constant: `import com.inwords.expenses.integration.databases.data.migrations.MIGRATION_1_2`
   - Register migration: `.addMigrations(MIGRATION_1_2)` in `createAppDatabase()`

3. **Example migration pattern** (for adding currency):
   ```kotlin
   internal val MIGRATION_1_2 = object : Migration(1, 2) {
       override fun migrate(connection: SQLiteConnection) {
           connection.execSQL(
               """
               INSERT INTO currency (currency_server_id, code, name) 
               SELECT NULL, 'AED', 'UAE Dirham'
               WHERE NOT EXISTS (SELECT 1 FROM currency WHERE code = 'AED')
               """.trimIndent()
           )
       }
   }
   ```

4. **For new installs**: Add data to `RoomOnCreateCallback.onCreate()` so new users get it immediately

**Migration tests (Android instrumented)**: MigrationTestHelper.createDatabase() bypasses Room callbacks, so seed base data manually (e.g., currencies) or invoke RoomOnCreateCallback.onCreate() with the SQLite connection before running migrations.

### Adding Dependencies

- Update version catalogs in `gradle/shared.versions.toml` or `gradle/buildSrc.versions.toml`
- Use catalog references in build files: `implementation(shared.some.library)`
- Maintain KMP compatibility for shared modules

### UI Development

- Follow Material 3 Expressive guidelines and design system from `shared:core:ui-design`
- Compose Multiplatform for shared UI components
- Platform-specific implementations in `androidMain`/`iosMain` source sets
- For navigation, use Navigation 3 library with helpers from `shared:core:navigation`

### Event Sharing

Events are shared via secure token-based links that expire in 14 days:

- **Primary flow**: Generate share token via `CreateShareTokenUseCase` when user clicks "Share" or "Copy"
- **Fallback flow**: If token generation fails (offline/network error), use PIN-based link with warning message
- **Share messages**: Localized with formatted expiration dates
- **Deeplinks**: Support both `?token=` (new secure method) and `?pinCode=` query parameters
- **Join flow**: Deeplinks with either `token` or `pinCode` auto-trigger join without requiring manual PIN entry
- **Error handling**: Token expiration and invalid token errors are displayed inline in JoinEventPane
- **Location**: `shared/feature/menu/` (share UI), `shared/feature/events/domain/CreateShareTokenUseCase.kt` (use case)
- **Key files**:
   - `MenuViewModel.kt` - Share/copy logic with token generation and fallback
   - `MenuDialog.kt` - UI with share button, loading indicator and clipboard copy button
   - `CreateShareTokenUseCase.kt` - Domain use case for token generation
   - `JoinEventViewModel.kt` - Handles token- and pinCode-based deeplink joining

#### Deeplink Instrumented Tests (Android)

- **Location**: `android/app/src/androidTest/kotlin/ru/commonex/BasicInstrumentedTest.kt`
- **Framework**: JUnit 4 (required for Marathon compatibility)
- **Covers**: share link generation, clipboard extraction, local event removal, and deeplink auto-join for both `token` and `pinCode`.
- **Prereqs**:
  - Device/emulator must allow clipboard access in tests.
  - Network toggling uses `svc wifi/data` via instrumentation (see `ConnectivityManager`); avoid running on devices where these shell commands are blocked.
- **Implementation notes**:
  - Tests use `RuleChain` with `ConnectivityRule` (outer) and `createAndroidComposeRule` (inner).
  - Screen objects use Kotlin context receivers: `context(rule: ComposeTestRule)`.
  - Wait for Copy to become enabled before reading clipboard (`MenuDialogScreen.waitUntilCopyEnabled()`).
  - Deeplink tests expect `https://commonex.ru/event/{id}?token=...` or `?pinCode=...` extracted from clipboard text.
  - Use `composeRule.activityRule.scenario.onActivity { it.onNewIntent(intent) }` to feed deeplinks.

#### Combining Instrumented Tests

- Prefer **scenario-composed tests** when they reduce setup cost and cover realistic flows (e.g., create → share token → remove local copy → join via deeplink).
- Combine steps **only if** the flow is coherent and failure localization remains clear; keep each test’s intent obvious from its name and comments.
- When combining, keep assertions at each critical transition (creation, share link, deletion, join) so failures are easy to pinpoint.
- Don’t hesitate to modify existing tests when they’re a better fit for new scenario checks; keep changes minimal and maintain clarity.

### Adding Participants to Existing Events

The "Add participants" feature allows users to add new participants to an existing event:

- **Entry point**: Menu dialog → "Add participants" option
- **UI**: Full-screen pane (`AddParticipantsToEventPane`) similar to the one used in event creation flow
- **Domain**: `AddParticipantsToCurrentEventUseCase` handles adding participants locally
- **Sync**: New participants are stored locally immediately (offline-first) with `serverId = null`
- **Background sync**: `EventPersonsPushTask` automatically syncs new participants to server
- **Location**: `shared/feature/events/src/commonMain/kotlin/.../ui/add_participants/`
- **Key files**:
  - `AddParticipantsToEventPane.kt` - UI composable
  - `AddParticipantsToEventViewModel.kt` - ViewModel with state management
  - `AddParticipantsToCurrentEventUseCase.kt` - Domain use case
  - `AddParticipantsToEventPaneDestination.kt` - Navigation destination

The confirm button is disabled when there are no participants or all participant names are empty (checked via `isConfirmEnabled` computed property).

### Adding a New Currency

When adding support for a new currency (e.g., AED), update all the following:

1. **Exchange rates**: `shared/feature/expenses/src/commonMain/kotlin/.../domain/CurrencyExchanger.kt`
   - Add currency code and USD exchange rate to the rates map

2. **New installs**: `shared/integration/databases/src/commonMain/kotlin/.../data/RoomOnCreateCallback.kt`
   - Add `INSERT INTO currency` statement in `onCreate()` method
   - Use sequential ID (next available number)

3. **Existing users**: Create migration in `shared/integration/databases/src/commonMain/kotlin/.../data/migrations/`
   - Create `Migration{N}To{N+1}.kt` file with `internal val MIGRATION_{N}_{N+1}` constant
   - Use idempotent INSERT with `WHERE NOT EXISTS` check
   - Increment database version in `AppDatabase.kt`
   - Import and register migration: `.addMigrations(MIGRATION_1_2)` in `createAppDatabase()`

4. **UI preview/mock data**: Update preview functions in UI components
   - `CreateEventPane.kt` - `CreateEventPanePreview()` function
   - `AddExpensePane.kt` - `mockAddExpenseScreenUiModel()` function
   - Any other UI components with currency lists in previews

**Note**: The migration ensures existing users get the currency on app update, while `RoomOnCreateCallback` ensures new installs have it from the start.

### Known TODOs and Technical Debt

- Several "TODO mvp" comments indicate MVP-level implementations that need improvement
- Some String vs Double type inconsistencies in network DTOs
- User agent configuration needs finalization in HTTP client

## Debugging and Troubleshooting

### Build Issues

- **Clean builds solve most KSP issues:** `.\gradlew clean`
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

- **JDK 22 (Temurin)** (Project targets JVM 17)
- **Android SDK** with API 36 (compile target)
- **Gradle 9.2.1** (use wrapper, do not install separately)
- **Git** for version control

### IDE Configuration

- **Android Studio** recommended for Android and KMM development
- Enable Kotlin Multiplatform plugin
- Configure JAVA_HOME to point to JDK 22

## Validation Steps

Before submitting changes, run these validation steps:

```powershell
# 1. Build
.\gradlew assembleDebug

# 2. Run all unit tests (25 seconds)
.\gradlew test

# 3. Run KMM host tests (~10 seconds)
.\gradlew testHostTest

# 4. Check code quality (30-48 seconds)
.\gradlew lint --continue

# 5. Verify KMM targets compile (iOS targets)
.\gradlew iosX64Test iosSimulatorArm64Test

# 6. Build release variant (includes R8 optimization)
.\gradlew assembleRelease

# 7. Build autotest variant (release-like, includes R8 optimization, but no shrinking and no obfuscation)
.\gradlew assembleAutotest

# 8. Optional: Run instrumented tests (requires device/emulator)
.\gradlew :app:connectedAutotestAndroidTest -Dcom.android.tools.r8.disableApiModeling

# 9. Optional: Run managed device tests (local Gradle Managed Devices testing)
.\gradlew :app:pixel6Api35AtdAutotestAndroidTest -Dcom.android.tools.r8.disableApiModeling

# 10. Optional: Run instrumented tests with Marathon (requires device/emulator + marathon CLI)
.\gradlew :app:assembleAutotest :app:assembleAutotestAndroidTest -Dcom.android.tools.r8.disableApiModeling
marathon
```

### Quick Validation (for small changes)

```powershell
# Fast validation for minor changes (~15 seconds total)
.\gradlew testHostTest
.\gradlew lintDebug
```

### Comprehensive Testing (before major releases)

```powershell
# Full test suite with aggregated reporting
.\gradlew allTests
.\gradlew allDevicesCheck
.\gradlew lint --continue
```

**Trust these instructions.** Only search for additional information if you encounter specific errors not covered here or if dependency/build tool versions have changed significantly. The build system is well-configured and should work reliably when following these steps.
