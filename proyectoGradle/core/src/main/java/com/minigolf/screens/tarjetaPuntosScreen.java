package com.minigolf.screens;

import java.util.ArrayList;
import java.util.Comparator;

// Importamos los recursos necesarios para la clase
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

// Importamos las clases necesarias de nuestro proyecto
import com.minigolf.MiniGolfMain;
import com.minigolf.models.Jugador;

/**
 * Las clases que implementan Screen son diferentes escenas que Game puede mostrar, por lo que son parte del ciclo de vida del juego (ver {@link MiniGolfMain}).
 */
public class tarjetaPuntosScreen implements Screen {

    // Atributos
    private final MiniGolfMain game;
    private ArrayList<Jugador> jugadores;
    private Stage stage;
    private Viewport viewport;
    private BitmapFont fontTitulo;
    private BitmapFont fontEncabezado;
    private BitmapFont fontContenido;

    // Dimensiones virtuales (buena practica para el diseño)
    private final float VIRTUAL_WIDTH = 900;
    private final float VIRTUAL_HEIGHT = 900;

    // Número máximo de niveles que se pueden mostrar en la tabla
    private final int MAX_NIVELES_MOSTRADOS = 18;
    private int nivelActualDelJuego;

    // Texturas para el fondo y botones
    private Texture texturaFondo;
    private Texture texturaBoton;

    // Constructor de la clase
    public tarjetaPuntosScreen(MiniGolfMain game, ArrayList<Jugador> jugadores, int nivelActualDelJuego) {
        this.game = game;
        this.jugadores = jugadores;
        // Ordenamos los jugadores por puntaje total para mostrarlos en la tabla
        this.jugadores.sort(Comparator.comparingInt(Jugador::getPuntajeTotal));
        this.nivelActualDelJuego = nivelActualDelJuego;
    }

