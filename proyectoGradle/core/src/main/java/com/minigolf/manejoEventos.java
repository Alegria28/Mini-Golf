package com.minigolf;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class manejoEventos implements InputProcessor {

    ArrayList<Jugador> jugadores;
    World mundoBox2d;

    // Recibimos los jugadores y el mundo, estos por referencia
    public manejoEventos(ArrayList<Jugador> jugadores, World mundoBox2d) {
        this.jugadores = jugadores;
        this.mundoBox2d = mundoBox2d;
    }

    /** Called when a key was pressed
     * 
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed */
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    /** Called when a key was released
     * 
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed */
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    /** Called when a key was typed
     * 
     * @param character The character
     * @return whether the input was processed */
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    /** Called when the screen was touched or a mouse button was pressed. The button parameter will be {@link Buttons#LEFT} on iOS.
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button the button
     * @return whether the input was processed */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        jugadores.get(0).setBolaJugador(colocarBola(mundoBox2d, screenX, screenY));

        return false;
    }

    /** Called when a finger was lifted or a mouse button was released. The button parameter will be {@link Buttons#LEFT} on iOS.
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button the button
     * @return whether the input was processed */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    /** Called when the touch gesture is cancelled. Reason may be from OS interruption to touch becoming a large surface such as
     * the user cheek). Relevant on Android and iOS only. The button parameter will be {@link Buttons#LEFT} on iOS.
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button the button
     * @return whether the input was processed */
    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    /** Called when a finger or the mouse was dragged.
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @return whether the input was processed */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    /** Called when the mouse was moved without any buttons being pressed. Will not be called on iOS.
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @return whether the input was processed */
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    /** Called when the mouse wheel was scrolled. Will not be called on iOS.
     * @param amountX the horizontal scroll amount, negative or positive depending on the direction the wheel was scrolled.
     * @param amountY the vertical scroll amount, negative or positive depending on the direction the wheel was scrolled.
     * @return whether the input was processed. */
    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    // Método para colocar la bola donde el usuario haga click
    public Body colocarBola(World mundoBax2d, int screenX, int screenY) {
        
        /* --------- Creación bola --------- */

        // Definimos un BodyDef para la bola
        BodyDef bolaBodyDef = new BodyDef();
        // Establecemos el tipo de cuerpo como dinámico (se mueve y responde a fuerzas)
        bolaBodyDef.type = BodyType.DynamicBody;
        // Establecemos la posición inicial de la bola en el centro del mundo virtual
        // Convertir coordenadas de pantalla (origen arriba-izquierda) a Box2D (origen abajo-izquierda)
        float worldX = screenX;
        float worldY = Gdx.graphics.getHeight() - screenY;
        bolaBodyDef.position.set(worldX, worldY);

        // Creamos el Body de la bola en el mundo de Box2D usando la definición anterior
        Body bolaBody = mundoBox2d.createBody(bolaBodyDef);

        // Creamos una forma circular para la bola y establecemos su radio
        CircleShape bolaShape = new CircleShape();
        bolaShape.setRadius(10f);

        // Creamos una FixtureDef para definir las propiedades físicas de la bola
        FixtureDef bolaFixtureDef = new FixtureDef();
        bolaFixtureDef.shape = bolaShape;
        bolaFixtureDef.friction = 0.4f;

        // Creamos la fixture y la unimos al cuerpo de la bola
        bolaBody.createFixture(bolaFixtureDef);

        // Liberamos la memoria de la forma una vez que ya no se necesita
        bolaShape.dispose();

        return bolaBody;
    }
}
