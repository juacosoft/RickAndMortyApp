# 🛸 RickAndMortyApp

Aplicación Android nativa que consume la [API pública de Rick and Morty](https://rickandmortyapi.com), desarrollada con **Clean Architecture**, **MVI**, **Jetpack Compose** y las librerías recomendadas por el ecosistema Android moderno.

-----

## 📸 Funcionalidades

- Listado paginado de personajes con **infinity scroll**
- Imagen, nombre y estado de cada personaje (**Alive / Dead / Unknown**)
- **Big loader** en la carga inicial y **bottom loader** en paginación
- Manejo de errores con **retry** (carga inicial y paginación)
- **Pull to refresh** para recargar el listado
- Placeholder e imagen de error al cargar imágenes
- Protección contra doble llamado en scroll

-----

## 🏗️ Arquitectura

El proyecto sigue los principios de **Clean Architecture** organizado en las siguientes capas:

```
app/
├── data/          # DTOs, datasources, Ktor client, implementaciones de repositorios
├── domain/        # Entidades, interfaces de repositorio, UseCases
├── presentation/  # ViewModels (MVI), Composables, Navigation, Theme
├── di/            # Módulos de inyección de dependencias (Hilt)
└── commons/       # Utilidades y extensiones compartidas
```

### Patrón MVI

Cada ViewModel implementa un contrato `MVIContract` con tres tipos:

|Tipo     |Descripción                                                          |
|---------|---------------------------------------------------------------------|
|`UiState`|Estado inmutable de la pantalla, expuesto como `StateFlow`           |
|`UiEvent`|Intenciones / acciones que la UI envía al ViewModel                  |
|`Effect` |Efectos one-shot (navegación, snackbars…) expuestos como `SharedFlow`|

-----

## 🛠️ Stack Tecnológico

|Categoría                |Librería / Tecnología              |
|-------------------------|-----------------------------------|
|Lenguaje                 |Kotlin                             |
|UI                       |Jetpack Compose                    |
|Arquitectura             |Clean Architecture + MVI           |
|Inyección de dependencias|Hilt                               |
|Networking               |Ktor Client                        |
|Serialización            |kotlinx.serialization              |
|Imágenes                 |Coil (coil-compose)                |
|Navegación               |Navigation Compose                 |
|Asincronía               |Coroutines + StateFlow + SharedFlow|
|Logging                  |Ktor Logging plugin → Logcat       |
|Testing                  |JUnit4 + MockK + Turbine           |

-----

## ⚙️ Requisitos

- Android Studio Hedgehog o superior
- JDK 17+
- `minSdk` 24 · `targetSdk` / `compileSdk` 36

-----

## 🚀 Configuración y ejecución

1. Clona el repositorio:

```bash
git clone https://github.com/tu-usuario/RickAndMortyApp.git
cd RickAndMortyApp
```

1. Abre el proyecto en **Android Studio**.
1. Sincroniza Gradle y ejecuta la app en un emulador o dispositivo físico (API 24+).

> No se requiere ninguna API key. La API de Rick and Morty es pública.

-----

## 🧪 Tests

El proyecto incluye pruebas unitarias para toda la lógica de negocio:

- **ViewModels** — estados y efectos MVI
- **UseCases** — lógica de dominio
- **Repositories** — integración data ↔ domain
- **Mappers** — conversiones DTO ↔ entidad

Para ejecutarlos:

```bash
./gradlew test
```

-----

## 🌐 API

**Base URL:** `https://rickandmortyapi.com/api`

|Endpoint        |Descripción                 |
|----------------|----------------------------|
|`GET /character`|Lista paginada de personajes|

La API expone paginación nativa mediante `info.next` / `info.prev` / `results`.

-----

## 📁 Convenciones de código

- **Clean Code** — sin comentarios de documentación en el código
- Mapeo data ↔ domain mediante funciones de extensión `.toDomain()` / `.toData()`
- Inversión de dependencias: siempre se inyectan interfaces, nunca implementaciones concretas
- Los UseCases **no** requieren interfaz; usan `@Inject constructor()` directamente
- Toda la lógica de negocio vive en ViewModels o UseCases, nunca en composables

-----

## 📌 Roadmap

- [ ] Pantalla de detalle de personaje
- [ ] Caché local con persistencia (Room)
- [ ] Visualización de imágenes en pantalla completa

-----

## 🤖 Inteligencia artificial
- Github copilot CLI
- Spec-driven Development
- [Openspec framworks](https://openspec.dev)

## 📄 Licencia


```
MIT License — libre de usar, modificar y distribuir.
```
