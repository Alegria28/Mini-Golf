package com.minigolf;

// En lugar de ApplicationListener para tener un manejo mas sencillo de las pantallas
import com.badlogic.gdx.Game;

/**
 * Clase controlador para el juego para gestionar el ciclo de vida de la aplicación y el manejo de pantallas
 */
public class MiniGolfMain extends Game {

    /* Ciclo de vida del juego */

    @Override
    // Method called once when the application is created.
    public void create() {
        // Establecemos la pantalla inicial del juego como el menú principal
        this.setScreen(new menuInicialScreen(this));
    }

    // This method is called by the game loop from the application every time (cada fotograma) rendering should be performed.
    @Override
    public void render() {
        // Llamamos al método render que esta en la clase Game
        super.render();
    }

    // This method is called every time the game screen is resized.
    @Override
    public void resize(int width, int height) {
        // Llamamos al método resize que esta en la clase Game
        screen.resize(width, height);
    }

    // Called when the application is destroyed.
    @Override
    public void dispose() {
        // Llama al dispose de la clase Game
        super.dispose();
    }
}