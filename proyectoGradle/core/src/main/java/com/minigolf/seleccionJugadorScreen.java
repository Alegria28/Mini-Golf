package com.minigolf;

// Importamos los recursos necesarios para la clase
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Las clases que implementan son diferentes escenas que Game puede mostrar, por lo que son parte del ciclo de vida del juego (ver {@link MiniGolfMain}).
 */
public class seleccionJugadorScreen implements Screen {

    // Atributos
    private Stage stage;
    private Table tablePrincipal;
    Texture textureFondo;
    private Table tableTextField;
    private Stack stack;
    private BitmapFont fontTextFields;
    private Skin textFieldSkin;
    private BitmapFont font;

    private final MiniGolfMain game;
    TextButtonStyle buttonStyle;

    // Variable para almacenar la selección del usuario
    private int numeroJugadores = 0;

    // Dimensiones virtuales (buena practica para el diseño)
    private final float VIRTUAL_WIDTH = 900;
    private final float VIRTUAL_HEIGHT = 900;

    // Para poder jugar con setVisible los marcamos como variables globales
    private TextField textField1;
    private TextField textField2;
    private TextField textField3;

    // Creamos un vector para almacenar los nombres de los jugadores
    String[] nombresVector;

    // Constructor de la clase
    public seleccionJugadorScreen(MiniGolfMain game, TextButtonStyle buttonStyle) {
        this.game = game;
        this.buttonStyle = buttonStyle;
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
        textureFondo = new Texture(Gdx.files.internal("fondoInicio.png"));
        // Creamos la imagen
        Image imagenFondo = new Image(textureFondo);
        // Aplicamos la opacidad a nuestra imagen de fondo
        imagenFondo.setColor(new Color(1f, 1f, 1f, 0.5f));

        /* --------- Table 1 --------- */

        // Creamos el table para el label y los 3 botones
        Table tableArriba = new Table();

        // Creamos el estilo para nuestro label
        LabelStyle labelPrincipalStyle = new LabelStyle();
        // Cambiamos su font
        labelPrincipalStyle.font = font;
        // El color va a ser blanco
        labelPrincipalStyle.fontColor = Color.WHITE;

        // Creamos nuestro label
        Label labelPrincipal = new Label("Numero de jugadores", labelPrincipalStyle);

        // Agregamos este label al table con .colspan() para que el ancho de este label
        // ocupe el de 3 columnas, ya que tenemos 3 botones abajo, cada uno en su propia columna
        // y para .center() puede centrar este label correctamente arriba de los 3 botones
        tableArriba.add(labelPrincipal).colspan(3).center();

        /* --------- Creación botones opciones numero de jugadores --------- */

        // Indicamos que lo siguiente que se va a agregar va a estar en otra row
        tableArriba.row();

        TextButton boton1Jugador = new TextButton("1 jugador", buttonStyle);
        boton1Jugador.addListener(new InputListener() {
            @Override
            // Called when a mouse button or a finger touch goes down on the actor
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            // Called when a mouse button or a finger touch goes up anywhere, but only if touchDown previously returned true for the mouse
            // button or touch
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                // Guardamos la selección del usuario
                numeroJugadores = 1;

                // Cada vez que se seleccione el botón, se limpiaran todos los textField's
                textField1.setText("");
                textField2.setText("");
                textField3.setText("");

                System.out.println("Opcion 1 jugador, la variable tiene: " + numeroJugadores);

            }
        });

