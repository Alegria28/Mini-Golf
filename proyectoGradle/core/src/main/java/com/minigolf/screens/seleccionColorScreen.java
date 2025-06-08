// cspell: ignore pixmap
package com.minigolf.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

// Importamos las clases necesarias
import com.minigolf.MiniGolfMain;
import com.minigolf.models.Jugador;;

/**
 * Las clases que implementan son diferentes escenas que Game puede mostrar, por lo que son parte del ciclo de vida del juego (ver {@link MiniGolfMain}).
 */
public class seleccionColorScreen implements Screen {

    // Atributos
    private Stage stage;
    private Table tablePrincipal;
    private Stack stack;
    Label informacionJugador;
    TextButton botonIniciar;
    private BitmapFont font;

    // Creamos un ArrayList para texturas, y que sea mas fácil limpiarlas
    private ArrayList<Texture> texturasColores = new ArrayList<Texture>();

    private final MiniGolfMain game;
    TextButtonStyle buttonStyle;
    String[] nombres;

    // Dimensiones virtuales (buena practica para el diseño)
    private final float VIRTUAL_WIDTH = 900;
    private final float VIRTUAL_HEIGHT = 900;

    // Indice para el vector nombres que recibimos
    private int indiceVector = 0;
    // Lista para almacenar la información de los jugadores
    private ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
    // Para saber si ya hemos terminado de seleccionar los colores
    private boolean terminado = false;

    // Constructor de la clase
    public seleccionColorScreen(MiniGolfMain game, TextButtonStyle buttonStyle, String[] nombres) {
        this.game = game;
        this.buttonStyle = buttonStyle;
        this.nombres = nombres;
    }

    @Override
    public void show() {

        /* --------- Configuración inicial stage y tablePrincipal --------- */

        // Creamos una cámara, que actúa como el "ojo" del juego, definiendo qué parte del mundo se mostrará en pantalla
        OrthographicCamera camera = new OrthographicCamera();

        // El viewport configura y gestiona la cámara, definiendo su "campo de visión" con las dimensiones VIRTUAL_WIDTH y VIRTUAL_HEIGHT.
        Viewport viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);

        // Creamos nuestro stage con el tamaño del viewport especificado (para que stage sepa cual es su sistema de coordenadas)
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        // Creamos nuestro table principal (actor)
        tablePrincipal = new Table();
        // Llenara por completo a stage
        tablePrincipal.setFillParent(true);

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
        Texture textureFondo = new Texture(Gdx.files.internal("fondoInicio.png"));
        // Creamos la imagen
        Image imagenFondo = new Image(textureFondo);
        // Aplicamos la opacidad a nuestra imagen de fondo
        imagenFondo.setColor(new Color(1f, 1f, 1f, 0.5f));

        // Agregamos el Texture a nuestro array
        texturasColores.add(textureFondo);

        /* --------- Table 1 --------- */

        // Creamos el table para el label de información
        Table tableArriba = new Table();

        // Creamos el estilo para nuestro label
        LabelStyle labelPrincipalStyle = new LabelStyle();
        // Cambiamos su font
        labelPrincipalStyle.font = font;
        // El color va a ser blanco
        labelPrincipalStyle.fontColor = Color.WHITE;

        // Utilizaremos un label para indicar el nombre del jugador a que le toca elegir color el cual va a estar centrado
        informacionJugador = new Label("", labelPrincipalStyle);
        informacionJugador.setAlignment(Align.center);

        // Agregamos el label al table de arriba
        tableArriba.add(informacionJugador).center();
        tableArriba.center();

        // Finalmente, agregamos este actor con todas sus cosas al tablePrincipal
        tablePrincipal.add(tableArriba).height(VIRTUAL_HEIGHT / 5).width(VIRTUAL_WIDTH).center();

        /* --------- Table 2 --------- */

        // Creamos una nueva fila en el tablePrincipal para agregar la segunda tabla
        tablePrincipal.row();

        // Creamos el table para los botones de colores
        Table tableBotones = new Table();

        /* --------- Botones para colores --------- */

