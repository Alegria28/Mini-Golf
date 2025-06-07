package com.minigolf;

import java.util.ArrayList;

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

/**
 * Pantalla principal del juego de golf donde se desarrolla la partida
 */
public class jugarGolfScreen implements Screen {

    // Factor de conversion para el mundo de Box2D, donde 100px = 1m 
    private final float PIXEL_A_METRO = 0.01f;

    // Atributos
    OrthographicCamera camera;
    private Stage stage;
    Texture textureFondo;
    private Stack stack;
    private Table tablePrincipal;
    private Label infoLabel;
    private Label fuerzaLabel;
    private BitmapFont font;

    private final MiniGolfMain game;
    private ArrayList<Jugador> jugadores;

    // Dimensiones virtuales
    private final float VIRTUAL_WIDTH = 900;
    private final float VIRTUAL_HEIGHT = 900;

    // Variable para tener un control de turno durante el juego
    private int turnoActual = 0;
    // Instancia de la clase que va a manejar los eventos
    private manejoEventos eventos;

    /* --------- Atributos motor físico Box2D --------- */

    // Creamos nuestro mundo sin gravedad
    World mundoBox2d = new World(new Vector2(0, 0), true);
    // Para poder ver los elementos en nuestro mundo de Box2D
    Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
    // Para poder dibujar figuras simples
    ShapeRenderer shapeRenderer;

    // Constructor de la clase
    public jugarGolfScreen(MiniGolfMain game, ArrayList<Jugador> jugadores) {
        this.game = game;
        this.jugadores = jugadores;
        eventos = new manejoEventos(jugadores, mundoBox2d);
    }

    @Override
    public void show() {

        // Creamos un InputMultiplexer para manejar varios eventos
        InputMultiplexer multiplexer = new InputMultiplexer();
        // Iniciamos la instancia de shapeRenderer
        shapeRenderer = new ShapeRenderer();

        /* --------- Configuración inicial stage y eventos --------- */
        camera = new OrthographicCamera();
        Viewport viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);

        // Creamos el stage
        stage = new Stage(viewport);

        // Agregamos este evento al multiplexer
        multiplexer.addProcessor(stage); // El UI tiene prioridad antes que la clase
        // Después, nuestra clase
        multiplexer.addProcessor(eventos);

        // Al mostrar esta pantalla, mostramos la información de los jugadores
        System.out.println("Iniciando juego con " + jugadores.size() + " jugadores:");
        for (Jugador jugador : jugadores) {
            System.out.println(jugador);
        }

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
        fuerzaLabel = new Label("", labelPrincipalStyle);
        fuerzaLabel.setAlignment(Align.center);
        // Agregamos el label a esta tabla
        tableAbajo.add(fuerzaLabel).center();
        tableAbajo.center();

        tablePrincipal.add(tableAbajo).height(90).width(VIRTUAL_WIDTH).center();

        /* --------- Pared 1 --------- */

        // Definimos un Body lo cual es un objeto dentro del mundo de Box2D, por defecto es estático, 
        // significando que no se mueve 
        BodyDef pared1BodyDef = new BodyDef();
        // Establecemos su posición en el mundo (pared inferior), es importante recalcar que Box2D si 
        // toma el centro del Body como posición, y no una esquina como LibGDX
        pared1BodyDef.position.set(450 * PIXEL_A_METRO, 90 * PIXEL_A_METRO);
        // Creamos un Body a partir de la definición y lo agregamos a nuestro mundo
        Body pared1Body = mundoBox2d.createBody(pared1BodyDef);

        // Creamos un polígono en general
        PolygonShape pared1Shape = new PolygonShape();
        // Establecemos el polígono con la forma de un rectángulo, el cual tiene como 
        // MITAD de un lado X 360 y en Y es nulo, por lo que es muy fino o invisible
        pared1Shape.setAsBox(360 * PIXEL_A_METRO, 0);

        // Creamos una FixtureDef para definir las propiedades físicas de la pared
        FixtureDef paredFixtureDef = new FixtureDef();
        paredFixtureDef.friction = 0.2f;
        paredFixtureDef.density = 0f;
        paredFixtureDef.restitution = 1f; // Rebote
        // Ponemos la forma creada 
        paredFixtureDef.shape = pared1Shape;

        // Creamos la fixture y la unimos al cuerpo de la pared
        pared1Body.createFixture(paredFixtureDef);
        // Liberamos el polígono
        pared1Shape.dispose();

