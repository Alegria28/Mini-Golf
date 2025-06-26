package com.minigolf.screens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

// Importamos las clases necesarias
import com.minigolf.MiniGolfMain;
import com.minigolf.models.Jugador;
import com.minigolf.handlers.manejoColisiones;
import com.minigolf.handlers.manejoEventos;
// Importamos los niveles
import com.minigolf.niveles.*;

/**
 * Pantalla principal del juego de golf donde se desarrolla la partida
 */
public class jugarGolfScreen implements Screen {

    // Factor de conversion para el mundo de Box2D, donde 100px = 1m 
    private final float PIXEL_A_METRO = 0.01f;
    private final float Grosor_Muros_PX = 15f; // Grosor visual de las paredes en píxeles

    // Atributos
    private OrthographicCamera camera;
    private Stage stage;
    private Texture textureFondo;
    private Texture texturePuntoDeInicio;
    private Image imagePuntoDeInicio;
    private Stack stack;
    private Table tablePrincipal;
    private Label infoLabel;
    private Label informacionTurno;
    private BitmapFont font;
    private Texture texturePared;
    private Texture texturaHoyo;
    private Texture texturaBoost;

    private final MiniGolfMain game;
    private ArrayList<Jugador> jugadores;
    // Instancia de la clase que va a manejar los eventos
    private manejoEventos eventos;
    // Instancia de la clase que va a manejar las colisiones
    private manejoColisiones manejoColisiones;

    // Dimensiones virtuales
    private final float VIRTUAL_WIDTH = 900;
    private final float VIRTUAL_HEIGHT = 900;

    /* --------- Atributos motor físico Box2D --------- */

    // Creamos nuestro mundo sin gravedad
    private World mundoBox2d = new World(new Vector2(0, 0), true);
    // Para poder ver los elementos en nuestro mundo de Box2D
    private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
    // Para poder dibujar figuras simples
    private ShapeRenderer shapeRenderer;

    // Para tener un control sobre el nivel actual del juego
    private int nivelActual;
    // Para saber si ya cargamos el nivel indicado
    private boolean nivelCargado = false;
    // Variable para tener un control de turno durante el juego
    private int turnoActual = 0;
    // Para guardar los bodies temporales que se van a eliminar
    private HashMap<Body, Boolean> hashMapBodiesTemporales = new HashMap<Body, Boolean>();
    // Para guardar las paredes del campo
    private ArrayList<Body> arrayListParedes = new ArrayList<Body>();

