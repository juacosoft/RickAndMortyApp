## Why

El proyecto RickAndMortyApp parte de un scaffold Android vacío. Antes de construir cualquier feature se necesita instalar todas las dependencias del stack definido, configurar los plugins requeridos (Hilt, kotlinx.serialization) y crear la estructura de paquetes de Clean Architecture. Sin esta base el resto de fases no pueden compilar ni integrarse correctamente.

## What Changes

- Agregar plugin de Hilt al classpath raíz y al módulo `:app`.
- Agregar plugin de kotlinx.serialization al módulo `:app`.
- Elevar `minSdk` de 24 a 26 según spec del proyecto.
- Agregar al `libs.versions.toml` y a `app/build.gradle.kts` las dependencias:
  - Hilt (runtime + compilador KSP)
  - Ktor Client (Android engine, contenido, serialización, logging)
  - kotlinx.serialization (JSON)
  - Navigation Compose
  - Coil (coil-compose)
  - MockK (test)
  - Turbine (test)
  - Coroutines Test (test)
- Reemplazar `annotationProcessor` / `kapt` por **KSP** para Hilt.
- Crear la estructura de paquetes bajo `co.martketing.rickandmortyapp`:
  - `data/`
  - `domain/`
  - `di/`
  - `presentation/`
  - `commons/`
- Verificar que el proyecto compila (`assembleDebug`) y pasa los unit tests (`test`) tras los cambios.

## Capabilities

### New Capabilities

- `project-setup`: Configuración completa de dependencias, plugins y estructura de capas de Clean Architecture lista para el desarrollo de features.

### Modified Capabilities

_(ninguna — el proyecto no tiene specs previas)_

## Impact

- `app/build.gradle.kts`: nuevas dependencias, plugins, cambio de minSdk.
- `gradle/libs.versions.toml`: versiones centralizadas de todas las librerías nuevas.
- `build.gradle.kts` raíz: classpath de Hilt y KSP.
- Estructura de directorios `app/src/main/java/co/martketing/rickandmortyapp/`: se crean los paquetes base.
- No hay cambios de comportamiento en runtime; la app sigue mostrando el scaffold por defecto.
