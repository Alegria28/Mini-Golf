package com.minigolf;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class MiniGolfMain extends ApplicationAdapter {

    // Variables globales
    private Stage stage;
    private Table table;
    private Viewport viewport;
    private OrthographicCamera camera;
    private Texture textureFondo;
    private Texture textureImagenJuego;
    Stack stack;

    // Dimensiones virtuales (buena practica para el diseño)
    private final float VIRTUAL_WIDTH = 900;
    private final float VIRTUAL_HEIGHT = 900;

    /* Ciclo de vida de la aplicación */

    @Override
    public void create() {

        /* Configuración inicial stage y table */

        // Creamos una cámara, que actúa como el "ojo" del juego, definiendo qué parte del mundo se mostrará en pantalla
        camera = new OrthographicCamera();

        // El viewport configura y gestiona la cámara, definiendo su "campo de visión" con las dimensiones VIRTUAL_WIDTH y VIRTUAL_HEIGHT.
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);

        // Creamos nuestro stage con el tamaño del viewport especificado (para que stage sepa cual es su sistema de coordenadas)
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        // Creamos nuestro table (actor)
        table = new Table();

        /* Agregar imagen del juego */

        // Cargamos la imagen en la memoria
        textureImagenJuego = new Texture(Gdx.files.internal("logoMiniGolf.png"));

        // A partir de la textura, creamos la imagen
        Image imagenMiniGolf = new Image(textureImagenJuego);

        // Nos alineamos en la parte superior
        table.top();

        // Agregamos esta imagen a nuestro table en la parte superior, regresa un objeto de tipo Cell donde agregamos un padding en la parte de arriba
        table.add(imagenMiniGolf).padTop(30);

        /* Configuración capas y fondo*/

        // Cargamos la imagen en la memoria
        textureFondo = new Texture(Gdx.files.internal("fondoInicio.png"));

        // A partir de nuestra textura, creamos la imagen
        Image imagenFondo = new Image(textureFondo);

        // Aplicamos la opacidad a nuestra imagen
        imagenFondo.setColor(new Color(1f, 1f, 1f, 0.5f));

        // Iniciamos nuestro stack para tener "capas" de nuestros actores
        stack = new Stack();
        // De esta manera stack ocupara todo el espacio de stage, y asi todos los demás que se agreguen
        stack.setFillParent(true);

        // Añadimos primero el fondo
        stack.add(imagenFondo);

        // Añadimos el table que va a estar encima de la imagen de fondo 
        stack.add(table);

        /* Actores */

        // Agregamos el actor que tiene el orden de los demás
        stage.addActor(stack);

    }

    // This method is called by the game loop from the application every time (cada fotograma) rendering should be performed.
    // Game logic updates are usually also performed in this method.
    @Override
    public void render() {

        // En cada fotograma, se limpia el buffer de color (en pocas palabra se limpia la pantalla)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Actualizamos el estado de todos los actores que están en el stage
        stage.act(Gdx.graphics.getDeltaTime());
        // Dibujamos los estados en su nuevo estado
        stage.draw();

    }

    // This method is called every time the game screen is resized and the game is
    // not in the paused state. It is also called once just after the create() method.
    // The parameters are the new width and height the screen has been resized to in pixels.
    @Override
    public void resize(int width, int height) {
        // Nos aseguramos de que nuestro viewport (ventana a traves de la cual se ve el stage) siempre este centrado
        stage.getViewport().update(width, height, true);
    }

    // Called when the application is destroyed. It is preceded by a call to pause().
    @Override
    public void dispose() {
        // Limpiamos y liberamos todos los recursos cargados
        stage.dispose();
        textureFondo.dispose();
        textureImagenJuego.dispose();
    }
}