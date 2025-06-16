package com.minigolf.screens;

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

import com.minigolf.MiniGolfMain;
import com.minigolf.models.Jugador;

import java.util.ArrayList;
import java.util.Comparator;

/*
 * Pantalla que muestra la tabla de puntuaciones al finalizar un nivel o el juego.
 * Permite ver los puntajes de todos los jugadores y navegar entre niveles.
 */
public class ScoreBoardScreen implements Screen {

    private final MiniGolfMain game;
    private ArrayList<Jugador> jugadores;
    private Stage stage;
    private Viewport viewport;
    private BitmapFont fontTitle;
    private BitmapFont fontHeader;
    private BitmapFont fontContent;

    private final float VIRTUAL_WIDTH = 900;
    private final float VIRTUAL_HEIGHT = 900;

    private final int MAX_NIVELES_MOSTRADOS = 18;
    private int nivelActualDelJuego;
    

    private Texture backgroundTexture;
    private Texture buttonTexture; 

    public ScoreBoardScreen(MiniGolfMain game, ArrayList<Jugador> jugadores, int nivelActualDelJuego) {
        this.game = game;
        this.jugadores = jugadores;
        // Ordenamos los jugadores por puntaje total para mostrarlos en la tabla
        this.jugadores.sort(Comparator.comparingInt(Jugador::getPuntajeTotal));
        this.nivelActualDelJuego = nivelActualDelJuego;
    }

