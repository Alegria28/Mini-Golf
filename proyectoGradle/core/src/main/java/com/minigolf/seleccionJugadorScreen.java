package com.minigolf;

// Importamos los recursos necesarios para la clase
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Las clases que implementan son diferentes escenas que Game puede mostrar, por lo que son parte del ciclo de vida del juego (ver {@link MiniGolfMain}).
 */
public class seleccionJugadorScreen implements Screen {

    // Atributos
    private Stage stage;
    private Table table;
    private Stack stack;
    private final MiniGolfMain game;
    private BitmapFont font;
    Image imagenFondo;
    TextButtonStyle buttonStyle;

    // Dimensiones virtuales (buena practica para el diseño)
    private final float VIRTUAL_WIDTH = 900;
    private final float VIRTUAL_HEIGHT = 900;

    // Constructor de la clase
    public seleccionJugadorScreen(MiniGolfMain game, BitmapFont font, Image imagenFondo, TextButtonStyle buttonStyle) {
        this.game = game;
        this.font = font;
        this.imagenFondo = imagenFondo;
        this.buttonStyle = buttonStyle;
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

        table.setDebug(true);

        /* --------- Creación label principal --------- */

        // Creamos el estilo para nuestro label
        LabelStyle labelPrincipalStyle = new LabelStyle();
        // Cambiamos su font
        labelPrincipalStyle.font = font;
        // El color va a ser blanco
        labelPrincipalStyle.fontColor = Color.WHITE;

        // Creamos nuestro label
        Label labelPrincipal = new Label("Numero de jugadores", labelPrincipalStyle);

        // Agregamos este label al table con .colspan() para que el ancho de este label
        // ocupe el de 3 columnas, ya que tenemos 3 botones abajo, cada uno en su propia columna
        // y para .center() puede centrar este label correctamente arriba de los 3 botones
        table.add(labelPrincipal).colspan(3).center();

        /* --------- Creación opciones numero de jugadores --------- */

        // Indicamos que lo siguiente que se va a agregar va a estar en otra row
        table.row();

        TextButton boton1Jugador = new TextButton("1 jugador", buttonStyle);

        TextButton boton2Jugador = new TextButton("2 jugadores", buttonStyle);

        TextButton boton3Jugador = new TextButton("3 jugadores", buttonStyle);

        // Agregamos los botones para que se van de forma horizontal con espaciado
        table.add(boton1Jugador).height(70).pad(50);
        table.add(boton2Jugador).height(70).pad(50);
        table.add(boton3Jugador).height(70).pad(50);

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

        // En cada fotograma, se limpia el buffer de color (en pocas palabra se limpia la pantalla)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Actualizamos el estado de todos los actores que están en el stage
        stage.act(Gdx.graphics.getDeltaTime());
        // Dibujamos los estados en su nuevo estado
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