        // Creamos el botón con un color obtenido del Drawable que retorna la función
        Button botonColor1 = new Button(crearDrawable(255, 255, 255)); // Blanco
        botonColor1.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // Ya no podremos hacer click si ya hemos terminado
                if (!terminado) {
                    System.out.println("Color blanco seleccionado");

                    // Creamos y agregamos la instancia de Jugador a nuestra lista
                    jugadores.add(new Jugador(nombres[indiceVector], new Color(1, 1, 1, 1f)));
                    // Aumentamos el indice del vector
                    indiceVector++;
                    // Ocultamos este botón ya que no esta disponible
                    botonColor1.setVisible(false);
                }
            }
        });

        Button botonColor2 = new Button(crearDrawable(0, 255, 0)); // Verde
        botonColor2.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // Ya no podremos hacer click si ya hemos terminado
                if (!terminado) {
                    System.out.println("Color verde seleccionado");

                    // Creamos y agregamos la instancia de Jugador a nuestra lista
                    jugadores.add(new Jugador(nombres[indiceVector], new Color(0f, 1f, 0f, 1f)));
                    // Aumentamos el indice del vector
                    indiceVector++;
                    // Ocultamos este botón ya que no esta disponible
                    botonColor2.setVisible(false);
                }
            }
        });

        Button botonColor3 = new Button(crearDrawable(255, 255, 0)); // Amarillo
        botonColor3.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // Ya no podremos hacer click si ya hemos terminado
                if (!terminado) {
                    System.out.println("Color amarillo seleccionado");

                    // Creamos y agregamos la instancia de Jugador a nuestra lista
                    jugadores.add(new Jugador(nombres[indiceVector], new Color(1f, 1f, 0f, 1f)));
                    // Aumentamos el indice del vector
                    indiceVector++;
                    // Ocultamos este botón ya que no esta disponible
                    botonColor3.setVisible(false);
                }
            }
        });

        Button botonColor4 = new Button(crearDrawable(0, 0, 255)); // Azul
        botonColor4.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // Ya no podremos hacer click si ya hemos terminado
                if (!terminado) {
                    System.out.println("Color azul seleccionado");

                    // Creamos y agregamos la instancia de Jugador a nuestra lista
                    jugadores.add(new Jugador(nombres[indiceVector], new Color(0f, 0f, 1f, 1f)));
                    // Aumentamos el indice del vector
                    indiceVector++;
                    // Ocultamos este botón ya que no esta disponible
                    botonColor4.setVisible(false);
                }
            }
        });

        // Agregamos los primeros 4 colores a la tabla
        tableBotones.add(botonColor1).width(100).height(100).pad(10);
        tableBotones.add(botonColor2).width(100).height(100).pad(10);
        tableBotones.add(botonColor3).width(100).height(100).pad(10);
        tableBotones.add(botonColor4).width(100).height(100).pad(10);

        // Indicamos que lo siguiente que se va a agregar va a estar en otra row
        tableBotones.row();

        // Creamos el botón con un color obtenido del Drawable que retorna la función
        Button botonColor5 = new Button(crearDrawable(255, 0, 0)); // Rojo
        botonColor5.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // Ya no podremos hacer click si ya hemos terminado
                if (!terminado) {
                    System.out.println("Color rojo seleccionado");

                    // Creamos y agregamos la instancia de Jugador a nuestra lista
                    jugadores.add(new Jugador(nombres[indiceVector], new Color(1f, 0f, 0f, 1f)));
                    // Aumentamos el indice del vector
                    indiceVector++;
                    // Ocultamos este botón ya que no esta disponible
                    botonColor5.setVisible(false);
                }

            }
        });

        Button botonColor6 = new Button(crearDrawable(255, 0, 255)); // Rosa
        botonColor6.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // Ya no podremos hacer click si ya hemos terminado
                if (!terminado) {
                    System.out.println("Color rosa seleccionado");

                    // Creamos y agregamos la instancia de Jugador a nuestra lista
                    jugadores.add(new Jugador(nombres[indiceVector], new Color(1f, 0f, 1f, 1f)));
                    // Aumentamos el indice del vector
                    indiceVector++;
                    // Ocultamos este botón ya que no esta disponible
                    botonColor6.setVisible(false);
                }
            }
        });

        Button botonColor7 = new Button(crearDrawable(0, 255, 255)); // Cyan
        botonColor7.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // Ya no podremos hacer click si ya hemos terminado
                if (!terminado) {
                    System.out.println("Color cyan seleccionado");

                    // Creamos y agregamos la instancia de Jugador a nuestra lista
                    jugadores.add(new Jugador(nombres[indiceVector], new Color(0f, 1f, 1f, 1f)));
                    // Aumentamos el indice del vector
                    indiceVector++;
                    // Ocultamos este botón ya que no esta disponible
                    botonColor7.setVisible(false);
                }
            }
        });

        Button botonColor8 = new Button(crearDrawable(120, 0, 190)); // Morado
        botonColor8.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // Ya no podremos hacer click si ya hemos terminado
                if (!terminado) {
                    System.out.println("Color morado seleccionado");

                    // Creamos y agregamos la instancia de Jugador a nuestra lista
                    jugadores.add(new Jugador(nombres[indiceVector], new Color(120f / 255f, 0f, 190f / 255f, 1f)));
                    // Aumentamos el indice del vector
                    indiceVector++;
                    // Ocultamos este botón ya que no esta disponible
                    botonColor8.setVisible(false);
                }
            }
        });

        // Agregamos los siguientes 4 colores a la tabla
        tableBotones.add(botonColor5).width(100).height(100).pad(10);
        tableBotones.add(botonColor6).width(100).height(100).pad(10);
        tableBotones.add(botonColor7).width(100).height(100).pad(10);
        tableBotones.add(botonColor8).width(100).height(100).pad(10);

        /* --------- Botón iniciar --------- */

        // Creamos una nueva fila para el botón de iniciar
        tableBotones.row();

        // Creamos un botón para iniciar el juego
        botonIniciar = new TextButton("Iniciar", buttonStyle);
        botonIniciar.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Iniciando juego");
                // Cambiar a la pantalla del juego de golf
                game.setScreen(new jugarGolfScreen(game, jugadores));
            }
        });

        // Lo ocultamos por ahora
        botonIniciar.setVisible(false);

        // Agregamos el botón de iniciar centrado debajo de los colores
        tableBotones.add(botonIniciar).colspan(4).center().width(300).height(70).pad(30);

        // Agregamos el table de botones al tablePrincipal
        tablePrincipal.add(tableBotones).height(VIRTUAL_HEIGHT * 3 / 5).width(VIRTUAL_WIDTH).center();

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

        // Agregamos el actor que tiene el orden de los demás
        stage.addActor(stack);

    }

    @Override
    public void render(float delta) {
        // En cada fotograma, se limpia el buffer de color (en pocas palabra se limpia la pantalla)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Actualizamos el estado de todos los actores que están en el stage
        stage.act(Gdx.graphics.getDeltaTime());
        // Dibujamos los estados en su nuevo estado
        stage.draw();

        // Mostramos el nombre que le corresponde a esa posición en el vector
        try {
            informacionJugador.setText("Color para: " + nombres[indiceVector]);
        } catch (ArrayIndexOutOfBoundsException e) {
            // Si cachamos este error, entonces ya hemos elegido el ultimo color
            botonIniciar.setVisible(true);
            // Ya hemos terminado de seleccionar
            terminado = true;
        }
    }

    @Override
    public void resize(int width, int height) {
        // Nos aseguramos de que nuestro viewport (ventana a traves de la cual se ve el stage) siempre este centrado
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
        // Limpiamos y liberamos todos los recursos cargados
        stage.dispose();
        // Recorremos nuestro array limpiando cada textura
        for (Texture actual : texturasColores) {
            actual.dispose();
        }
        // Limpiamos el array
        texturasColores.clear();
        font.dispose();
        Gdx.input.setInputProcessor(null);
    }

    public Drawable crearDrawable(float r, float g, float b) {

        // Información de https://shorturl.at/mCaWx

        // Creamos un pixmap, lo cual es un "lienzo" donde se puede dibujar pixel por pixel, en este caso solo hacemos un lienzo de 1x1 pixeles con un formato RGBA con
        // 8 bits en cada canal
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        // Primero cargamos el color que va a tener este pixel (pixmap tiene un formato normalizado de 0-1 en lugar de 0-255, por lo que dividimos entre 255 para poder
        // obtener el formato normalizado)
        pixmap.setColor(new Color(r / 255f, g / 255f, b / 255f, 1f));
        // Ahora si pintamos este pixel con el color cargado
        pixmap.fill();

        // Creamos una textura a partir de este pixmap
        Texture textureColor = new Texture(pixmap);
        // Creamos un drawable a partir de la textura
        Drawable colorBoton = new TextureRegionDrawable(new TextureRegion(textureColor));

        // Liberamos pixmap y guardamos el texture
        pixmap.dispose();
        texturasColores.add(textureColor);

        return colorBoton;
    }
}
