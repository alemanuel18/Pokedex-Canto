# Laboratorio 5 - Network access with Retrofit
## Pokédex Canto 🎮

Una Pokédex desarrollada en Android con Kotlin y Jetpack Compose, que consume la PokéAPI para mostrar información detallada de los primeros 151 Pokémon.

### 📱 Características

* ✅ Lista completa de los 151 Pokémon originales
* ✅ Detalles completos con información de cada Pokémon
* ✅ Imágenes múltiples (normal, shiny, frente, atrás)
* ✅ Estadísticas visuales con barras de progreso
* ✅ Sistema de tipos y cálculo de debilidades
* ✅ Navegación fluida entre pantallas
* ✅ UI responsiva que se adapta a rotaciones de pantalla
* ✅ Internacionalización preparada con strings.xml

### 🏗️ Arquitectura
```
┌─────────────────┐
│   UI (Compose)  │ ← Pantallas y componentes
├─────────────────┤
│   ViewModels    │ ← Lógica de presentación
├─────────────────┤
│   Repository    │ ← Abstracción de datos
├─────────────────┤
│   API Service   │ ← Conexión con PokéAPI
└─────────────────┘
```
### 🗂️ Estructura del Proyecto
```
app/src/main/java/com/example/pokedexcanto/
├── data/
│   ├── Pokemon.kt              # Modelos de datos
│   ├── PokemonApiService.kt    # Interface Retrofit
│   └── PokemonRepository.kt    # Repositorio de datos
├── ui/
│   ├── screens/
│   │   ├── PokemonListScreen.kt    # Pantalla lista
│   │   └── PokemonDetailScreen.kt  # Pantalla detalles
│   ├── viewmodels/
│   │   ├── PokemonListViewModel.kt    # VM lista
│   │   └── PokemonDetailViewModel.kt  # VM detalles
│   └── theme/                  # Tema y colores
└── MainActivity.kt             # Activity principal
```

### 🖼️ Fotos 

![Imagen de WhatsApp 2025-09-23 a las 12 33 39_598d0736](https://github.com/user-attachments/assets/e448be50-d009-4a3d-99d2-049dc1e341d8)

![Imagen de WhatsApp 2025-09-23 a las 12 33 39_3f1c1937](https://github.com/user-attachments/assets/1590eea4-96ab-4c05-827e-95ff65ccc618)

![Imagen de WhatsApp 2025-09-23 a las 12 33 39_5ef9f844](https://github.com/user-attachments/assets/4574c3b4-02ae-469b-ae46-23869b092950)
