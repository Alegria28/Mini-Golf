package com.minigolf.screens;

// Importamos los recursos necesarios para la clase
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

// Importamos la clase principal del juego
import com.minigolf.MiniGolfMain;

/**
 * Pantalla de instrucciones del juego MiniGolf.
 * Muestra cómo jugar, los controles y la dinámica del juego.
 */
public class instruccionesScreen implements Screen {

    // Atributos de la clase
    private final MiniGolfMain game;
    private Stage stage;
    private Viewport viewport;
    private Texture textureFondo;
    private BitmapFont fontTitulo;
    private BitmapFont fontSubtitulo; // Nuevo para subtítulos como "Objetivo del Juego"
    private BitmapFont fontContenido;
    private Skin buttonSkin;

    // Dimensiones virtuales (buena práctica para el diseño)
    private final float VIRTUAL_WIDTH = 900;
    private final float VIRTUAL_HEIGHT = 900;

    // Constructor de la clase
    public instruccionesScreen(final MiniGolfMain game) {
        this.game = game;
    }

    @Override
    public void show() {
        /* --------- Configuración inicial viewport y stage --------- */

        // Creamos una cámara
        OrthographicCamera camera = new OrthographicCamera();
        // El viewport configura la cámara con las dimensiones virtuales
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        // Creamos nuestro stage con el tamaño del viewport especificado
        stage = new Stage(viewport);
        // Configuramos el procesador de entrada para que stage pueda recibir eventos
        Gdx.input.setInputProcessor(stage);

        /* --------- Configuración de fonts --------- */

        // Cargamos la tipografía que se utilizará
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Inter-Variable.ttf"));

        // Configuramos el font para el título principal
        FreeTypeFontParameter paramTitulo = new FreeTypeFontParameter();
        paramTitulo.size = 40;
        paramTitulo.color = Color.WHITE;
        fontTitulo = fontGenerator.generateFont(paramTitulo);

        // Configuramos el font para los subtítulos
        FreeTypeFontParameter paramSubtitulo = new FreeTypeFontParameter();
        paramSubtitulo.size = 20; // Un poco más grande que el contenido normal
        paramSubtitulo.color = Color.YELLOW; // Color diferente para destacar subtítulos
        fontSubtitulo = fontGenerator.generateFont(paramSubtitulo);

        // Configuramos el font para el contenido de las instrucciones y el botón
        FreeTypeFontParameter paramContenido = new FreeTypeFontParameter();
        paramContenido.size = 16;
        paramContenido.color = Color.WHITE;
        fontContenido = fontGenerator.generateFont(paramContenido);

        // Ya que ya no lo vamos a ocupar, podemos liberarlo
        fontGenerator.dispose();

        /* --------- Configuración imagen de fondo --------- */

        // Creamos la textura a partir de la imagen
        textureFondo = new Texture(Gdx.files.internal("fondoInicio.png"));
        // Creamos la imagen
        Image imagenFondo = new Image(textureFondo);
        // Aplicamos la opacidad a nuestra imagen de fondo para que el texto sea más legible
        imagenFondo.setColor(new Color(1f, 1f, 1f, 0.5f)); // 50% de opacidad

        /* --------- Configuración estilos para Labels y Botones --------- */

        // Estilo para el título principal
        LabelStyle estiloTitulo = new LabelStyle(fontTitulo, Color.WHITE);
        // Estilo para los subtítulos
        LabelStyle estiloSubtitulo = new LabelStyle(fontSubtitulo, Color.YELLOW);
        // Estilo para el contenido de las instrucciones
        LabelStyle estiloContenido = new LabelStyle(fontContenido, Color.WHITE);

        // Cargamos el skin para los botones
        buttonSkin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        // Creamos un TextButtonStyle y le asignamos la fuente y los drawables del skin
        TextButtonStyle buttonStyle = new TextButtonStyle();
        buttonStyle.font = fontContenido;
        buttonStyle.up = buttonSkin.getDrawable("buttonUp");
        buttonStyle.down = buttonSkin.getDrawable("buttonDown");
        buttonStyle.over = buttonSkin.getDrawable("buttonOver");

        /* --------- Creación de la tabla principal para la pantalla --------- */

        Table mainTable = new Table();
        mainTable.setFillParent(true); // La tabla ocupará todo el stage
        mainTable.center().top().padTop(50); // Centramos el contenido de la tabla y lo alineamos arriba

        /* --------- Título de la pantalla de instrucciones --------- */
        Label labelTitulo = new Label("Instrucciones del Juego", estiloTitulo);
        mainTable.add(labelTitulo).padBottom(40).row(); // Añadimos el título y un padding inferior

        /* --------- Contenido de las instrucciones --------- */

        // Objetivo del Juego
        Label labelObjetivoTitulo = new Label("Objetivo del Juego", estiloSubtitulo);
        labelObjetivoTitulo.setAlignment(Align.center);
        mainTable.add(labelObjetivoTitulo).padBottom(10).row();

        Label labelObjetivoContenido = new Label(
            "Tu meta es meter la pelota en el hoyo al final de cada uno de los 18 niveles (hoyos) con el menor número de golpes.",
            estiloContenido
        );
        labelObjetivoContenido.setWrap(true); // Permite que el texto se ajuste a múltiples líneas
        labelObjetivoContenido.setAlignment(Align.center);
        mainTable.add(labelObjetivoContenido).width(VIRTUAL_WIDTH * 0.8f).padBottom(30).row();

        // Cómo Jugar (Controles y Mecánicas)
        Label labelComoJugarTitulo = new Label("Cómo Jugar (Controles y Mecánicas)", estiloSubtitulo);
        labelComoJugarTitulo.setAlignment(Align.center);
        mainTable.add(labelComoJugarTitulo).padBottom(10).row();

        Label labelControles1 = new Label(
            "1. Golpear la Pelota:\n" +
            "   - Deberás aplicar una fuerza al palo de golf para golpear la pelota. La velocidad dependerá de esta fuerza.\n" +
            "   - Para controlar la fuerza, usa las flechas de ARRIBA y ABAJO.",
            estiloContenido
        );
        labelControles1.setWrap(true);
        labelControles1.setAlignment(Align.left); // Alineamos a la izquierda para listas
        mainTable.add(labelControles1).width(VIRTUAL_WIDTH * 0.8f).padBottom(10).row();

        Label labelControles2 = new Label(
            "   - También deberás calcular el ángulo de tu golpe para dirigir la pelota correctamente hacia el hoyo.\n" +
            "   - Para controlar la dirección, usa las flechas DERECHA e IZQUIERDA.",
            estiloContenido
        );
        labelControles2.setWrap(true);
        labelControles2.setAlignment(Align.left);
        mainTable.add(labelControles2).width(VIRTUAL_WIDTH * 0.8f).padBottom(10).row();

        Label labelMovimiento = new Label(
            "2. Movimiento de la Pelota:\n" +
            "   - Una vez golpeada, la pelota se moverá por el área, rebotando en los bordes y obstáculos.\n" +
            "   - La pelota perderá velocidad progresivamente debido a la fricción con las diferentes superficies del terreno.",
            estiloContenido
        );
        labelMovimiento.setWrap(true);
        labelMovimiento.setAlignment(Align.left);
        mainTable.add(labelMovimiento).width(VIRTUAL_WIDTH * 0.8f).padBottom(10).row();

        Label labelObstaculos = new Label(
            "3. Obstáculos:\n" +
            "   - Cada hoyo cuenta con una variedad de obstáculos fijos. Estos elementos están diseñados para dificultar el camino hacia el hoyo.",
            estiloContenido
        );
        labelObstaculos.setWrap(true);
        labelObstaculos.setAlignment(Align.left);
        mainTable.add(labelObstaculos).width(VIRTUAL_WIDTH * 0.8f).padBottom(30).row();

        // Dinámica del Juego
        Label labelDinamicaTitulo = new Label("Dinámica del Juego", estiloSubtitulo);
        labelDinamicaTitulo.setAlignment(Align.center);
        mainTable.add(labelDinamicaTitulo).padBottom(10).row();

        Label labelNivel = new Label(
            "El juego consiste en recorrer un campo que contiene 18 hoyos. Completarás el juego al terminar todos los hoyos.",
            estiloContenido
        );
        labelNivel.setWrap(true);
        labelNivel.setAlignment(Align.left);
        mainTable.add(labelNivel).width(VIRTUAL_WIDTH * 0.8f).padBottom(10).row();

        Label labelMultijugador = new Label(
            "Modo Multijugador:\n" +
            "   - Si juegas con más de una persona, las jugadas se intercalarán.\n" +
            "   - Un jugador golpeará la pelota. Si no logra meterla en el hoyo, será el turno del siguiente jugador hasta que alguien logre embocar. El juego registrará los golpes de cada jugador.",
            estiloContenido
        );
        labelMultijugador.setWrap(true);
        labelMultijugador.setAlignment(Align.left);
        mainTable.add(labelMultijugador).width(VIRTUAL_WIDTH * 0.8f).padBottom(30).row();


        /* --------- Botón "Regresar al Menú Principal" --------- */

        TextButton botonRegresar = new TextButton("Regresar al Menú Principal", buttonStyle);
        botonRegresar.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true; // Devuelve true para indicar que el evento ha sido manejado
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // Se llama cuando el botón del ratón o el dedo se levanta, solo si touchDown devolvió true
                Gdx.app.log("InstruccionesScreen", "Regresando al menu principal.");
                // Cambiamos la pantalla de nuevo al MenuInicial
                game.setScreen(new menuInicialScreen(game));
            }
        });

        // Añadimos el botón con padding superior para separarlo del texto
        mainTable.add(botonRegresar).center().height(70).width(400).padTop(40).padBottom(50).row();

        /* --------- Configuración de capas y fondo --------- */

        // Iniciamos nuestro stack para tener "capas" de nuestros actores
        Stack stack = new Stack();
        stack.setFillParent(true); // El stack ocupará todo el espacio del stage

        // Añadimos primero el fondo
        stack.add(imagenFondo);
        // Añadimos la tabla principal que contendrá los textos y el botón
        stack.add(mainTable);

        /* --------- Agregamos el stack al stage --------- */

        stage.addActor(stack);
    }

    @Override
    public void render(float delta) {
        // En cada fotograma, se limpia el buffer de color (se limpia la pantalla)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Actualizamos el estado de todos los actores que están en el stage
        stage.act(Gdx.graphics.getDeltaTime());
        // Dibujamos todos los actores en su nuevo estado
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Nos aseguramos de que nuestro viewport siempre esté centrado
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {
        // Método llamado cuando la aplicación se pausa
    }

    @Override
    public void resume() {
        // Método llamado cuando la aplicación se reanuda
    }

    @Override
    public void hide() {
        // Método llamado cuando esta pantalla ya no es la actual
    }

    @Override
    public void dispose() {
        // Limpiamos y liberamos todos los recursos cargados
        stage.dispose();
        textureFondo.dispose();
        fontTitulo.dispose();
        fontSubtitulo.dispose(); // Liberamos la nueva fuente
        fontContenido.dispose();
        buttonSkin.dispose();
        // Es una buena práctica anular el InputProcessor al deshacerse de la pantalla
        Gdx.input.setInputProcessor(null);
    }
}
