# 🏌️ Mini Golf Game

[![Java](https://img.shields.io/badge/Java-8%2B-orange.svg)](https://www.oracle.com/java/)
[![LibGDX](https://img.shields.io/badge/LibGDX-1.13.1-blue.svg)](https://libgdx.com/)
[![Box2D](https://img.shields.io/badge/Box2D-Physics-green.svg)](https://box2d.org/)
[![Gradle](https://img.shields.io/badge/Gradle-7.x-brightgreen.svg)](https://gradle.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Platform](https://img.shields.io/badge/Platform-Desktop-lightgrey.svg)]()

Un juego de mini golf multijugador desarrollado en Java utilizando el framework LibGDX y el motor de física Box2D. Cuenta con física realista, múltiples niveles, sistema de turnos y efectos de audio inmersivos.

<div align="center">
  <img src="proyectoGradle/assets/logoMiniGolf.png" alt="Mini Golf Logo" width="200"/>
</div>

## 📸 Capturas de Pantalla

<div align="center">
  <table>
    <tr>
      <td align="center">
        <img src="docs/images/menu_principal.png" alt="Menú Principal" width="300"/>
        <br><b>Menú Principal</b>
      </td>
      <td align="center">
        <img src="docs/images/gameplay.png" alt="Gameplay" width="300"/>
        <br><b>Jugabilidad</b>
      </td>
    </tr>
    <tr>
      <td align="center">
        <img src="docs/images/seleccion_jugadores.png" alt="Selección de Jugadores" width="300"/>
        <br><b>Selección de Colores</b>
      </td>
      <td align="center">
        <img src="docs/images/tabla_puntuaciones.png" alt="Tabla de Puntuaciones" width="300"/>
        <br><b>Tabla de Puntuaciones</b>
      </td>
    </tr>
  </table>
</div>

## 📋 Descripción

Mini Golf Game es un juego de golf en miniatura que permite a múltiples jugadores competir en diversos niveles con obstáculos únicos. El juego cuenta con física realista, efectos de sonido inmersivos y una interfaz de usuario intuitiva.

### ✨ Características Principales

- 🎮 **Multijugador**: Soporte para múltiples jugadores en turnos
- 🎯 **18 Niveles**: Diversos campos de mini golf con dificultad progresiva
- 🎨 **Colores Personalizados**: Cada jugador puede elegir el color de su pelota
- 🔊 **Efectos de Sonido**: Audio inmersivo para colisiones y eventos del juego
- ⚡ **Zonas Especiales**: Elementos de aceleración que modifican el movimiento de la pelota
- 📊 **Sistema de Puntuación**: Conteo de golpes (strokes) por jugador y nivel
- 🖼️ **Gráficos 2D**: Texturas y fondos personalizados para una experiencia visual atractiva

## 🛠️ Tecnologías Utilizadas

### Framework Principal
- **[LibGDX](https://libgdx.com/)**: Framework de desarrollo de juegos multiplataforma
- **[Box2D](https://box2d.org/)**: Motor de física 2D para simulación realista

### Lenguaje y Herramientas
- **Java**: Lenguaje de programación principal
- **Gradle**: Sistema de construcción y gestión de dependencias
- **FreeType**: Renderizado de fuentes TTF
- **OpenGL**: Renderizado gráfico acelerado por hardware

## 🎮 Cómo Jugar

### Controles
- **Click Izquierdo**: Colocar pelota en el punto de inicio
- **Flechas Izquierda/Derecha**: Ajustar dirección del golpe
- **Flechas Arriba/Abajo**: Ajustar fuerza del golpe
- **Barra Espaciadora**: Ejecutar el golpe

### Objetivo
El objetivo es llevar la pelota desde el punto de inicio hasta el hoyo en el menor número de golpes posible. Cada jugador toma turnos y el jugador con menos golpes totales al final de todos los niveles gana.

## 🚀 Inicio Rápido

### 📋 Requisitos del Sistema
- **Java Development Kit (JDK)**: 8 o superior
- **Memoria RAM**: Mínimo 512MB, recomendado 1GB
- **Espacio en Disco**: 100MB para instalación
- **Sistema Operativo**: Windows, Linux, macOS
- **OpenGL**: Soporte para OpenGL 2.0+

### 🔧 Instalación Detallada

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/tu-usuario/Mini-Golf.git
   cd Mini-Golf
   ```

2. **Navegar al directorio del proyecto**
   ```bash
   cd proyectoGradle
   ```

3. **Ejecutar el juego**
   ```bash
   # Linux/Mac
   ./gradlew lwjgl3:run
   
   # Windows
   gradlew.bat lwjgl3:run
   ```

## 📁 Estructura del Proyecto

```
proyectoGradle/
├── assets/                     # Recursos del juego
│   ├── *.png                  # Texturas e imágenes
│   ├── *.mp3                  # Efectos de sonido
│   ├── fonts/                 # Fuentes TTF
│   └── ui/                    # Elementos de interfaz
├── core/                      # Lógica principal del juego
│   └── src/main/java/com/minigolf/
│       ├── screens/           # Pantallas del juego
│       ├── models/            # Modelos de datos
│       ├── handlers/          # Manejadores de eventos y colisiones
│       └── niveles/           # Definiciones de niveles
├── lwjgl3/                    # Configuración de desktop
└── gradle/                    # Configuración de Gradle
```

## 🤝 Contribuciones

¡Las contribuciones son bienvenidas y muy apreciadas! Aquí te explicamos cómo puedes ayudar:

### 🚀 Cómo Contribuir

1. **Fork** el proyecto
2. **Crea** una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. **Commit** tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. **Push** a la rama (`git push origin feature/AmazingFeature`)
5. **Abre** un Pull Request

### 📋 Tipos de Contribuciones

#### 🐛 Reportar Bugs
- Usa el template de issue para bugs
- Incluye pasos para reproducir el problema
- Proporciona información del sistema
- Adjunta capturas de pantalla si es posible

#### ✨ Sugerir Features
- Describe claramente la funcionalidad propuesta
- Explica por qué sería útil
- Considera la compatibilidad con el código existente

#### 📝 Mejorar Documentación
- Corregir errores tipográficos
- Agregar ejemplos de código
- Traducir documentación
- Mejorar comentarios del código

#### 🔧 Contribuir Código
- Sigue las convenciones de código existentes
- Incluye tests para nuevas funcionalidades
- Actualiza la documentación si es necesario
- Asegúrate de que el código compile sin errores

### 🛠️ Tecnologías y Bibliotecas

#### Framework Principal
- **[LibGDX](https://libgdx.com/)** - *Framework multiplataforma para desarrollo de juegos*
  - Versión: 1.13.1
  - Licencia: Apache 2.0
  - Uso: Motor gráfico, audio, entrada y gestión de archivos

#### Motor de Física
- **[Box2D](https://box2d.org/)** - *Motor de física 2D de alto rendimiento*
  - Versión: Integrada con LibGDX
  - Licencia: MIT
  - Uso: Simulación física, detección de colisiones, dinámicas realistas

#### Herramientas de Desarrollo
- **[Gradle](https://gradle.org/)** - *Sistema de construcción y gestión de dependencias*
- **[FreeType](https://www.freetype.org/)** - *Renderizado de fuentes TTF/OTF*
- **[OpenGL](https://www.opengl.org/)** - *API gráfica multiplataforma*

### 🌟 Proyecto Open Source

#### Filosofía
Este proyecto abraza los principios del software libre y la colaboración abierta:
- **Transparencia**: Todo el código es público y revisable
- **Colaboración**: Las contribuciones son bienvenidas y valoradas
- **Educación**: El código sirve como recurso de aprendizaje
- **Comunidad**: Construcción de una comunidad de desarrolladores

#### Licencias Utilizadas
- **MIT License**: Para el código principal del juego
- **Apache 2.0**: Para componentes de LibGDX
- **SIL OFL**: Para fuentes tipográficas
- **Creative Commons**: Para documentación y assets gráficos