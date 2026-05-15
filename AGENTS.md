# AGENTS.md

## Project

Single-module Android app using Kotlin 2.0.21 + Jetpack Compose. No CI/CD, tests, or lint setup yet.

## Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Run unit tests
./gradlew test

# Run instrumented tests (requires emulator/device)
./gradlew connectedAndroidTest

# Build with specific variant
./gradlew assembleDebug --info  # verbose output
```

## Key files

- `app/build.gradle.kts` - App config, SDK versions (compile 36, min 24)
- `gradle/libs.versions.toml` - Dependency versions (Compose BOM 2024.09.00)

## Notes

- Uses Gradle Kotlin DSL, not Groovy
- Single module: `:app`
- Package: `co.martketing.rickandmortyapp`
- No code generation or custom build tasks