    @Override
    public void show() {

        /* --------- Configuración inicial viewport y stage --------- */

        // Creamos el viewport con las dimensiones virtuales
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        // Creamos nuestro stage con el tamaño del viewport especificado
        stage = new Stage(viewport);
        // Configuramos el procesador de entrada para que stage pueda recibir eventos
        Gdx.input.setInputProcessor(stage);

        /* --------- Configuración de fonts --------- */

        // Cargamos la tipografía que se utilizara
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Inter-Variable.ttf"));

        // Configuramos el font para el título
        FreeTypeFontParameter paramTitulo = new FreeTypeFontParameter();
        paramTitulo.size = 48;
        paramTitulo.color = Color.WHITE;
        fontTitulo = fontGenerator.generateFont(paramTitulo);

        // Configuramos el font para los encabezados
        FreeTypeFontParameter paramEncabezado = new FreeTypeFontParameter();
        paramEncabezado.size = 32;
        paramEncabezado.color = Color.BLACK;
        fontEncabezado = fontGenerator.generateFont(paramEncabezado);

        // Configuramos el font para el contenido
        FreeTypeFontParameter paramContenido = new FreeTypeFontParameter();
        paramContenido.size = 28;
        paramContenido.color = Color.BLACK;
        fontContenido = fontGenerator.generateFont(paramContenido);

        // Ya que ya no lo vamos a ocupar, podemos liberarlo
        fontGenerator.dispose();

        /* --------- Configuración fondo redondeado para la tabla de puntuaciones --------- */

        // Creamos un Pixmap para crear una textura con esquinas redondeadas
        Pixmap pixmapRedondeado = new Pixmap(100, 100, Pixmap.Format.RGBA8888);
        pixmapRedondeado.setColor(Color.WHITE);
        int radioEsquina = 15;
        // Creamos las formas para las esquinas redondeadas
        pixmapRedondeado.fillRectangle(0, radioEsquina, pixmapRedondeado.getWidth(),
                pixmapRedondeado.getHeight() - 2 * radioEsquina);
        pixmapRedondeado.fillRectangle(radioEsquina, 0, pixmapRedondeado.getWidth() - 2 * radioEsquina,
                pixmapRedondeado.getHeight());
        pixmapRedondeado.fillCircle(radioEsquina, radioEsquina, radioEsquina);
        pixmapRedondeado.fillCircle(pixmapRedondeado.getWidth() - radioEsquina, radioEsquina, radioEsquina);
        pixmapRedondeado.fillCircle(radioEsquina, pixmapRedondeado.getHeight() - radioEsquina, radioEsquina);
        pixmapRedondeado.fillCircle(pixmapRedondeado.getWidth() - radioEsquina,
                pixmapRedondeado.getHeight() - radioEsquina,
                radioEsquina);

        // Creamos la textura del fondo y liberamos el pixmap
        texturaFondo = new Texture(pixmapRedondeado);
        pixmapRedondeado.dispose();
        Drawable fondoContenedorPuntuaciones = new TextureRegionDrawable(new TextureRegion(texturaFondo));

        /* --------- Configuración diseño del botón --------- */

        // Creamos un Pixmap para la textura del botón
        Pixmap pixmapBoton = new Pixmap(100, 100, Pixmap.Format.RGBA8888);
        pixmapBoton.setColor(new Color(0.1f, 0.5f, 0.8f, 1f));
        pixmapBoton.fill();
        texturaBoton = new Texture(pixmapBoton);
        pixmapBoton.dispose();
        Drawable drawableBoton = new TextureRegionDrawable(new TextureRegion(texturaBoton));

        /* --------- Configuración estilos para Labels --------- */

        LabelStyle estiloTitulo = new LabelStyle(fontTitulo, Color.WHITE);
        LabelStyle estiloEncabezado = new LabelStyle(fontEncabezado, Color.BLACK);
        LabelStyle estiloContenido = new LabelStyle(fontContenido, Color.BLACK);

        /* --------- Configuración estilo para botones --------- */

        TextButtonStyle estiloBoton = new TextButtonStyle();
        estiloBoton.font = fontEncabezado;
        estiloBoton.fontColor = Color.WHITE;
        estiloBoton.up = drawableBoton;
        estiloBoton.down = drawableBoton;
        estiloBoton.over = drawableBoton;

        /* --------- Creación tabla principal para la pantalla --------- */

        // Creamos la tabla principal que llenara toda la pantalla
        Table tablaPrincipal = new Table();
        tablaPrincipal.setFillParent(true);
        tablaPrincipal.center();
        stage.addActor(tablaPrincipal);

        /* --------- Contenedor de la tabla de puntuaciones (con fondo redondeado) --------- */

        Table contenedorPuntuaciones = new Table();
        contenedorPuntuaciones.setBackground(fondoContenedorPuntuaciones);
        contenedorPuntuaciones.setColor(new Color(1, 1, 1, 0.95f));
        contenedorPuntuaciones.pad(20);

        /* --------- Título de la pantalla --------- */

        Label labelTitulo = new Label("Campo de golf Campestre", estiloTitulo);
        tablaPrincipal.add(labelTitulo).padBottom(40).row();

        /* --------- Tabla para las puntuaciones (dentro de contenedorPuntuaciones) --------- */

        Table tablaPuntuaciones = new Table();

        /* --------- Creación de encabezados --------- */

        tablaPuntuaciones.add(new Label("Hoyo", estiloEncabezado)).pad(5).height(40).center().width(120);
        for (int i = 1; i <= MAX_NIVELES_MOSTRADOS; i++) {
            tablaPuntuaciones.add(new Label(String.valueOf(i), estiloEncabezado)).pad(5).height(40).center();
        }
        tablaPuntuaciones.add(new Label("Total", estiloEncabezado)).pad(5).height(40).center().row();

        /* --------- Fila de Par --------- */

        tablaPuntuaciones.add(new Label("Par", estiloEncabezado)).pad(5).height(40).center().width(120);
        // Array con los valores de par para cada hoyo
        int[] pares = { 2, 2, 4, 5, 4, 5, 5, 9, 10, 12, 4, 7, 14, 14, 14, 9, 8, 4 };
        int totalPar = 0;
        for (int i = 0; i < MAX_NIVELES_MOSTRADOS; i++) {
            if (i < pares.length) {
                tablaPuntuaciones.add(new Label(String.valueOf(pares[i]), estiloContenido)).pad(5).height(40).center();
                totalPar += pares[i];
            } else {
                tablaPuntuaciones.add(new Label("-", estiloContenido)).pad(5).height(40).center();
            }
        }
        tablaPuntuaciones.add(new Label(String.valueOf(totalPar), estiloContenido)).pad(5).height(40).center().row();

        /* --------- Filas de jugadores --------- */

        for (Jugador jugador : jugadores) {
            // Creamos el label del jugador con su color correspondiente
            Label labelJugador = new Label(jugador.getNombre(), new LabelStyle(fontContenido, jugador.getColorBola()));
            labelJugador.setAlignment(Align.left);
            tablaPuntuaciones.add(labelJugador).pad(5).height(40).growX().align(Align.left);

            // Agregamos los puntajes de cada hoyo para este jugador
            for (int i = 0; i < MAX_NIVELES_MOSTRADOS; i++) {
                if (i < jugador.getPuntajePorHoyo().size()) {
                    int golpes = jugador.getPuntajePorHoyo().get(i);
                    tablaPuntuaciones.add(new Label(String.valueOf(golpes), estiloContenido)).pad(5).height(40)
                            .center();
                } else {
                    tablaPuntuaciones.add(new Label("-", estiloContenido)).pad(5).height(40).center();
                }
            }
            // Agregamos el puntaje total del jugador
            tablaPuntuaciones.add(new Label(String.valueOf(jugador.getPuntajeTotal()), estiloContenido)).pad(5)
                    .height(40)
                    .center().row();
        }

        // Agregamos la tabla de puntuaciones al contenedor
        contenedorPuntuaciones.add(tablaPuntuaciones).expand().fill().row();
        tablaPrincipal.add(contenedorPuntuaciones).width(VIRTUAL_WIDTH * 0.95f).height(VIRTUAL_HEIGHT * 0.7f).center()
                .padBottom(50)
                .row();

        /* --------- Contenedor para los botones --------- */

        Table tablaBotones = new Table();

        // Verificamos si el juego ha terminado para decidir qué botones mostrar
        // El juego termina después de completar TODOS los niveles (cuando nivelActual >= MAX_NIVELES_MOSTRADOS)
        boolean esFinDelJuego = nivelActualDelJuego >= MAX_NIVELES_MOSTRADOS;

        /* --------- Botón "Salir al Menú" (siempre visible o solo al final) --------- */

        TextButton botonSalir = new TextButton("Salir al Menu", estiloBoton);
        botonSalir.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Registramos en el log que estamos saliendo al menú principal
                Gdx.app.log("TarjetaPuntosScreen", "Saliendo al menu principal.");
                // La responsabilidad de reiniciar el juego es de la pantalla de menú
                game.setScreen(new menuInicialScreen(game));
            }
        });

        // Verificamos si es el final del juego para mostrar diferentes opciones
        if (esFinDelJuego) {
            // Si es el final, mostramos un solo botón grande para salir
            Gdx.app.log("TarjetaPuntosScreen", "¡Todos los niveles completados!");
            tablaPrincipal.add(botonSalir).width(300).height(70).center();

        } else {
            /* --------- Si no es el final, mostramos "Continuar" y "Salir" --------- */

            TextButton botonContinuar = new TextButton("Siguiente Hoyo", estiloBoton);
            botonContinuar.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // Registramos en el log que estamos cargando el siguiente nivel
                    Gdx.app.log("TarjetaPuntosScreen", "Cargando siguiente nivel.");
                    nivelActualDelJuego++;
                    Gdx.app.log("TarjetaPuntosScreen", "Cargando el hoyo " + (nivelActualDelJuego + 1));

                    // Pasamos al siguiente nivel
                    game.setScreen(new jugarGolfScreen(game, jugadores, nivelActualDelJuego));
                }
            });

            // Agregamos ambos botones a la tabla de botones
            tablaBotones.add(botonSalir).width(250).height(70).padRight(20);
            tablaBotones.add(botonContinuar).width(250).height(70).padLeft(20);
            tablaPrincipal.add(tablaBotones).center();
        }
    }

    @Override
    public void render(float delta) {

        // En cada fotograma, se limpia el buffer de color (en pocas palabras se limpia la pantalla)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Actualizamos el estado de todos los actores que están en el stage
        stage.act(delta);
        // Dibujamos todos los actores en su nuevo estado
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Nos aseguramos de que nuestro viewport (ventana a través de la cual se ve el stage) siempre esté centrado
        viewport.update(width, height, true);
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
        fontTitulo.dispose();
        fontEncabezado.dispose();
        fontContenido.dispose();
        texturaFondo.dispose();
        texturaBoton.dispose();
    }
}