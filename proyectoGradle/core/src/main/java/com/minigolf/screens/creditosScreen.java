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
 * Pantalla de créditos del juego MiniGolf.
 * Muestra información sobre los desarrolladores y el juego.
 */
public class creditosScreen implements Screen {

    // Atributos de la clase
    private final MiniGolfMain game;
    private Stage stage;
    private Viewport viewport;
    private Texture textureFondo;
    private BitmapFont fontTitulo;
    private BitmapFont fontContenido;
    private Skin buttonSkin;

    // Dimensiones virtuales (buena práctica para el diseño)
    private final float VIRTUAL_WIDTH = 900;
    private final float VIRTUAL_HEIGHT = 900;

    // Constructor de la clase
    public creditosScreen(final MiniGolfMain game) {
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

        // Cargamos la tipografía que se utilizará (la misma que en MenuInicial)
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Inter-Variable.ttf"));

        // Configuramos el font para el título de los créditos
        FreeTypeFontParameter paramTitulo = new FreeTypeFontParameter();
        paramTitulo.size = 50; // Tamaño un poco más grande para el título
        paramTitulo.color = Color.WHITE;
        fontTitulo = fontGenerator.generateFont(paramTitulo);

        // Configuramos el font para el contenido de los créditos y el botón
        FreeTypeFontParameter paramContenido = new FreeTypeFontParameter();
        paramContenido.size = 32; // Tamaño para el contenido
        paramContenido.color = Color.WHITE;
        fontContenido = fontGenerator.generateFont(paramContenido);

        // Ya que ya no lo vamos a ocupar, podemos liberarlo
        fontGenerator.dispose();

        /* --------- Configuración imagen de fondo --------- */

        // Creamos la textura a partir de la imagen (misma que en MenuInicial)
        textureFondo = new Texture(Gdx.files.internal("fondoInicio.png"));
        // Creamos la imagen
        Image imagenFondo = new Image(textureFondo);
        // Aplicamos la opacidad a nuestra imagen de fondo para que el texto sea más legible
        imagenFondo.setColor(new Color(1f, 1f, 1f, 0.5f)); // 50% de opacidad

        /* --------- Configuración estilos para Labels y Botones --------- */

        // Estilo para el título de los créditos
        LabelStyle estiloTitulo = new LabelStyle(fontTitulo, Color.WHITE);
        // Estilo para el contenido de los créditos
        LabelStyle estiloContenido = new LabelStyle(fontContenido, Color.WHITE);

        // Cargamos el skin para los botones (el mismo que en MenuInicial para consistencia)
        buttonSkin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        // Creamos un TextButtonStyle y le asignamos la fuente y los drawables del skin
        TextButtonStyle buttonStyle = new TextButtonStyle();
        buttonStyle.font = fontContenido; // Usamos la fuente de contenido para el botón
        // Declaraciones dentro del .json para los estados del botón
        buttonStyle.up = buttonSkin.getDrawable("buttonUp");
        buttonStyle.down = buttonSkin.getDrawable("buttonDown");
        buttonStyle.over = buttonSkin.getDrawable("buttonOver");

        /* --------- Creación de la tabla principal para la pantalla --------- */

        Table mainTable = new Table();
        mainTable.setFillParent(true); // La tabla ocupará todo el stage
        mainTable.center(); // Centramos el contenido de la tabla

        /* --------- Título de la pantalla de créditos --------- */

        Label labelTitulo = new Label("Créditos del Juego", estiloTitulo);
        mainTable.add(labelTitulo).padBottom(50).row(); // Añadimos el título y un padding inferior

        /* --------- Contenido de los créditos --------- */

        Label labelEstudios = new Label("UNIVERSIDAD AUTONOMA DE AGUASCALIENTES", estiloContenido);
        labelEstudios.setAlignment(Align.center);
        mainTable.add(labelEstudios).padBottom(50).row();

        Label labelDesarrollo = new Label("Desarrollado por:", estiloContenido);
        labelDesarrollo.setAlignment(Align.center); // Centramos el texto
        mainTable.add(labelDesarrollo).padBottom(10).row();

        Label labelNombres = new Label("Eduardo Arturo Alegria Vela", estiloContenido);
        labelNombres.setAlignment(Align.center);
        mainTable.add(labelNombres).padBottom(30).row();

        Label labelAgradecimientos = new Label("Oscar de Jesus Guillen Ibarra", estiloContenido);
        labelAgradecimientos.setAlignment(Align.center);
        mainTable.add(labelAgradecimientos).padBottom(10).row();

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
                Gdx.app.log("CreditosScreen", "Regresando al menu principal.");
                // Cambiamos la pantalla de nuevo al MenuInicial
                game.setScreen(new menuInicialScreen(game));
            }
        });

        mainTable.add(botonRegresar).center().height(70).width(400).padTop(40).row(); // Añadimos el botón

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
        fontContenido.dispose();
        buttonSkin.dispose();
        // Es una buena práctica anular el InputProcessor al deshacerse de la pantalla
        Gdx.input.setInputProcessor(null);
    }
}