        TextButton boton2Jugador = new TextButton("2 jugadores", buttonStyle);
        boton2Jugador.addListener(new InputListener() {
            @Override
            // Called when a mouse button or a finger touch goes down on the actor
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            // Called when a mouse button or a finger touch goes up anywhere, but only if touchDown previously returned true for the mouse
            // button or touch
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                // Guardamos la selección del usuario
                numeroJugadores = 2;

                // Cada vez que se seleccione el botón, se limpiaran todos los textField's
                textField1.setText("");
                textField2.setText("");
                textField3.setText("");

                System.out.println("Opcion 2 jugadores, la variable tiene: " + numeroJugadores);
            }
        });

        TextButton boton3Jugador = new TextButton("3 jugadores", buttonStyle);
        boton3Jugador.addListener(new InputListener() {
            @Override
            // Called when a mouse button or a finger touch goes down on the actor
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            // Called when a mouse button or a finger touch goes up anywhere, but only if touchDown previously returned true for the mouse
            // button or touch
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                // Guardamos la selección del usuario
                numeroJugadores = 3;

                // Cada vez que se seleccione el botón, se limpiaran todos los textField's
                textField1.setText("");
                textField2.setText("");
                textField3.setText("");

                System.out.println("Opcion 3 jugador, la variable tiene: " + numeroJugadores);
            }
        });

        // Agregamos los botones para que se van de forma horizontal con espaciado
        tableArriba.add(boton1Jugador).height(70).pad(30);
        tableArriba.add(boton2Jugador).height(70).pad(30);
        tableArriba.add(boton3Jugador).height(70).pad(30);
        tableArriba.center();

        // Finalmente, agregamos este actor con todas sus cosas al tablePrincipal
        tablePrincipal.add(tableArriba).height(VIRTUAL_HEIGHT / 5).width(VIRTUAL_WIDTH).center();

        /* ---------  Table 2 --------- */

        /* Configuración font para los text fields */

        // Cargamos la tipografía que se utilizara
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Inter-Variable.ttf"));
        // Creamos un objeto para modificar las características de esta tipografía
        fontParameter = new FreeTypeFontParameter();
        fontParameter.size = 25;

        // Creamos el font con las características nuevas
        fontTextFields = fontGenerator.generateFont(fontParameter);
        // Ya que ya no lo vamos a ocupar, podemos liberarlo
        fontGenerator.dispose();

        // Creamos el table para los 3 textFields's
        tableTextField = new Table();

        // Creamos el estilo para nuestros textField's
        TextFieldStyle textFieldStyle = new TextFieldStyle();
        // Cambiamos su font y color
        textFieldStyle.font = fontTextFields;
        textFieldStyle.fontColor = Color.WHITE;

        // Utilizamos la skin para obtener el diseño de los textField (fondos para los estados up y down)
        textFieldSkin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        // Declaraciones dentro del .json
        textFieldStyle.background = textFieldSkin.getDrawable("textField");
        textFieldStyle.cursor = textFieldSkin.getDrawable("textFieldCursor");
        textFieldStyle.selection = textFieldSkin.getDrawable("selection");

        // Creamos el textField
        textField1 = new TextField("", textFieldStyle);
        // Definimos el tamaño máximo que puede almacenar
        textField1.setMaxLength(10);
        // El texto va a estar alineado al centro
        textField1.setAlignment(Align.center);
        // Agregamos este textField a la tabla dedicada
        tableTextField.add(textField1).center().width(300).height(50).pad(50);

        // Cambiamos de fila
        tableTextField.row();

        // Creamos el textField
        textField2 = new TextField("", textFieldStyle);
        // Definimos el tamaño máximo que puede almacenar
        textField2.setMaxLength(10);
        // El texto va a estar alineado al centro
        textField2.setAlignment(Align.center);
        // Agregamos este textField a la tabla dedicada
        tableTextField.add(textField2).center().width(300).height(50).pad(50);

        // Cambiamos de fila
        tableTextField.row();

        // Creamos el textField
        textField3 = new TextField("", textFieldStyle);
        // Definimos el tamaño máximo que puede almacenar
        textField3.setMaxLength(10);
        // El texto va a estar alineado al centro
        textField3.setAlignment(Align.center);
        // Agregamos este textField a la tabla dedicada
        tableTextField.add(textField3).center().width(300).height(50).pad(50);

        // Creamos un botón para pasar a la siguiente screen con el numero de jugadores
        TextButton botonSiguienteScreen = new TextButton("Siguiente", buttonStyle);
        botonSiguienteScreen.addListener(new InputListener() {

            @Override
            // Called when a mouse button or a finger touch goes down on the actor
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            // Called when a mouse button or a finger touch goes up anywhere, but only if touchDown previously returned true for the mouse
            // button or touch
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                /*  Configuración font para el Dialog */

                // Cargamos la tipografía que se utilizara
                FreeTypeFontGenerator temporalGenerator = new FreeTypeFontGenerator(
                        Gdx.files.internal("fonts/Inter-Variable.ttf"));
                // Creamos un objeto para modificar las características de esta tipografía
                FreeTypeFontParameter temporalParameter = new FreeTypeFontParameter();
                temporalParameter.size = 15;
                temporalParameter.padLeft = 8;
                temporalParameter.padRight = 8;
                // Creamos el font con las características nuevas
                BitmapFont temporalFont = temporalGenerator.generateFont(temporalParameter);
                // Ya que ya no lo vamos a ocupar, podemos liberarlo
                temporalGenerator.dispose();

                // Creamos el estilo para nuestro label
                LabelStyle temporalLabelStyle = new LabelStyle();
                // Cambiamos su font
                temporalLabelStyle.font = temporalFont;

                // Cargamos la skin para el Dialog que vamos a usar
                Skin errorDialogSkin = new Skin(Gdx.files.internal("ui/uiskin.json"));
                // Creamos un Dialog
                Dialog errorDialog = new Dialog("", errorDialogSkin);
                // Creamos un Label para el mensaje que se va a mostrar
                Label labelDialog = new Label("¡Todos los campos deben de estar llenos!", temporalLabelStyle);

                // Creamos una instancia de acciones
                SequenceAction acciones = new SequenceAction();
                // Creamos y agregamos las acciones que se van a ejecutar
                acciones.addAction(Actions.delay(1.5f));
                acciones.addAction(new Action() {
                    @Override
                    public boolean act(float delta) {
                        errorDialog.hide();
                        // Liberamos recursos después de mostrar el mensaje
                        errorDialogSkin.dispose();
                        temporalFont.dispose();
                        return true;
                    }
                });

                // Antes de pasar a la siguiente pantalla, verificamos si los textField no están vacíos (según el numero de jugadores seleccionados)
                switch (numeroJugadores) {
                    case 1:
                        // Si el textField esta vacio, mostramos un Dialog indicando el error por 1 segundo
                        if (textField1.getText().length() == 0) {
                            errorDialog.show(stage);
                            // Modificamos su tamaño y posición (utilizando una formula x = (anchoPantalla - anchoDialog) / 2 y
                            // lo mismo para y)
                            errorDialog.setPosition((VIRTUAL_WIDTH - 300) / 2, (VIRTUAL_HEIGHT - 70) / 2);
                            errorDialog.setSize(300, 70);
                            errorDialog.text(labelDialog).center();

                            // Agregamos las acciones que hemos declarado previamente
                            errorDialog.addAction(acciones);
                        }
                        // Si los textField's tienen nombres, entonces los guardamos 
                        else {
                            nombresVector = new String[1];
                            nombresVector[0] = textField1.getText();
                            for (int i = 0; i < 1; ++i) {
                                System.out.println("[" + nombresVector[i] + "]");
                            }

                            System.out.println("Entrando a la pantalla selección colores con 1 jugador");

                            game.setScreen(new seleccionColorScreen(game, buttonStyle, nombresVector));
                        }
                        break;
                    case 2:
                        // Si el textField esta vacio, mostramos un Dialog indicando el error por 1 segundo
                        if (textField1.getText().length() == 0 || textField2.getText().length() == 0) {
                            errorDialog.show(stage);
                            // Modificamos su tamaño y posición (utilizando una formula x = (anchoPantalla - anchoDialog) / 2 y
                            // lo mismo para y)
                            errorDialog.setPosition((VIRTUAL_WIDTH - 300) / 2, (VIRTUAL_HEIGHT - 70) / 2);
                            errorDialog.setSize(300, 70);
                            errorDialog.text(labelDialog).center();

                            // Agregamos las acciones que hemos declarado previamente
                            errorDialog.addAction(acciones);
                        }
                        // Si los textField's tienen nombres, entonces los guardamos 
                        else {
                            nombresVector = new String[2];
                            nombresVector[0] = textField1.getText();
                            nombresVector[1] = textField2.getText();
                            for (int i = 0; i < 2; ++i) {
                                System.out.println("[" + nombresVector[i] + "]");
                            }

                            System.out.println("Entrando a la pantalla selección colores con 1 jugador");

                            game.setScreen(new seleccionColorScreen(game, buttonStyle, nombresVector));
                        }
                        break;
                    case 3:
                        // Si el textField esta vacio, mostramos un Dialog indicando el error por 1 segundo
                        if (textField1.getText().length() == 0 || textField2.getText().length() == 0
                                || textField3.getText().length() == 0) {
                            errorDialog.show(stage);
                            // Modificamos su tamaño y posición (utilizando una formula x = (anchoPantalla - anchoDialog) / 2 y
                            // lo mismo para y)
                            errorDialog.setPosition((VIRTUAL_WIDTH - 300) / 2, (VIRTUAL_HEIGHT - 70) / 2);
                            errorDialog.setSize(300, 70);
                            errorDialog.text(labelDialog).center();

                            // Agregamos las acciones que hemos declarado previamente
                            errorDialog.addAction(acciones);
                        }
                        // Si los textField's tienen nombres, entonces los guardamos 
                        else {
                            nombresVector = new String[3];
                            nombresVector[0] = textField1.getText();
                            nombresVector[1] = textField2.getText();
                            nombresVector[2] = textField3.getText();
                            for (int i = 0; i < 3; ++i) {
                                System.out.println("[" + nombresVector[i] + "]");
                            }

                            System.out.println("Entrando a la pantalla selección colores con 1 jugador");

                            game.setScreen(new seleccionColorScreen(game, buttonStyle, nombresVector));
                        }
                        break;
                }
            }
        });

        // Agregamos el ultimo botón al table
        tableTextField.row();
        tableTextField.add(botonSiguienteScreen).width(300).height(50).pad(20);

        // Creamos una nueva fila en el tablePrincipal para agregar esa otra con todas sus cosas
        tablePrincipal.row();
        tablePrincipal.add(tableTextField).height(VIRTUAL_HEIGHT / 2).width(VIRTUAL_WIDTH).center();

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

        // En cada frame, verificamos que textfield se va a mostrar
        switch (numeroJugadores) {
            case 0:
                textField1.setVisible(false);
                textField2.setVisible(false);
                textField3.setVisible(false);
                break;
            case 1:
                textField1.setVisible(true);
                textField2.setVisible(false);
                textField3.setVisible(false);
                break;
            case 2:
                textField1.setVisible(true);
                textField2.setVisible(true);
                textField3.setVisible(false);
                break;
            case 3:
                textField1.setVisible(true);
                textField2.setVisible(true);
                textField3.setVisible(true);
                break;
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
        textureFondo.dispose();
        fontTextFields.dispose();
        textFieldSkin.dispose();
        font.dispose();
        Gdx.input.setInputProcessor(null);
    }
}