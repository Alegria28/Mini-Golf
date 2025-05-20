# Entendiendo tu Proyecto LibGDX

Este documento describe la estructura básica de un proyecto LibGDX generado y cómo ejecutarlo por primera vez.

## Estructura del Proyecto

Un proyecto LibGDX está organizado en varios módulos (subproyectos), gestionados por Gradle.

### 1. Directorio Raíz del Proyecto (`proyectoGradle`)

Es la carpeta principal que contiene todo tu proyecto. Aquí se encuentran los siguientes archivos y carpetas:

*   **`.editorconfig`**: Archivo de configuración que ayuda a mantener estilos de codificación consistentes (como indentación, finales de línea) entre diferentes editores e IDEs.
*   **`.gitattributes`**: Archivo de configuración de Git que define atributos para rutas específicas, como la normalización de finales de línea para asegurar consistencia entre diferentes sistemas operativos.
*   **`.gitignore`**: Archivo que especifica los archivos y carpetas que Git debe ignorar y no incluir en el control de versiones (ej. la carpeta `.gradle/`, archivos de compilación, configuraciones locales del IDE).
*   **`build.gradle`**: Archivo principal de configuración de compilación de Gradle para el proyecto raíz. Define configuraciones globales, repositorios de dependencias, versiones de plugins y puede declarar configuraciones aplicables a todos los submódulos.
*   **`gradle.properties`**: Archivo para definir propiedades globales para el proyecto Gradle, como versiones de bibliotecas (ej. `gdxVersion`, `ktxVersion`), opciones de la JVM para el demonio de Gradle, o activar/desactivar características del proyecto.
*   **`gradlew`**: Script ejecutable del Gradle Wrapper para sistemas operativos basados en Unix (Linux y macOS). Permite ejecutar tareas de Gradle sin necesidad de una instalación global de Gradle.
*   **`gradlew.bat`**: Script ejecutable del Gradle Wrapper para sistemas operativos Windows. Permite ejecutar tareas de Gradle sin necesidad de una instalación global de Gradle.
*   **`settings.gradle`**: Archivo de configuración de Gradle que define la estructura del proyecto, declarando qué carpetas son submódulos (proyectos) que Gradle debe incluir en la compilación (ej. `include 'core', 'lwjgl3'`).

*   **`assets/`**: **Propósito**: Contiene todos los recursos compartidos del juego (imágenes, sonidos, fuentes, datos de UI como `uiskin.json`, `uiskin.atlas`) que se empaquetarán con todas las versiones de tu juego.
*   **`core/`**: **Propósito**: Módulo principal que contiene la lógica del juego y el código Java independiente de la plataforma. Aquí reside la clase principal de tu juego.
*   **`gradle/`**: **Propósito**: Carpeta que contiene los archivos del "Gradle Wrapper" (`gradle-wrapper.jar` y `gradle-wrapper.properties`). Estos permiten que el proyecto se compile con una versión específica de Gradle sin que necesites instalarla manualmente.
*   **`lwjgl3/`**: **Propósito**: Módulo que contiene el código específico para lanzar el juego como una aplicación de escritorio en sistemas operativos Windows, macOS y Linux, utilizando la biblioteca LWJGL 3.

### 2. Módulo `core` (Detalle)

*   **`core/build.gradle`**: Configuración de Gradle específica para el módulo `core`, donde se declaran las dependencias principales del juego (LibGDX, Box2D, etc.).
*   **`core/src/main/java/`**: Contiene el código fuente Java de la lógica principal de tu juego.
    *   Dentro de esta carpeta, encontrarás tu paquete (ej. `com/minigolf/`) y la clase principal de tu juego (ej. `MiniGolfGame.java`).

### 3. Módulo `lwjgl3` (Detalle)

*   **`lwjgl3/build.gradle`**: Configuración de Gradle para el módulo de escritorio, incluyendo dependencias específicas para LWJGL3.
*   **`lwjgl3/nativeimage.gradle`**: Script de Gradle adicional para configurar la creación de imágenes nativas con GraalVM (si se utiliza esa característica).
*   **`lwjgl3/icons/`**: Carpeta que contiene los iconos de la aplicación (ej. `logo.icns` para macOS, `logo.ico` para Windows, `logo.png` genérico) para la versión de escritorio.
*   **`lwjgl3/src/main/java/`**: Contiene el código fuente Java para el lanzador de escritorio.
    *   Aquí se encuentra la clase `Lwjgl3Launcher.java` (o similar) con el método `main` para iniciar la aplicación.
    *   También puede incluir clases de ayuda como `StartupHelper.java`.
*   **`lwjgl3/src/main/resources/`**: Puede contener recursos adicionales específicos solo para la versión de escritorio.

## Cómo Iniciar el Proyecto por Primera Vez

La forma más común de iniciar tu juego en el escritorio es:

1.  **Abrir una Terminal o Símbolo del Sistema:**

2.  **Navegar al Directorio Raíz del Proyecto:**

3.  **Ejecutar la Tarea de Gradle:**
    *   En Windows:
        ```bash
        gradlew.bat lwjgl3:run
        ```
    *   En Linux y macOS:
        ```bash
        gradlew lwjgl3:run
        ```
        ```bash
        gradle lwjgl3:run
        ```

    La primera vez que ejecutes esto, Gradle descargará las dependencias necesarias (incluyendo la propia versión de Gradle si no está presente). Esto puede tardar unos minutos. Después, tu juego debería compilarse y aparecer una ventana con la pantalla de inicio predeterminada de LibGDX.