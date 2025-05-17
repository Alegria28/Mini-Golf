// cspell: ignore minigolf, Creditos, boton

package com.minigolf;

// En lugar de ApplicationListener para tener un manejo mas sencillo de las pantallas
import com.badlogic.gdx.Game;

// Importamos los recursos necesarios para la clase
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Clase controlador para el juego para gestionar el ciclo de vida de la aplicación y el manejo de pantallas
 */
public class MiniGolfMain extends Game {

    // Atributos compartidos 
    public BitmapFont font;
    public Image imagenFondo;

    /* Ciclo de vida del juego */

    @Override
    public void create() {
        // Cargamos la tipografía que se utilizara
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Inter-Variable.ttf"));
        // Creamos un objeto para modificar las características de esta tipografía
        FreeTypeFontParameter fontParameter = new FreeTypeFontParameter();
        fontParameter.size = 40;
        fontParameter.padLeft = 10;
        fontParameter.padRight = 10;
        // Creamos el font con las características nuevas
        font = fontGenerator.generateFont(fontParameter);
        // Ya que ya no lo vamos a ocupar, podemos liberarlo
        fontGenerator.dispose();

        // Creamos la textura a partir de la imagen
        Texture textureFondo = new Texture(Gdx.files.internal("fondoInicio.png"));
        // Creamos la imagen
        imagenFondo = new Image(textureFondo);
        // Aplicamos la opacidad a nuestra imagen de fondo
        imagenFondo.setColor(new Color(1f, 1f, 1f, 0.5f));

        // Establecemos la pantalla inicial del juego como el menú principal
        this.setScreen(new menuInicialScreen(this, font, imagenFondo));
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
        // Liberamos los recursos
        font.dispose();
        screen.dispose();
        // Llama al dispose de la clase Game
        super.dispose();
    }
}