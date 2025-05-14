package com.minigolf;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
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

    // Dimensiones virtuales (buena practica para el diseño)
    private final float VIRTUAL_WIDTH = 900;
    private final float VIRTUAL_HEIGHT = 900;

    /* Ciclo de vida de la aplicación */

    @Override
    public void create() {

        // Creamos una cámara, que actúa como el "ojo" del juego, definiendo qué parte del mundo se mostrará en pantalla
        camera = new OrthographicCamera();

        // El viewport configura y gestiona la cámara, definiendo su "campo de visión" con las dimensiones VIRTUAL_WIDTH y VIRTUAL_HEIGHT.
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);

        // Creamos nuestro stage con el tamaño del viewport especificado (para que stage sepa cual es su sistema de coordenadas)
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        // Creamos nuestro table
        table = new Table();
        // table tiene el mismo tamaño que el stage
        table.setFillParent(true);

        // Agregamos a nuestro stage el table (un actor)
        stage.addActor(table);

    }

    // This method is called by the game loop from the application every time rendering should be performed.
    // Game logic updates are usually also performed in this method.
    @Override
    public void render() {
        // En cada fotograma, se limpia el buffer de color (en pocas palabra se limpia la pantalla)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Actualizamos el estado de todos los actores que están en el stage
        stage.act(Gdx.graphics.getRawDeltaTime());
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
    }
}