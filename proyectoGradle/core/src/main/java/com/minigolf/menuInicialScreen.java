package com.minigolf;

// Importamos los recursos necesarios para la clase
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Las clases que implementan son diferentes escenas que Game puede mostrar, por lo que son parte del ciclo de vida del juego (ver {@link MiniGolfMain}).
 */
public class menuInicialScreen implements Screen {

    // Atributos de la clase
    private Stage stage;
    private Table table;
    private Stack stack;
    private final MiniGolfMain game;
    private BitmapFont font;
    private Image imagenFondo;

    // Dimensiones virtuales (buena practica para el diseño)
    private final float VIRTUAL_WIDTH = 900;
    private final float VIRTUAL_HEIGHT = 900;

    // Constructor de la clase
    public menuInicialScreen(final MiniGolfMain game, BitmapFont font, Image imagenFondo) {
        this.game = game;
        this.font = font;
        this.imagenFondo = imagenFondo;
    }

    @Override
    public void show() {

        /* --------- Configuración inicial stage y table --------- */

        // Creamos una cámara, que actúa como el "ojo" del juego, definiendo qué parte del mundo se mostrará en pantalla
        OrthographicCamera camera = new OrthographicCamera();

        // El viewport configura y gestiona la cámara, definiendo su "campo de visión" con las dimensiones VIRTUAL_WIDTH y VIRTUAL_HEIGHT.
        Viewport viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);

        // Creamos nuestro stage con el tamaño del viewport especificado (para que stage sepa cual es su sistema de coordenadas)
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        // Creamos nuestro table (actor)
        table = new Table();

        /* --------- Agregar imagen del juego --------- */

        // Creamos la textura a partir de la imagen
        Texture textureImagenJuego = new Texture(Gdx.files.internal("logoMiniGolf.png"));

        // Creamos la imagen
        Image imagenMiniGolf = new Image(textureImagenJuego);

        // Nos alineamos en la parte superior
        table.top();

        // Agregamos la imagen a la tabla y le aplica un espacio superior  (para estar separado del viewport)
        table.add(imagenMiniGolf).padTop(30);

        /* --------- Configuración de los botones --------- */

        // Creamos un buttonStyle para asignarle el font creado a los botones
        TextButtonStyle buttonStyle = new TextButtonStyle();
        buttonStyle.font = font;

        // Utilizamos la skin para obtener los estilos visuales de los botones (fondos para los estados up y down)
        Skin buttonSkin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        // Declaraciones dentro del .json
        buttonStyle.up = buttonSkin.getDrawable("buttonUp");
        buttonStyle.down = buttonSkin.getDrawable("buttonDown");

        /* --------- Creación de los botones --------- */

        // Creamos el botón con el texto y el estilo que hemos definido para este
        TextButton botonIniciar = new TextButton("Iniciar", buttonStyle);
        botonIniciar.addListener(new InputListener() {
            @Override
            // Called when a mouse button or a finger touch goes down on the actor
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            // Called when a mouse button or a finger touch goes up anywhere, but only if touchDown previously returned true for the mouse
            // button or touch
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Entrando a la pantalla de seleccion de jugadores");

                // Usando la referencia del juego principal, actualizamos screen creando una screen
                game.setScreen(new seleccionJugadorScreen(game, font, imagenFondo, buttonStyle));
            }
        });

        // Creamos el botón con el texto y el estilo que hemos definido para este
        TextButton botonInstrucciones = new TextButton("Instrucciones", buttonStyle);

        // Creamos el botón con el texto y el estilo que hemos definido para este
        TextButton botonCreditos = new TextButton("Creditos", buttonStyle);

        // Creamos el botón con el texto y el estilo que hemos definido para este
        TextButton botonSalir = new TextButton("Salir", buttonStyle);
        botonSalir.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // Salimos de la aplicación
                Gdx.app.exit();
            }
        });

        // Indicamos que lo siguiente que se va a agregar va a estar en otra row
        table.row();
        // Agregamos los botones con una altura y espaciado
        table.add(botonIniciar).center().height(70).padTop(60);

        table.row();
        table.add(botonInstrucciones).center().height(70).padTop(40);

        table.row();
        table.add(botonCreditos).center().height(70).padTop(40);

        table.row();
        table.add(botonSalir).center().height(70).padTop(40);

        /* --------- Configuración capas y fondo --------- */

        // Iniciamos nuestro stack para tener "capas" de nuestros actores
        stack = new Stack();
        // De esta manera stack ocupara todo el espacio de stage, y asi todos los demás que se agreguen
        stack.setFillParent(true);

        // Añadimos primero el fondo
        stack.add(imagenFondo);

        // Añadimos el table que va a estar encima de la imagen de fondo
        stack.add(table);

        /* --------- Actores --------- */

        // Agregamos el actor que tiene el orden de los demás
        stage.addActor(stack);
    }

    @Override
    public void render(float delta) {
        // Establece el color con el que se limpiará la pantalla (negro) para asegurarnos de que en cada fotograma se borre anterior antes de dibujar lo nuevo
        Gdx.gl.glClearColor(0, 0, 0, 1);

        // En cada fotograma, se limpia el buffer de color (en pocas palabra se limpia la pantalla)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Actualizamos todos los actores para el fotograma actual, el Math.min previene problemas si el juego se pausa y 'delta' se vuelve muy grande.
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));

        // Dibujamos los actores en su nuevo estado
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Nos aseguramos de que nuestro viewport (ventana a traves de la cual se ve el stage) siempre este centrado
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        // Limpiamos y liberamos todos los recursos cargados
        stage.dispose();
    }
}