## Herramientas a usar
- **LibGDX**: Framework de desarrollo de juegos 2D y 3D en Java que proporciona gran cantidad de funcionalidades como:
    - Gestión de gráficos
    - Entrada de usuarios
    - Audio
    - Gestión de recursos 
    - Física (facilita la integración de bibliotecas de física populares como Jbox2d)
    - UI 
    - Red
    - Documentación: [https://libgdx.com/wiki/](https://libgdx.com/wiki/)
- **Jbox2d (Box2D)**: Motor de física popular donde se pueden definir objetos con propiedades físicas (tamaño, forma, masa, densidad...). Luego el motor de física se encarga de calcular como interactúan estos objetos entre si bajo la influencia de fuerzas y colisiones
    - Documentación: [https://box2d.org/documentation/](https://box2d.org/documentation/)

## Tareas a realizar
1. Creación del menu principal
    Menu creativo y alusivo al deporte del golf
    - Iniciar juego
    - Instrucciones básicas del juego
    - Créditos
    - Salir
2. Preparación del juego
    - Selección de numero de jugadores (3)
    - Selección del nombre y color de bola de cada jugador (No dejar que estos colores se repitan)
3. Ciclo de juego principal
    Primer paso:
        - Mostrar el turno del jugador
        - Mostrar par del hoyo
        - Creación de los hoyos (¿Ya preestablecidos o generados de manera aleatoria?), con caídas y obstáculos
        - Designar area para colocar bola
        - Designar tarjeta de puntos (pueden ser pares como los del campestre)
    Segundo paso:
        - Implementar función para colocar la bola
        - Implementar función para golpear la bola (colisiones entre bolas, campo y fuerzas), mostrar linea de dirección y contar strokes individuales
        - Implementar función para terminar el hoyo
            - Una vez que un jugador termino, esperar a que todos lo hagan
            - Una vez que todos hayan acabado, mostrar tarjeta de puntos 
    Tercer paso:
        - ¿Animaciones al siguiente hoyo?
        - Al terminar mostrar la tarjeta de puntos final, y la opción de regresar al menu