        /* --------- Pared 2 --------- */

        // Definimos un Body lo cual es un objeto dentro del mundo de Box2D, por defecto es estático,
        // significando que no se mueve
        BodyDef pared2BodyDef = new BodyDef();
        // Establecemos su posición en el mundo (pared superior), Box2D toma el centro del Body como posición
        pared2BodyDef.position.set(450 * PIXEL_A_METRO, 810 * PIXEL_A_METRO);
        // Creamos un Body a partir de la definición y lo agregamos a nuestro mundo
        Body pared2Body = mundoBox2d.createBody(pared2BodyDef);

        // Creamos un polígono en general
        PolygonShape pared2Shape = new PolygonShape();
        // Establecemos el polígono con la forma de un rectángulo, el cual tiene como
        // MITAD de un lado X 360 y en Y es nulo, por lo que es muy fino o invisible
        pared2Shape.setAsBox(360 * PIXEL_A_METRO, 0);

        // Ponemos la forma creada 
        paredFixtureDef.shape = pared2Shape;
        // Creamos la fixture y la unimos al cuerpo de la pared
        pared2Body.createFixture(paredFixtureDef);
        // Liberamos el polígono
        pared2Shape.dispose();

        /* --------- Pared 3 --------- */

        // Definimos un Body lo cual es un objeto dentro del mundo de Box2D, por defecto es estático,
        // significando que no se mueve
        BodyDef pared3BodyDef = new BodyDef();
        // Establecemos su posición en el mundo (pared izquierda), Box2D toma el centro del Body como posición
        pared3BodyDef.position.set(90 * PIXEL_A_METRO, 450 * PIXEL_A_METRO);
        // Creamos un Body a partir de la definición y lo agregamos a nuestro mundo
        Body pared3Body = mundoBox2d.createBody(pared3BodyDef);

        // Creamos un polígono en general
        PolygonShape pared3Shape = new PolygonShape();
        // Establecemos el polígono con la forma de un rectángulo, el cual tiene como
        // MITAD de un lado Y 360 y en X es nulo, por lo que es muy fino o invisible
        pared3Shape.setAsBox(0, 360 * PIXEL_A_METRO);

        // Ponemos la forma creada 
        paredFixtureDef.shape = pared3Shape;
        // Creamos la fixture y la unimos al cuerpo de la pared
        pared3Body.createFixture(paredFixtureDef);
        // Liberamos el polígono
        pared3Shape.dispose();

        /* --------- Pared 4 --------- */

        // Definimos un Body lo cual es un objeto dentro del mundo de Box2D, por defecto es estático,
        // significando que no se mueve
        BodyDef pared4BodyDef = new BodyDef();
        // Establecemos su posición en el mundo (pared derecha), Box2D toma el centro del Body como posición
        pared4BodyDef.position.set(810 * PIXEL_A_METRO, 450 * PIXEL_A_METRO);
        // Creamos un Body a partir de la definición y lo agregamos a nuestro mundo
        Body pared4Body = mundoBox2d.createBody(pared4BodyDef);

        // Creamos un polígono en general
        PolygonShape pared4Shape = new PolygonShape();
        // Establecemos el polígono con la forma de un rectángulo, el cual tiene como
        // MITAD de un lado Y 360 y en X es nulo, por lo que es muy fino o invisible
        pared4Shape.setAsBox(0, 360 * PIXEL_A_METRO);

        // Ponemos la forma creada 
        paredFixtureDef.shape = pared4Shape;
        // Creamos la fixture y la unimos al cuerpo de la pared
        pared4Body.createFixture(paredFixtureDef);
        // Liberamos el polígono
        pared4Shape.dispose();

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

