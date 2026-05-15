## 1. Version Catalog (libs.versions.toml)

- [x] 1.1 Agregar versiones: hilt `2.52`, ksp `2.0.21-1.0.28`, ktor `3.0.3`, kotlinx-serialization `1.7.3`, navigation-compose `2.8.4`, coil `3.0.4`, mockk `1.13.13`, turbine `1.2.0`, coroutines `1.9.0`
- [x] 1.2 Declarar librerías: `hilt-android`, `hilt-compiler`, `ktor-client-android`, `ktor-client-content-negotiation`, `ktor-serialization-kotlinx-json`, `ktor-client-logging`, `kotlinx-serialization-json`, `navigation-compose`, `coil-compose`, `mockk`, `turbine`, `kotlinx-coroutines-test`
- [x] 1.3 Declarar plugins: `hilt` (`com.google.dagger.hilt.android`), `ksp` (`com.google.devtools.ksp`), `kotlin-serialization` (`org.jetbrains.kotlin.plugin.serialization`)

## 2. Build Gradle Raíz (build.gradle.kts)

- [x] 2.1 Añadir `alias(libs.plugins.hilt) apply false` al bloque `plugins`
- [x] 2.2 Añadir `alias(libs.plugins.ksp) apply false` al bloque `plugins`

## 3. App Build Gradle (app/build.gradle.kts)

- [x] 3.1 Aplicar plugins: `alias(libs.plugins.hilt)`, `alias(libs.plugins.ksp)`, `alias(libs.plugins.kotlin.serialization)`
- [x] 3.2 Cambiar `minSdk` de 24 a 26
- [x] 3.3 Agregar dependencias de runtime: `hilt-android`, `ktor-client-android`, `ktor-client-content-negotiation`, `ktor-serialization-kotlinx-json`, `ktor-client-logging`, `kotlinx-serialization-json`, `navigation-compose`, `coil-compose`
- [x] 3.4 Agregar dependencia de Hilt compiler con `ksp(libs.hilt.compiler)`
- [x] 3.5 Agregar dependencias de test: `testImplementation(libs.mockk)`, `testImplementation(libs.turbine)`, `testImplementation(libs.kotlinx.coroutines.test)`

## 4. Estructura de Paquetes

- [x] 4.1 Crear directorio `app/src/main/java/co/martketing/rickandmortyapp/data/` con `.gitkeep`
- [x] 4.2 Crear directorio `app/src/main/java/co/martketing/rickandmortyapp/domain/` con `.gitkeep`
- [x] 4.3 Crear directorio `app/src/main/java/co/martketing/rickandmortyapp/di/` con `.gitkeep`
- [x] 4.4 Crear directorio `app/src/main/java/co/martketing/rickandmortyapp/presentation/` con `.gitkeep`
- [x] 4.5 Crear directorio `app/src/main/java/co/martketing/rickandmortyapp/commons/` con `.gitkeep`

## 5. Verificación de Build

- [x] 5.1 Ejecutar `./gradlew assembleDebug` → debe terminar con `BUILD SUCCESSFUL`
- [x] 5.2 Ejecutar `./gradlew test` → debe terminar con `BUILD SUCCESSFUL` y 0 tests fallidos

## 6. Pruebas Manuales

- [x] 6.1 Hacer gradle sync en Android Studio → sin errores de resolución de dependencias
- [x] 6.2 Verificar que la app instala y abre correctamente en emulador/dispositivo
- [x] 6.3 Confirmar que los paquetes `data`, `domain`, `di`, `presentation`, `commons` son visibles en la vista de proyecto de Android Studio

## 7. Commit

- [x] 7.1 Semantic commit: `build: setup dependencies and clean architecture package structure`