    // Constructor de la clase
    public jugarGolfScreen(MiniGolfMain game, ArrayList<Jugador> jugadores, int nivelActual) {
        this.game = game;
        this.jugadores = jugadores;
        this.nivelActual = nivelActual;

        // Cambiamos el estado de los jugadores a que no han terminado el hoyo
        for (Jugador jugador : this.jugadores) {
            jugador.setHoyoTerminado(false);
        }
        // Iniciamos las instancias de nuestros handlers
        this.eventos = new manejoEventos(jugadores, mundoBox2d, nivelActual);
        this.manejoColisiones = new manejoColisiones(jugadores, hashMapBodiesTemporales, mundoBox2d,
                eventos);

        // Iniciamos la instancia de shapeRenderer
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void show() {

        /* --------- Configuración inicial stage, eventos y cámara --------- */

        // Creamos un InputMultiplexer para manejar varios eventos
        InputMultiplexer multiplexer = new InputMultiplexer();

        camera = new OrthographicCamera();
        Viewport viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);

        // Creamos el stage
        stage = new Stage(viewport);

        // Ponemos la posición de la cámara al centro del mundo virtual (450px,450px), esto para que la cámara 
        // "mire" al centro del campo de golf
        camera.position.set(450, 450, 0);
        // Actualizamos la cámara para que esto surta efecto
        camera.update();

        // Agregamos este evento al multiplexer
        multiplexer.addProcessor(stage); // El UI tiene prioridad antes que la clase
        // Después, nuestra clase
        multiplexer.addProcessor(eventos);

        // Al mostrar esta pantalla, mostramos la información de los jugadores
        System.out.println("Iniciando juego con " + jugadores.size() + " jugadores:");

        /* --------- Configuración font --------- */

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

        /* --------- Configuración imagen fondo --------- */

        // Creamos la textura a partir de la imagen
        textureFondo = new Texture(Gdx.files.internal("fondoInicio.png"));
        // Creamos la imagen
        Image imagenFondo = new Image(textureFondo);
        // Aplicamos la opacidad a nuestra imagen de fondo
        imagenFondo.setColor(new Color(1f, 1f, 1f, 0.5f));

        /* --------- Configuración imagen punto de inicio --------- */

        // Creamos la textura a partir de la imagen
        texturePuntoDeInicio = new Texture(Gdx.files.internal("puntoDeInicio.jpg"));
        // Creamos la imagen y le cambiamos el tamaño
        imagePuntoDeInicio = new Image(texturePuntoDeInicio);
        imagePuntoDeInicio.setSize(70f, 70f);

        /* --------- Carga de la textura para las paredes --------- */
        try {
            texturePared = new Texture(Gdx.files.internal("paredes.png")); // Carga la textura de las paredes
        } catch (com.badlogic.gdx.utils.GdxRuntimeException e) {
            Gdx.app.error("jugarGolfScreen", "Error al cargar paredes.png: " + e.getMessage());
            // Puedes añadir un placeholder o manejar el error de otra forma si la imagen no se carga
        }

        /* --------- Carga de la textura para el hoyo --------- */
        try {
            texturaHoyo = new Texture(Gdx.files.internal("hoyo.png")); // Carga la textura del hoyo
        } catch (com.badlogic.gdx.utils.GdxRuntimeException e) {
            Gdx.app.error("jugarGolfScreen", "Error al cargar hoyo.png: " + e.getMessage());
            // Puedes añadir un placeholder o manejar el error de otra forma si la imagen no se carga
        }

        /* --------- Carga de la textura para el boost --------- */
        try {
            texturaBoost = new Texture(Gdx.files.internal("aceleracion.png")); // Carga la textura del boost
        } catch (com.badlogic.gdx.utils.GdxRuntimeException e) {
            Gdx.app.error("jugarGolfScreen", "Error al cargar boost.png: " + e.getMessage());
            // Puedes añadir un placeholder o manejar el error de otra forma si la imagen no se carga
        }
        /* --------- Configuración tablePrincipal --------- */

        // Creamos nuestro table principal (actor)
        tablePrincipal = new Table();
        // Llenara por completo a stage
        tablePrincipal.setFillParent(true);
        // Alineamos todo hacia arriba
        tablePrincipal.top();

        /* --------- Table para label información --------- */

        // Creamos el table para el label de información
        Table tableArriba = new Table();

        // Creamos el estilo para nuestro label
        LabelStyle labelPrincipalStyle = new LabelStyle();
        // Cambiamos su font
        labelPrincipalStyle.font = font;
        // El color va a ser blanco
        labelPrincipalStyle.fontColor = Color.WHITE;

        // Utilizaremos un label para mostrar información del juego
        infoLabel = new Label("", labelPrincipalStyle);
        infoLabel.setAlignment(Align.center);

        // Agregamos el label al table de arriba
        tableArriba.add(infoLabel).center();
        tableArriba.center();

        // Finalmente, agregamos este actor con todas sus cosas al tablePrincipal
        // Usamos una altura específica para que quede justo arriba de las paredes
        tablePrincipal.add(tableArriba).height(90).width(VIRTUAL_WIDTH).center();

        // Agregamos una nueva fila para el área de juego
        tablePrincipal.row();

        /* --------- Table para juego --------- */

        // Creamos un table vacío para el área de juego (donde están las paredes Box2D)
        Table tableJuego = new Table();
        // Este table ocupará el espacio donde están las paredes (desde y=90 hasta y=810)
        tablePrincipal.add(tableJuego).height(720).width(720).center();

        // Agregamos una nueva fila para el espacio inferior
        tablePrincipal.row();

        /* --------- Table abajo juego --------- */

        // Creamos un table vacío para el espacio inferior
        Table tableAbajo = new Table();
        // Creamos un label para mostrar el nivel de fuerza
        informacionTurno = new Label("", labelPrincipalStyle);
        informacionTurno.setAlignment(Align.center);
        // Agregamos el label a esta tabla
        tableAbajo.add(informacionTurno).center();
        tableAbajo.center();

        tablePrincipal.add(tableAbajo).height(90).width(VIRTUAL_WIDTH).center();

        /* --------- Preparación nivel --------- */

        agregarParedes();

        /* --------- Configuración capas y fondo --------- */

        // Iniciamos nuestro stack para tener "capas" de nuestros actores
        stack = new Stack();
        // De esta manera stack ocupara todo el espacio de stage, y asi todos los demás que se agreguen
        stack.setFillParent(true);

        // Añadimos primero el fondo
        stack.add(imagenFondo);

        // Añadimos el table que va a estar encima de la imagen de fondo 
        stack.add(tablePrincipal);

        /* --------- Actores --------- */

        // Agregamos por ultimo los eventos en el multiplexer
        Gdx.input.setInputProcessor(multiplexer);

        // Agregamos la clase que se va a encargar de procesar las colisiones
        mundoBox2d.setContactListener(manejoColisiones);

        // Agregamos el actor que tiene el orden de los demás
        stage.addActor(stack);
    }