        // Agregamos el actor que tiene el orden de los demás
        stage.addActor(stack);
    }

    @Override
    public void render(float delta) {
        // Limpiar pantalla
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Actualizamos nuestro mundo de física
        mundoBox2d.step(1 / 60f, 6, 2);

        // Actualizar y dibujar stage
        stage.act(delta);
        stage.draw();
        // Dibujamos nuestro mundo
        debugRenderer.render(mundoBox2d, camera.combined);

        // Ponemos la posición de la cámara al centro del mundo virtual (450px,450px), esto para que la cámara 
        // "mire" al centro del campo de golf
        camera.position.set(450, 450, 0);
        // Actualizamos la cámara para que esto surta efecto
        camera.update();

        // RENDERIZADO DEL DEBUG DE BOX2D CON ESCALADO
        // Dibujamos las formas de colisión de Box2D para debug, .cpy() crea una copia de la matriz 
        // de la cámara para no modificar la original, .scl(100f) escala la matriz × 100 para convertir 
        // metros de Box2D a píxeles de pantalla
        debugRenderer.render(
                mundoBox2d, // Mundo Box2D
                camera.combined // Matriz de transformación
                        .cpy() // Creamos una copia (no modifica la original)
                        .scl(100f)); // Escalar x100 para metro -> pixel

        // Bandera para saber si la pelota se esta moviendo, verificando si para empezar el jugador tiene bola
        boolean pelotaDetenida = false;
        if (jugadores.get(turnoActual).getBolaJugador() != null) {
            pelotaDetenida = pelotaDetenida(jugadores.get(turnoActual).getBolaJugador());
        }

        // Verificamos si el turno actual tiene bola, de no ser asi, entonces esperamos a que la coloque
        if (jugadores.get(turnoActual).getBolaJugador() == null) {
            infoLabel.setText("Colocar bola para: " + jugadores.get(turnoActual).getNombre());
        }
        // Si el jugador tiene bola y le toca golpear
        else if (jugadores.get(turnoActual).getBolaJugador() != null
                && jugadores.get(turnoActual).getPuedeGolpear() == true) {

            infoLabel.setText("Turno de: " + jugadores.get(turnoActual).getNombre());

            // Llamamos a nuestro método para dibujar la linea de dirección
            dibujarLineaDireccion();

            // Verificamos que tecla esta pulsada
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                System.out.println("Tecla LEFT");
                eventos.actualizarAngulo(0);
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                System.out.println("Tecla RIGHT");
                eventos.actualizarAngulo(1);
            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                System.out.println("Tecla DOWN");
                eventos.actualizarFuerza(0);
            } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                System.out.println("Tecla UP");
                eventos.actualizarFuerza(1);
            }

            // Mostramos información sobre el tiro
            fuerzaLabel.setText("Fuerza: " + String.valueOf(eventos.obtenerFuerza()) + "%");
        }
        // Verificamos si el turno ha terminado:
        // - El jugador tiene pelota
        // - Ya no puede golpear
        // - Y la pelota se ha parado
        else if (jugadores.get(turnoActual).getBolaJugador() != null
                && jugadores.get(turnoActual).getPuedeGolpear() == false && pelotaDetenida) {

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
            if (jugador.getBolaJugador() != null) {
                mundoBox2d.destroyBody(jugador.getBolaJugador());
                jugador.setBolaJugador(null);
            }
        }

        // Liberar recursos
        stage.dispose();
        mundoBox2d.dispose();
        debugRenderer.dispose();
        textureFondo.dispose();
        font.dispose();
        Gdx.input.setInputProcessor(null);
        shapeRenderer.dispose();
    }

    // Obtenemos actualizaciones de la bola para saber si esta esta detenida
    private boolean pelotaDetenida(Body pelota) {
        // Obtenemos la magnitud de la velocidad
        Vector2 velocidad = pelota.getLinearVelocity();
        float magnitudVelocidad = velocidad.len();

        // Se detectara que esta parada si la velocidad cumple 0.05m/s
        return magnitudVelocidad < 0.1f;
    }
    
    // Dibujamos una linea para mostrar la dirección del golpe
    private void dibujarLineaDireccion(){

        // Obtenemos la bola del jugador
        Body bola = jugadores.get(turnoActual).getBolaJugador();

        // Obtenemos la posición de la bola (convertida en metros)
        Vector2 posicionBola = bola.getPosition();
        float pelotaX = posicionBola.x / PIXEL_A_METRO;
        float pelotaY = posicionBola.y / PIXEL_A_METRO;

        // Obtenemos la fuerza y dirección del golpe actual
        float anguloDireccion = eventos.obtenerAnguloDireccion();
        float fuerza = eventos.obtenerFuerza();

        // Longitud de la linea basada en la fuerza del golpe (mínimo 0.5 y máximo 5 pixeles)
        float longitudLinea = 0.5f + (fuerza * 4.5f);

        // Calculamos las coordenadas donde termina la linea
        float lineaFinX = pelotaX + MathUtils.cos(anguloDireccion) * longitudLinea;
        float lineaFinY = pelotaY + MathUtils.sin(anguloDireccion) * longitudLinea;

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
}