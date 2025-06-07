package com.minigolf;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class manejoEventos implements InputProcessor {

    // Factor de conversion para el mundo 
    private final float PIXEL_A_METRO = 0.01f; // 100 pixeles = 1m

    // Declaramos el radio de las bolas y el máximo de fuerza que se puede ejercer
    private final float RADIO = 10 * PIXEL_A_METRO; // La bola tiene un radio de 0.1m
    private final float MAXIMO_FUERZA = 2f;

    // Atributos
    private ArrayList<Jugador> jugadores;
    private World mundoBox2d;

    // Indice para saber con que jugador estamos trabajando
    private int jugadorActual = 0;

    // Fuerza del golpe 
    private float fuerza = 0.1f;
    // Angulo (0-360) para almacenar la dirección del golpe
    private float anguloDireccion = 0f;

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

        // Al presionar la tecla espacio, se ejecuta el golpe si este jugador tiene bola
        if (keycode == Input.Keys.SPACE && jugadores.get(jugadorActual).getBolaJugador() != null) {

            // El fector fuerza tiene una magnitud (fuerza) y una dirección (anguloDireccion) para cada uno
            //  de sus componentes
            Vector2 vectorFuerza = new Vector2(
                    MathUtils.cosDeg(anguloDireccion) * fuerza,
                    MathUtils.sinDeg(anguloDireccion) * fuerza);

            // Obtenemos la bola del jugador y guardamos su posición
            Body bolaTemporal = jugadores.get(jugadorActual).getBolaJugador();
            Vector2 posicionBola = bolaTemporal.getPosition();

            // Calculamos el angulo opuesto para el punto de impacto
            float anguloTemporal = anguloDireccion + 180;
            // Si este se pasa de 360, obtenemos el angulo correcto
            if (anguloTemporal > 360) {
                anguloTemporal -= 360;
            }

            // A partir de este angulo opuesto calculamos el punto de impacto sobre la pelota
            Vector2 puntoImpacto = new Vector2(posicionBola.x + MathUtils.cosDeg(anguloTemporal) * RADIO,
                    posicionBola.y + MathUtils.sinDeg(anguloTemporal) * RADIO);

            // Aplicamos la fuerza de manera instantánea al centro de la bola
            bolaTemporal.applyLinearImpulse(vectorFuerza, puntoImpacto, true);

            // Mostramos la magnitud
            System.out.println("Magnitud fuerza: " + vectorFuerza.len() + "Nw");

            // Este jugador ya golpeo
            jugadores.get(jugadorActual).setPuedeGolpear(false);

        }
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

        // Solo agregamos una bola si el jugador no tiene
        if (jugadores.get(jugadorActual).getBolaJugador() == null) {
            // Le agregamos la bola al jugador
            jugadores.get(jugadorActual).setBolaJugador(colocarBola(mundoBox2d, screenX, screenY));
            // Cambiamos la bandera para que pueda golpear
            jugadores.get(jugadorActual).setPuedeGolpear(true);
        }

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
    private Body colocarBola(World mundoBax2d, int screenX, int screenY) {

        /* --------- Creación bola --------- */

        // Definimos un BodyDef para la bola
        BodyDef bolaBodyDef = new BodyDef();
        // Establecemos el tipo de cuerpo como dinámico (se mueve y responde a fuerzas)
        bolaBodyDef.type = BodyType.DynamicBody;
        // Establecemos la posición inicial de la bola en el centro del mundo virtual
        // Convertir coordenadas de pantalla (origen arriba-izquierda) a Box2D (origen abajo-izquierda),
        // haciendo la conversion de pixel a metro, 900px * 0.01 = 9m
        float worldX = screenX * PIXEL_A_METRO;
        float worldY = (Gdx.graphics.getHeight() - screenY) * PIXEL_A_METRO;
        bolaBodyDef.position.set(worldX, worldY);

        // Creamos el Body de la bola en el mundo de Box2D usando la definición anterior
        Body bolaBody = mundoBox2d.createBody(bolaBodyDef);
        // Para reducir la velocidad gradualmente
        bolaBody.setLinearDamping(0.3f);

        // Creamos una forma circular para la bola y establecemos su radio
        CircleShape bolaShape = new CircleShape();
        bolaShape.setRadius(RADIO);

        // Creamos una FixtureDef para definir las propiedades físicas de la bola (lo mas realista posible)
        FixtureDef bolaFixtureDef = new FixtureDef();
        bolaFixtureDef.shape = bolaShape;
        bolaFixtureDef.restitution = 0.6f; // Rebote
        bolaFixtureDef.density = 1f;
        bolaFixtureDef.friction = 0.1f;

        // Creamos la fixture y la unimos al cuerpo de la bola
        bolaBody.createFixture(bolaFixtureDef);

        // Liberamos la memoria de la forma una vez que ya no se necesita
        bolaShape.dispose();

        return bolaBody;
    }

    // Método para actualizar el vectorDireccion
    public void actualizarAngulo(int direccion) {

        switch (direccion) {
            case 0:
                this.anguloDireccion -= 0.02f;
                break;
            case 1:
                this.anguloDireccion += 0.02f;
                break;
        }
    }

    // Método para actualizar la fuerza del golpe
    public void actualizarFuerza(int direccion) {

        // Calculamos la fuerza aplicada, asegurándonos que no pase de 10 y que no baje de 1
        switch (direccion) {
            case 0:
                this.fuerza = Math.max(0.1f, this.fuerza - 0.01f);
                break;
            case 1:
                this.fuerza = Math.min(MAXIMO_FUERZA, this.fuerza + 0.01f);
                break;
        }
    }

    // Método para obtener el jugador actual
    public void setJugadorActual(int jugadorActual) {
        this.jugadorActual = jugadorActual;
    }

    // Método para regresar la fuerza actual en porcentaje
    public float obtenerFuerza() {
        return (this.fuerza * 100) / MAXIMO_FUERZA;
    }

    // Método para regresar el angulo actual de dirección
    public float obtenerAnguloDireccion(){
        return this.anguloDireccion;
    }
}