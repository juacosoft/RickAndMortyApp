## ADDED Requirements

### Requirement: Dependencias del stack instaladas
El proyecto SHALL tener todas las dependencias del stack tecnológico declaradas en `gradle/libs.versions.toml` y aplicadas en `app/build.gradle.kts` con las versiones exactas definidas en el design.

#### Scenario: Versiones centralizadas en version catalog
- **WHEN** se revisa `gradle/libs.versions.toml`
- **THEN** existen entradas de versión para: hilt, ksp, ktor, kotlinx-serialization, navigation-compose, coil, mockk, turbine, coroutines

#### Scenario: Dependencias de runtime presentes
- **WHEN** se revisa la sección `dependencies` de `app/build.gradle.kts`
- **THEN** están declaradas: hilt-android, ktor-client-android, ktor-client-content-negotiation, ktor-serialization-kotlinx-json, ktor-client-logging, navigation-compose, coil-compose

#### Scenario: Dependencias de test presentes
- **WHEN** se revisa la sección `testImplementation` y `androidTestImplementation` de `app/build.gradle.kts`
- **THEN** están declaradas: mockk, turbine, kotlinx-coroutines-test

### Requirement: Plugin KSP configurado
El proyecto SHALL usar KSP (no KAPT) como procesador de anotaciones para Hilt.

#### Scenario: Plugin KSP en classpath raíz
- **WHEN** se revisa `build.gradle.kts` raíz
- **THEN** el plugin `com.google.devtools.ksp` está declarado con `apply false`

#### Scenario: Plugin KSP aplicado en módulo app
- **WHEN** se revisa el bloque `plugins` de `app/build.gradle.kts`
- **THEN** se aplica `alias(libs.plugins.ksp)` y `alias(libs.plugins.hilt)`

#### Scenario: Hilt compiler declarado con ksp
- **WHEN** se revisa `app/build.gradle.kts`
- **THEN** la dependencia `hilt-compiler` está bajo `ksp(...)`, no bajo `kapt(...)`

### Requirement: Plugin kotlinx.serialization aplicado
El módulo `:app` SHALL tener el plugin `kotlin("plugin.serialization")` aplicado para habilitar el procesamiento de `@Serializable`.

#### Scenario: Plugin de serialización en app
- **WHEN** se revisa el bloque `plugins` de `app/build.gradle.kts`
- **THEN** aparece `alias(libs.plugins.kotlin.serialization)`

### Requirement: minSdk actualizado a 26
El SDK mínimo de la aplicación SHALL ser 26.

#### Scenario: minSdk correcto en build config
- **WHEN** se revisa `defaultConfig` en `app/build.gradle.kts`
- **THEN** `minSdk = 26`

### Requirement: Estructura de paquetes de Clean Architecture creada
El proyecto SHALL tener los paquetes base vacíos listos para las fases de implementación.

#### Scenario: Directorios de capas presentes
- **WHEN** se navega a `app/src/main/java/co/martketing/rickandmortyapp/`
- **THEN** existen los directorios: `data/`, `domain/`, `di/`, `presentation/`, `commons/`

### Requirement: Build exitoso tras configuración
El proyecto SHALL compilar y pasar los tests unitarios sin errores después de aplicar todos los cambios de esta fase.

#### Scenario: assembleDebug sin errores
- **WHEN** se ejecuta `./gradlew assembleDebug`
- **THEN** el proceso termina con `BUILD SUCCESSFUL`

#### Scenario: unit tests sin errores
- **WHEN** se ejecuta `./gradlew test`
- **THEN** el proceso termina con `BUILD SUCCESSFUL` y cero tests fallidos