    @Override
    public void render(float delta) {
        // Limpiar pantalla
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // En caso de que haya lag (que delta sea mayor que 1/60f) entonces 
        // obligamos que el paso sea 1/60 para que este sea constante
        float timeStep = Math.min(delta, 1 / 60f); // 60fps
        // Actualizamos nuestro mundo de física, en cada iteración (timeStep) se ajustan las
        // velocidades y posiciones para que estas sean mas precisas, el rendimiento depende 
        // de cuantas iteraciones para el calculo de velocidad y posición se hagan cada frame
        mundoBox2d.step(timeStep, 10, 10);

        // Aquí limpiamos las bolas (si es necesario)
        limpiarBodiesMundo(true);

        // Actualizar y dibujar stage
        stage.act(delta);
        stage.draw();
        // RENDERIZADO DEL DEBUG DE BOX2D CON ESCALADO (actualizar y dibujar con escalado)
        // Dibujamos las formas de colisión de Box2D para debug, .cpy() crea una copia de la matriz 
        // de la cámara para no modificar la original, .scl(100f) escala la matriz × 100 para convertir 
        // metros de Box2D a píxeles de pantalla
        debugRenderer.render(
                mundoBox2d, // Mundo Box2D
                camera.combined // Matriz de transformación
                        .cpy() // Creamos una copia (no modifica la original)
                        .scl(100f)); // Escalar x100 para metro -> pixel

        // Verificamos el nivel actual 
        gestionarNivel(jugadoresTerminaron());

        // Bandera para saber si la pelota se esta moviendo, verificando si para empezar el jugador tiene bola
        boolean pelotaDetenida = false;
        if (jugadores.get(turnoActual).getBolaJugador() != null) {
            pelotaDetenida = pelotaDetenida(jugadores.get(turnoActual).getBolaJugador());
        }

        // Verificamos si el turno actual tiene bola y si no ha terminado el hoyo, de no ser asi, entonces esperamos a que la coloque
        if (jugadores.get(turnoActual).getBolaJugador() == null && !jugadores.get(turnoActual).isHoyoTerminado()) {
            infoLabel.setText("Colocar bola para: " + jugadores.get(turnoActual).getNombre());
            // Limpiamos el label
            informacionTurno.setText("");
        } else {

            // Le agregamos un formato a la fuerza para solo mostrar 2 decimales
            String numeroConFormato = String.format("%.2f", eventos.getFuerza());

            // Mostramos la información una vez que el usuario haya colocado la bola
            informacionTurno.setText(
                    "Fuerza: " + numeroConFormato + "%\t\tStrokes: "
                            + jugadores.get(turnoActual).getStrokesActuales());

            // Si el jugador tiene bola y le toca golpear
            if (jugadores.get(turnoActual).getBolaJugador() != null
                    && jugadores.get(turnoActual).isPuedeGolpear() == true) {

                // Mostramos información sobre el turno
                infoLabel.setText("Turno de: " + jugadores.get(turnoActual).getNombre());

                // Llamamos a nuestro método para dibujar la linea de dirección
                dibujarLineaDireccion();

                // Verificamos que tecla esta pulsada
                if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                    eventos.setAngulo(0);
                } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                    eventos.setAngulo(1);
                } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                    eventos.setFuerza(0);
                } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                    eventos.setFuerza(1);
                }

            }
            // Verificamos si el turno ha terminado:
            // - El jugador tiene bola, ya no puede golpear y la pelota se ha parado
            // - O si el jugador ya termino el hoyo
            else if (jugadores.get(turnoActual).getBolaJugador() != null
                    && !jugadores.get(turnoActual).isPuedeGolpear() && pelotaDetenida
                    || jugadores.get(turnoActual).isHoyoTerminado()) {

                System.out.println("Pelota detenida para: " + jugadores.get(turnoActual).getNombre());

                // Verificamos si podemos pasar al siguiente jugador, de no ser asi, entonces reiniciamos
                // nuestro contador
                if (turnoActual + 1 == jugadores.size()) {
                    turnoActual = 0;
                } else {
                    turnoActual++;
                }

                // Este jugador puede volver a golpear
                jugadores.get(turnoActual).setPuedeGolpear(true);
                // Actualizamos en nuestra clase eventos
                eventos.setJugadorActual(turnoActual);

                System.out.println("Cambiando de turno, ahora sigue: " + jugadores.get(turnoActual).getNombre());
            }
        }

    }

    @Override
    public void resize(int width, int height) {
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
        // Limpiamos todas las pelotas de los jugadores
        for (Jugador jugador : jugadores) {
            mundoBox2d.destroyBody(jugador.getBolaJugador());
            jugador.setBolaJugador(null);
        }

        // Limpiamos los bodies temporales que uso el nivel
        for (Map.Entry<Body, Boolean> e : hashMapBodiesTemporales.entrySet()) {
            mundoBox2d.destroyBody(e.getKey());
        }

        // Limpiamos todas las paredes
        for (Body pared : arrayListParedes) {
            mundoBox2d.destroyBody(pared);
        }

        // Liberar recursos
        stage.dispose();
        mundoBox2d.dispose();
        debugRenderer.dispose();
        textureFondo.dispose();
        texturePuntoDeInicio.dispose();
        font.dispose();
        if (texturePared != null) {
            texturePared.dispose();
        }
        if (texturaHoyo != null) {
            texturaHoyo.dispose();
        }
        Gdx.input.setInputProcessor(null);
        shapeRenderer.dispose();

        hashMapBodiesTemporales.clear();
        arrayListParedes.clear();
    }

    private void agregarParedes() {
        // Half thickness in meters
        float halfWallThickness_m = (Grosor_Muros_PX / 2) * PIXEL_A_METRO;
        float halfFieldWidth_m = 360 * PIXEL_A_METRO; // Half width of the playable field
        float halfFieldHeight_m = 360 * PIXEL_A_METRO; // Half height of the playable field

        // Central coordinates for the field relative to the screen (450, 450)
        float centerX_px = 450;
        float centerY_px = 450;

        /* --------- Pared 1 (Inferior) --------- */

        // Definimos un Body estático para la pared inferior
        BodyDef pared1BodyDef = new BodyDef();
        // Posición central de la pared en el mundo Box2D (metros)
        pared1BodyDef.position.set(
                centerX_px * PIXEL_A_METRO, // X-centro del campo
                (90 * PIXEL_A_METRO) + halfWallThickness_m // Y-borde inferior del campo + medio grosor de pared
        );
        Body pared1Body = mundoBox2d.createBody(pared1BodyDef);
        arrayListParedes.add(pared1Body);

        // Forma rectangular para la pared con un grosor
        PolygonShape pared1Shape = new PolygonShape();
        pared1Shape.setAsBox(halfFieldWidth_m, halfWallThickness_m); // Ancho total del campo, grosor de pared

        FixtureDef paredFixtureDef = new FixtureDef();
        paredFixtureDef.shape = pared1Shape;
        paredFixtureDef.restitution = 1f;
        paredFixtureDef.density = 0f;
        paredFixtureDef.friction = 0.2f;
        paredFixtureDef.filter.categoryBits = manejoEventos.CATEGORIA_PARED;
        paredFixtureDef.filter.maskBits = manejoEventos.CATEGORIA_BOLA;
        pared1Body.createFixture(paredFixtureDef);
        pared1Shape.dispose();

        // Representación visual de la Pared 1
        Image imagePared1 = new Image(texturePared);
        imagePared1.setSize(halfFieldWidth_m * 2 / PIXEL_A_METRO, halfWallThickness_m * 2 / PIXEL_A_METRO); // Ancho real, alto real
        imagePared1.setPosition(
                (pared1Body.getPosition().x / PIXEL_A_METRO) - imagePared1.getWidth() / 2, // Posición X (ajuste de centro a esquina)
                (pared1Body.getPosition().y / PIXEL_A_METRO) - imagePared1.getHeight() / 2 // Posición Y (ajuste de centro a esquina)
        );
        stage.addActor(imagePared1);
        pared1Body.setUserData(imagePared1); // Asociar imagen con el cuerpo Box2D

        /* --------- Pared 2 (Superior) --------- */

        BodyDef pared2BodyDef = new BodyDef();
        pared2BodyDef.position.set(
                centerX_px * PIXEL_A_METRO,
                (810 * PIXEL_A_METRO) - halfWallThickness_m // Y-borde superior del campo - medio grosor de pared
        );
        Body pared2Body = mundoBox2d.createBody(pared2BodyDef);
        arrayListParedes.add(pared2Body);

        PolygonShape pared2Shape = new PolygonShape();
        pared2Shape.setAsBox(halfFieldWidth_m, halfWallThickness_m);
        paredFixtureDef.shape = pared2Shape;
        pared2Body.createFixture(paredFixtureDef);
        pared2Shape.dispose();

        // Representación visual de la Pared 2
        Image imagePared2 = new Image(texturePared);
        imagePared2.setSize(halfFieldWidth_m * 2 / PIXEL_A_METRO, halfWallThickness_m * 2 / PIXEL_A_METRO);
        imagePared2.setPosition(
                (pared2Body.getPosition().x / PIXEL_A_METRO) - imagePared2.getWidth() / 2,
                (pared2Body.getPosition().y / PIXEL_A_METRO) - imagePared2.getHeight() / 2);
        stage.addActor(imagePared2);
        pared2Body.setUserData(imagePared2);

        /* --------- Pared 3 (Izquierda) --------- */

        BodyDef pared3BodyDef = new BodyDef();
        pared3BodyDef.position.set(
                (90 * PIXEL_A_METRO) + halfWallThickness_m, // X-borde izquierdo del campo + medio grosor de pared
                centerY_px * PIXEL_A_METRO);
        Body pared3Body = mundoBox2d.createBody(pared3BodyDef);
        arrayListParedes.add(pared3Body);

        PolygonShape pared3Shape = new PolygonShape();
        pared3Shape.setAsBox(halfWallThickness_m, halfFieldHeight_m); // Grosor de pared, alto total del campo
        paredFixtureDef.shape = pared3Shape;
        pared3Body.createFixture(paredFixtureDef);
        pared3Shape.dispose();

        // Representación visual de la Pared 3
        Image imagePared3 = new Image(texturePared);
        imagePared3.setSize(halfWallThickness_m * 2 / PIXEL_A_METRO, halfFieldHeight_m * 2 / PIXEL_A_METRO);
        imagePared3.setPosition(
                (pared3Body.getPosition().x / PIXEL_A_METRO) - imagePared3.getWidth() / 2,
                (pared3Body.getPosition().y / PIXEL_A_METRO) - imagePared3.getHeight() / 2);
        stage.addActor(imagePared3);
        pared3Body.setUserData(imagePared3);

        /* --------- Pared 4 (Derecha) --------- */

        BodyDef pared4BodyDef = new BodyDef();
        pared4BodyDef.position.set(
                (810 * PIXEL_A_METRO) - halfWallThickness_m, // X-borde derecho del campo - medio grosor de pared
                centerY_px * PIXEL_A_METRO);
        Body pared4Body = mundoBox2d.createBody(pared4BodyDef);
        arrayListParedes.add(pared4Body);

        PolygonShape pared4Shape = new PolygonShape();
        pared4Shape.setAsBox(halfWallThickness_m, halfFieldHeight_m);
        paredFixtureDef.shape = pared4Shape;
        pared4Body.createFixture(paredFixtureDef);
        pared4Shape.dispose();

        // Representación visual de la Pared 4
        Image imagePared4 = new Image(texturePared);
        imagePared4.setSize(halfWallThickness_m * 2 / PIXEL_A_METRO, halfFieldHeight_m * 2 / PIXEL_A_METRO);
        imagePared4.setPosition(
                (pared4Body.getPosition().x / PIXEL_A_METRO) - imagePared4.getWidth() / 2,
                (pared4Body.getPosition().y / PIXEL_A_METRO) - imagePared4.getHeight() / 2);
        stage.addActor(imagePared4);
        pared4Body.setUserData(imagePared4);
    }

    private void gestionarNivel(boolean todosTerminaron) {

        // Si todos los jugadores ya terminaron
        if (todosTerminaron) {
            System.out.println("Los jugadores han terminado el nivel, mostrando tarjeta de puntos");
            game.setScreen(new tarjetaPuntosScreen(game, jugadores, nivelActual));
        }

        // Si no se ha cargado el nivel aun, entonces lo hacemos.
        // Solo carga un nuevo nivel si la pantalla de puntuaciones NO se está mostrando,
        // y si el nivel actual ha avanzado (lo que ocurre cuando ScoreBoardScreen te devuelve aquí).
        if (!nivelCargado) {
            // Según el nivel en el que estemos
            switch (nivelActual) {
                case 1:
                    System.out.println("Creando el primer nivel");
                    mostrarInformacionJugadores();
                    hashMapBodiesTemporales = nivel1Golf.crearNivel(stage, mundoBox2d, imagePuntoDeInicio,
                            hashMapBodiesTemporales, texturePared, texturaHoyo);
                    nivelCargado = true;
                    break;
                case 2:
                    System.out.println("Creando el segundo nivel");
                    mostrarInformacionJugadores();
                    hashMapBodiesTemporales = nivel2Golf.crearNivel(stage, mundoBox2d, imagePuntoDeInicio,
                            hashMapBodiesTemporales, texturePared, texturaHoyo);
                    nivelCargado = true;
                    break;
                case 3:
                    System.out.println("Creando el tercer nivel");
                    mostrarInformacionJugadores();
                    hashMapBodiesTemporales = nivel3Golf.crearNivel(stage, mundoBox2d, imagePuntoDeInicio,
                            hashMapBodiesTemporales, texturePared, texturaHoyo);
                    nivelCargado = true;
                    break;

                case 4:
                    System.out.println("Creando el cuarto nivel");
                    mostrarInformacionJugadores();
                    hashMapBodiesTemporales = nivel4Golf.crearNivel(stage, mundoBox2d, imagePuntoDeInicio,
                            hashMapBodiesTemporales, texturePared, texturaHoyo);
                    nivelCargado = true;
                    break;
                case 5:
                    System.out.println("Creando el quinto nivel");
                    mostrarInformacionJugadores();
                    hashMapBodiesTemporales = nivel5Golf.crearNivel(stage, mundoBox2d, imagePuntoDeInicio,
                            hashMapBodiesTemporales, texturePared, texturaHoyo);
                    nivelCargado = true;
                    break;
                case 6:
                    System.out.println("Creando el sexto nivel");
                    mostrarInformacionJugadores();
                    hashMapBodiesTemporales = nivel6Golf.crearNivel(stage, mundoBox2d, imagePuntoDeInicio,
                            hashMapBodiesTemporales, texturePared, texturaHoyo);
                    nivelCargado = true;
                    break;
                case 7:
                    System.out.println("Creando el séptimo nivel");
                    mostrarInformacionJugadores();
                    hashMapBodiesTemporales = nivel7Golf.crearNivel(stage, mundoBox2d, imagePuntoDeInicio,
                            hashMapBodiesTemporales, texturePared, texturaHoyo);
                    nivelCargado = true;
                    break;
                case 8:
                    System.out.println("Creando el octavo nivel");
                    mostrarInformacionJugadores();
                    hashMapBodiesTemporales = nivel8Golf.crearNivel(stage, mundoBox2d, imagePuntoDeInicio,
                            hashMapBodiesTemporales, texturePared, texturaHoyo);
                    nivelCargado = true;
                    break;
                case 9:
                    System.out.println("Creando el noveno nivel");
                    mostrarInformacionJugadores();
                    hashMapBodiesTemporales = nivel9Golf.crearNivel(stage, mundoBox2d, imagePuntoDeInicio,
                            hashMapBodiesTemporales, texturePared, texturaHoyo);
                    nivelCargado = true;
                    break;
                case 10:
                    System.out.println("Creando el décimo nivel");
                    mostrarInformacionJugadores();
                    hashMapBodiesTemporales = nivel10Golf.crearNivel(stage, mundoBox2d, imagePuntoDeInicio,
                            hashMapBodiesTemporales, texturePared, texturaHoyo);
                    nivelCargado = true;
                    break;
                case 11:
                    System.out.println("Creando el undécimo nivel");
                    mostrarInformacionJugadores();
                    hashMapBodiesTemporales = nivel11Golf.crearNivel(stage, mundoBox2d, imagePuntoDeInicio,
                            hashMapBodiesTemporales, texturePared, texturaHoyo);
                    nivelCargado = true;
                    break;
                case 12:
                    System.out.println("Creando el duodécimo nivel");
                    mostrarInformacionJugadores();
                    hashMapBodiesTemporales = nivel12Golf.crearNivel(stage, mundoBox2d, imagePuntoDeInicio,
                            hashMapBodiesTemporales, texturePared, texturaHoyo);
                    nivelCargado = true;
                    break;
                case 13:
                    System.out.println("Creando el decimotercer nivel");
                    mostrarInformacionJugadores();
                    hashMapBodiesTemporales = nivel13Golf.crearNivel(stage, mundoBox2d, imagePuntoDeInicio,
                            hashMapBodiesTemporales, texturePared, texturaHoyo);
                    nivelCargado = true;
                    break;
                case 14:
                    System.out.println("Creando el decimocuarto nivel");
                    mostrarInformacionJugadores();
                    hashMapBodiesTemporales = nivel14Golf.crearNivel(stage, mundoBox2d, imagePuntoDeInicio,
                            hashMapBodiesTemporales, texturePared, texturaHoyo);
                    nivelCargado = true;
                    break;
                case 15:
                    System.out.println("Creando el decimoquinto nivel");
                    mostrarInformacionJugadores();
                    hashMapBodiesTemporales = nivel15Golf.crearNivel(stage, mundoBox2d, imagePuntoDeInicio,
                            hashMapBodiesTemporales, texturePared, texturaHoyo, texturaBoost);
                    nivelCargado = true;
                    break;
                case 16:
                    System.out.println("Creando el decimosexto nivel");
                    mostrarInformacionJugadores();
                    hashMapBodiesTemporales = nivel16Golf.crearNivel(stage, mundoBox2d, imagePuntoDeInicio,
                            hashMapBodiesTemporales, texturePared, texturaHoyo, texturaBoost);
                    nivelCargado = true;
                    break;
                case 17:
                    System.out.println("Creando el decimoséptimo nivel");
                    mostrarInformacionJugadores();
                    hashMapBodiesTemporales = nivel17Golf.crearNivel(stage, mundoBox2d, imagePuntoDeInicio,
                            hashMapBodiesTemporales, texturePared, texturaHoyo, texturaBoost);
                    nivelCargado = true;
                    break;
                case 18:
                    System.out.println("Creando el decimoctavo nivel");
                    mostrarInformacionJugadores();
                    hashMapBodiesTemporales = nivel18Golf.crearNivel(stage, mundoBox2d, imagePuntoDeInicio,
                            hashMapBodiesTemporales, texturePared, texturaHoyo, texturaBoost);
                    nivelCargado = true;
                    break;
                default:
                    System.out.println("Nivel no reconocido, no se cargará ningún nivel.");
                    // Si el nivel no es reconocido, no hacemos nada
            }
        }
    }

    // Obtenemos actualizaciones de la bola para saber si esta esta detenida
    private boolean pelotaDetenida(Body pelota) {
        // Obtenemos la magnitud de la velocidad
        Vector2 velocidad = pelota.getLinearVelocity();
        float magnitudVelocidad = velocidad.len();

        // Se detectara que esta parada si la velocidad cumple 0.01m/s
        return magnitudVelocidad < 0.01f;
    }

    // Dibujamos una linea para mostrar la dirección del golpe
    private void dibujarLineaDireccion() {

        // Obtenemos la bola del jugador
        Body bola = jugadores.get(turnoActual).getBolaJugador();

        // Obtenemos la posición de la bola (convertida en metros)
        Vector2 posicionBola = bola.getPosition();
        float pelotaX = posicionBola.x / PIXEL_A_METRO;
        float pelotaY = posicionBola.y / PIXEL_A_METRO;

        // Obtenemos la fuerza y dirección del golpe actual
        float anguloDireccion = eventos.getAnguloDireccion();
        float fuerza = eventos.getFuerza();

        // Longitud de la linea basada en la fuerza del golpe (mínimo 0.5 y máximo 5 pixeles)
        float longitudLinea = 0.5f + (fuerza * 4.5f);

        // Calculamos las coordenadas donde termina la linea
        float lineaFinX = pelotaX + MathUtils.cosDeg(anguloDireccion) * longitudLinea;
        float lineaFinY = pelotaY + MathUtils.sinDeg(anguloDireccion) * longitudLinea;

        // Configuramos el render para usar la matriz de la cámara, esto para que utilice las coordenadas
        // de nuestra matriz de camera
        shapeRenderer.setProjectionMatrix(camera.combined);
        // Preparamos el shapeRenderer para dibujar (una linea)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        // Le ponemos un color a la linea (según el color del jugador)
        Color colorJugador = jugadores.get(turnoActual).getColorBola();
        shapeRenderer.setColor(colorJugador);

        // Ahora si dibujamos la linea
        shapeRenderer.line(pelotaX, pelotaY, lineaFinX, lineaFinY);

        // Terminamos de utilizar shapeRenderer
        shapeRenderer.end();
    }

    // Método para mostrar la información de los jugadores cada hoyo
    private void mostrarInformacionJugadores() {
        for (Jugador jugador : jugadores) {
            System.out.println(jugador);
        }
    }

    // Limpiamos los bodies del nivel según su bandera (true - bola | false - obstáculos/hoyo...)
    private void limpiarBodiesMundo(boolean tipoBody) {
        // Creamos un iterator para poder recorrer nuestro hashMap de manera segura
        Iterator<Map.Entry<Body, Boolean>> iterator = hashMapBodiesTemporales.entrySet().iterator();
        while (iterator.hasNext()) { // Mientras que haya elementos 
            // Obtenemos este elemento y avanzamos al siguiente
            Map.Entry<Body, Boolean> temporal = iterator.next();

            // Solo eliminamos los bodies que correspondan a la bandera
            if (temporal.getValue() == tipoBody) {
                mundoBox2d.destroyBody(temporal.getKey()); // La eliminamos del mundo
                iterator.remove(); // Lo eliminamos del hashMap de manera segura (para que de esta manera
                // no obtengamos la excepción ConcurrentModificationException https://www.shorturl.at/shortener.php)
            }
        }
    }

    // Método para verificar si todos los jugadores han terminado el hoyo
    private boolean jugadoresTerminaron() {
        for (Jugador jugador : jugadores) {
            if (!jugador.isHoyoTerminado()) {
                return false;
            }
        }
        return true;
    }
}