    @Override
    public void show() {
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Inter-Variable.ttf"));

        FreeTypeFontParameter paramTitle = new FreeTypeFontParameter();
        paramTitle.size = 48;
        paramTitle.color = Color.WHITE;
        fontTitle = fontGenerator.generateFont(paramTitle);

        FreeTypeFontParameter paramHeader = new FreeTypeFontParameter();
        paramHeader.size = 32;
        paramHeader.color = Color.BLACK;
        fontHeader = fontGenerator.generateFont(paramHeader);

        FreeTypeFontParameter paramContent = new FreeTypeFontParameter();
        paramContent.size = 28;
        paramContent.color = Color.BLACK;
        fontContent = fontGenerator.generateFont(paramContent);

        fontGenerator.dispose();

        // Fondo redondeado para la tabla de puntuaciones
        Pixmap pixmapRounded = new Pixmap(100, 100, Pixmap.Format.RGBA8888);
        pixmapRounded.setColor(Color.WHITE);
        int cornerRadius = 15;
        pixmapRounded.fillRectangle(0, cornerRadius, pixmapRounded.getWidth(), pixmapRounded.getHeight() - 2 * cornerRadius);
        pixmapRounded.fillRectangle(cornerRadius, 0, pixmapRounded.getWidth() - 2 * cornerRadius, pixmapRounded.getHeight());
        pixmapRounded.fillCircle(cornerRadius, cornerRadius, cornerRadius);
        pixmapRounded.fillCircle(pixmapRounded.getWidth() - cornerRadius, cornerRadius, cornerRadius);
        pixmapRounded.fillCircle(cornerRadius, pixmapRounded.getHeight() - cornerRadius, cornerRadius);
        pixmapRounded.fillCircle(pixmapRounded.getWidth() - cornerRadius, pixmapRounded.getHeight() - cornerRadius, cornerRadius);
        
        backgroundTexture = new Texture(pixmapRounded);
        pixmapRounded.dispose();
        Drawable scoreContainerBackground = new TextureRegionDrawable(new TextureRegion(backgroundTexture));

        // Diseño del botón
        Pixmap pixmapButton = new Pixmap(100, 100, Pixmap.Format.RGBA8888);
        pixmapButton.setColor(new Color(0.1f, 0.5f, 0.8f, 1f)); 
        pixmapButton.fill();
        buttonTexture = new Texture(pixmapButton);
        pixmapButton.dispose();
        Drawable buttonDrawable = new TextureRegionDrawable(new TextureRegion(buttonTexture));


        // Estilos para Labels
        LabelStyle styleTitle = new LabelStyle(fontTitle, Color.WHITE);
        LabelStyle styleHeader = new LabelStyle(fontHeader, Color.BLACK);
        LabelStyle styleContent = new LabelStyle(fontContent, Color.BLACK);

        // Estilo para botones
        TextButtonStyle buttonStyle = new TextButtonStyle();
        buttonStyle.font = fontHeader;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.up = buttonDrawable; 
        buttonStyle.down = buttonDrawable;
        buttonStyle.over = buttonDrawable;


        // Crear la tabla principal para la pantalla 
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.center();
        stage.addActor(mainTable);
 
        // Contenedor de la tabla de puntuaciones (con fondo redondeado)
        Table scoreContainer = new Table();
        scoreContainer.setBackground(scoreContainerBackground);
        scoreContainer.setColor(new Color(1, 1, 1, 0.95f));
        scoreContainer.pad(20);

        // Título de la pantalla
        Label titleLabel = new Label("El campo de golf de Maspalomas", styleTitle);
        mainTable.add(titleLabel).padBottom(40).row();

        // Tabla para las puntuaciones (dentro de scoreContainer)
        Table scoreTable = new Table();
        
        // Encabezados
        scoreTable.add(new Label("Hoyo", styleHeader)).pad(5).height(40).center().width(120);
        for (int i = 1; i <= MAX_NIVELES_MOSTRADOS; i++) {
            scoreTable.add(new Label(String.valueOf(i), styleHeader)).pad(5).height(40).center();
        }
        scoreTable.add(new Label("Total", styleHeader)).pad(5).height(40).center().row();

        // Fila de Par
        scoreTable.add(new Label("Par", styleHeader)).pad(5).height(40).center().width(120);
        int[] pars = {4, 3, 4, 5, 4, 4, 3, 5, 3, 5, 4, 4, 3, 5, 4, 4, 3, 4};
        int totalPar = 0;
        for (int i = 0; i < MAX_NIVELES_MOSTRADOS; i++) {
            if (i < pars.length) {
                scoreTable.add(new Label(String.valueOf(pars[i]), styleContent)).pad(5).height(40).center();
                totalPar += pars[i];
            } else {
                scoreTable.add(new Label("-", styleContent)).pad(5).height(40).center();
            }
        }
        scoreTable.add(new Label(String.valueOf(totalPar), styleContent)).pad(5).height(40).center().row();

        // Filas de jugadores
        for (Jugador jugador : jugadores) {
            Label jugadorLabel = new Label(jugador.getNombre(), new LabelStyle(fontContent, jugador.getColorBola()));
            jugadorLabel.setAlignment(Align.left);
            scoreTable.add(jugadorLabel).pad(5).height(40).growX().align(Align.left);

            for (int i = 0; i < MAX_NIVELES_MOSTRADOS; i++) {
                if (i < jugador.getPuntajePorHoyo().size()) {
                    int strokes = jugador.getPuntajePorHoyo().get(i);
                    scoreTable.add(new Label(String.valueOf(strokes), styleContent)).pad(5).height(40).center();
                } else {
                    scoreTable.add(new Label("-", styleContent)).pad(5).height(40).center();
                }
            }
            scoreTable.add(new Label(String.valueOf(jugador.getPuntajeTotal()), styleContent)).pad(5).height(40).center().row();
        }

        scoreContainer.add(scoreTable).expand().fill().row();
        mainTable.add(scoreContainer).width(VIRTUAL_WIDTH * 0.95f).height(VIRTUAL_HEIGHT * 0.7f).center().padBottom(50).row();
        
        // Contenedor para los botones
        Table buttonTable = new Table();
        
        // Comprobar si el juego ha terminado para decidir qué botones mostrar
        boolean esFinDelJuego = nivelActualDelJuego >= MAX_NIVELES_MOSTRADOS - 1;

        // Botón "Salir al Menú" (siempre visible o solo al final)
        TextButton exitButton = new TextButton("Salir al Menú", buttonStyle);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("ScoreBoardScreen", "Saliendo al menú principal.");
                // La responsabilidad de reiniciar el juego es de la pantalla de menú
                game.setScreen(new menuInicialScreen(game));
            }
        });

        if (esFinDelJuego) {
            // Si es el final, mostramos un solo botón grande para salir
            Gdx.app.log("ScoreBoardScreen", "¡Todos los niveles completados!");
            mainTable.add(exitButton).width(300).height(70).center();

        } else {
            // Si no es el final, mostramos "Continuar" y "Salir"
            TextButton continueButton = new TextButton("Siguiente Hoyo", buttonStyle);
            continueButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.log("ScoreBoardScreen", "Cargando siguiente nivel.");
                    nivelActualDelJuego++;
                    if (nivelActualDelJuego >= MAX_NIVELES_MOSTRADOS) {
                        nivelActualDelJuego = 0; // Reiniciar al primer nivel si se supera el
                        // máximo de niveles mostrados.
                    }                  
                    Gdx.app.log("ScoreBoardScreen", "Cargando el hoyo " + (nivelActualDelJuego + 1));

                    game.setScreen(new jugarGolfScreen(game, jugadores, nivelActualDelJuego)); // Pasamos de nivel
                }
            });
            
            buttonTable.add(exitButton).width(250).height(70).padRight(20);
            buttonTable.add(continueButton).width(250).height(70).padLeft(20);
            mainTable.add(buttonTable).center();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
        fontTitle.dispose();
        fontHeader.dispose();
        fontContent.dispose();
        
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
        if (buttonTexture != null) { // <-- AÑADIDO: Liberar la textura del botón
            buttonTexture.dispose();
        }
    }
}