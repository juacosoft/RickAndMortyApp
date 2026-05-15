## Context

El proyecto parte de un scaffold Android estándar (una sola Activity + Compose). No existe ninguna capa de arquitectura, ni dependencias de infraestructura instaladas. Este diseño cubre la configuración inicial del proyecto: plugins, dependencias y estructura de paquetes.

## Goals / Non-Goals

**Goals:**
- Definir versiones exactas de todas las dependencias del stack.
- Configurar KSP como procesador de anotaciones (reemplaza kapt) para Hilt.
- Habilitar el plugin kotlinx.serialization en el módulo `:app`.
- Subir `minSdk` a 26.
- Crear la estructura de paquetes vacía de Clean Architecture que usarán las fases siguientes.
- Garantizar que `assembleDebug` y `test` pasen sin errores tras los cambios.

**Non-Goals:**
- Implementar ninguna feature, pantalla o lógica de negocio.
- Configurar Ktor ni sus módulos de networking (eso es Fase 2).
- Añadir reglas de ProGuard o shrinking.

## Decisions

### KSP en lugar de KAPT para Hilt
KSP es el procesador de anotaciones recomendado por Google desde AGP 8.x; compila significativamente más rápido que KAPT. El plugin `com.google.devtools.ksp` se añade al classpath raíz y se aplica en `:app`.

### Versiones de librerías
| Librería | Versión | Motivo |
|---|---|---|
| Hilt | 2.52 | Última estable compatible con Kotlin 2.0.x + KSP |
| KSP | 2.0.21-1.0.28 | Debe coincidir con la versión de Kotlin (`2.0.21`) |
| Ktor | 3.0.3 | Última estable en la rama 3.x |
| kotlinx.serialization | 1.7.3 | Compatible con Kotlin 2.0.21 |
| Navigation Compose | 2.8.4 | Última estable con soporte type-safe routes |
| Coil | 3.0.4 | coil-compose; soporta Compose 1.7+ |
| MockK | 1.13.13 | Última estable |
| Turbine | 1.2.0 | Compatible con Coroutines 1.9.x |
| Coroutines | 1.9.0 | Requerido por Turbine 1.2 y Ktor 3 |

### Estructura de paquetes
```
co.martketing.rickandmortyapp/
├── data/
├── domain/
├── di/
├── presentation/
└── commons/
```
Cada carpeta contendrá un archivo `.gitkeep` para que git rastree los directorios vacíos hasta que la fase correspondiente los pueble.

### No se añade ningún módulo Gradle extra
El proyecto permanece single-module (`:app`) durante todas las fases definidas. La separación de capas se hace por paquete, no por módulo Gradle, para mantener la configuración simple dado el tamaño del proyecto.

## Risks / Trade-offs

- **Versiones de KSP acopladas a Kotlin**: Si se actualiza Kotlin hay que actualizar KSP en el mismo commit o el build falla.  
  → Mitigación: documentar la relación en `libs.versions.toml` con comentario inline de versión.

- **Coil 3.x cambia la API respecto a 2.x**: La migración no es transparente si se venía de Coil 2.  
  → No aplica: el proyecto es nuevo, se empieza directamente con Coil 3.

- **Navigation Compose type-safe (2.8+) requiere `kotlinx.serialization`**: Ya incluida en el stack; no hay conflicto.

## Migration Plan

1. Modificar `gradle/libs.versions.toml`: añadir versiones y bibliotecas.
2. Modificar `build.gradle.kts` raíz: añadir plugins KSP y Hilt al classpath.
3. Modificar `app/build.gradle.kts`: aplicar plugins, ajustar minSdk, añadir dependencias.
4. Crear directorios de paquetes con `.gitkeep`.
5. Ejecutar `./gradlew assembleDebug` → debe compilar sin errores.
6. Ejecutar `./gradlew test` → debe pasar sin errores.
7. Commit semántico: `build: setup dependencies and clean architecture package structure`.

Rollback: revertir los tres archivos gradle modificados con `git revert`.
