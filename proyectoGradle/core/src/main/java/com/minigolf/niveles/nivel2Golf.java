package com.minigolf.niveles;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture; 
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

// Importamos la clase 
import com.minigolf.handlers.*;

public class nivel2Golf {

    // Factor de conversion para el mundo de Box2D, donde 100px = 1m 
    private static final float PIXEL_A_METRO = 0.01f;

    // Atributos
    public static final float coordenadaInicioX = 130; 
    private static final float coordenadaInicioY = 700; 
    private static final float coordenadaHoyoX = 750; 
    private static final float coordenadaHoyoY = 750; 

    // Areas válidas para colocar la bola (en el mundo)
    public static final float minX = 130 * PIXEL_A_METRO, maxX = 200 * PIXEL_A_METRO; // Ajustamos el área válida
    public static final float minY = 700 * PIXEL_A_METRO, maxY = 770 * PIXEL_A_METRO; // Ajustamos el área válida

    public static HashMap<Body, Boolean> crearNivel(Stage stage, World mundoBox2d, Image imagePuntoDeInicio,
            HashMap<Body, Boolean> hashMapBodiesTemporales, Texture textureParedes, Texture texturaHoyo) { 
        // Quitamos la imagen del stage (si es que estaba en el)
        imagePuntoDeInicio.remove();

        // Cambiamos la posición de la imagen
        imagePuntoDeInicio.setPosition(coordenadaInicioX, coordenadaInicioY);

        // Agregamos la imagen a nuestro table con cierto tamaño
        stage.addActor(imagePuntoDeInicio);

        /* --------- Hoyo --------- */

        // Definimos un Body lo cual es un objeto dentro del mundo de Box2D, por defecto es estático, 
        // significando que no se mueve 
        BodyDef bodyDefHoyo = new BodyDef();
        // Establecemos su posición en el mundo (pared inferior), es importante recalcar que Box2D si 
        // toma el centro del Body como posición, y no una esquina como LibGDX
        bodyDefHoyo.position.set(coordenadaHoyoX * PIXEL_A_METRO, coordenadaHoyoY * PIXEL_A_METRO);
        // Creamos un Body a partir de la definición y lo agregamos a nuestro mundo
        Body bodyHoyo = mundoBox2d.createBody(bodyDefHoyo);
        // Agregamos esta pared a la cola para liberarlo después (al acabar el nivel)
        hashMapBodiesTemporales.put(bodyHoyo, false);

        // Creamos un polígono en general
        CircleShape shapeHoyo = new CircleShape();
        shapeHoyo.setRadius(13 * PIXEL_A_METRO); // Hoyo mas grande que una bola

        // Creamos una FixtureDef para definir las propiedades físicas de la pared
        FixtureDef fixtureDefHoyo = new FixtureDef();
        // Ponemos la forma creada 
        fixtureDefHoyo.shape = shapeHoyo;
        fixtureDefHoyo.restitution = 1f; // Rebote
        fixtureDefHoyo.density = 0f; // 0 para bodies estáticos
        fixtureDefHoyo.friction = 0.2f;

        // Configuración de colisiones
        fixtureDefHoyo.filter.categoryBits = manejoEventos.CATEGORIA_HOYO; // Pertenece a esta categoría
        fixtureDefHoyo.filter.maskBits = manejoEventos.CATEGORIA_BOLA; // Puede colisionar con las bolas

        // Creamos la fixture y la unimos al cuerpo de la pared
        bodyHoyo.createFixture(fixtureDefHoyo);
        // Liberamos el polígono
        shapeHoyo.dispose();

        // --- Representación Visual del Hoyo (Image con textura) ---
        Image imageHoyo= new Image(texturaHoyo);
        float hoyoAnchoPx = 2 * 13 * PIXEL_A_METRO / PIXEL_A_METRO; // Diámetro del hoyo
        float hoyoAltoPx = 2 * 13 * PIXEL_A_METRO / PIXEL_A_METRO; // Diámetro del hoyo
        imageHoyo.setSize(hoyoAnchoPx, hoyoAltoPx);
        float hoyoX_px = (bodyDefHoyo.position.x / PIXEL_A_METRO) - (hoyoAnchoPx / 2);
        float hoyoY_px = (bodyDefHoyo.position.y / PIXEL_A_METRO) - (hoyoAltoPx / 2);
        imageHoyo.setPosition(hoyoX_px, hoyoY_px);
        stage.addActor(imageHoyo);
        bodyHoyo.setUserData(imageHoyo);
        /* --------- Obstáculo 1 --------- */

        // Definimos un Body lo cual es un objeto dentro del mundo de Box2D, por defecto es estático, 
        // significando que no se mueve 
        BodyDef bodyDefObstaculo1 = new BodyDef();
        // Establecemos su posición en el mundo (centro del campo), es importante recalcar que Box2D si 
        // toma el centro del Body como posición, y no una esquina como LibGDX
        bodyDefObstaculo1.position.set(450 * PIXEL_A_METRO, 560 * PIXEL_A_METRO); // Ajustamos la posición del obstáculo pegado a la pared derecha
        // Creamos un Body a partir de la definición y lo agregamos a nuestro mundo
        Body bodyObstaculo1 = mundoBox2d.createBody(bodyDefObstaculo1);
        // Agregamos esta pared a la cola para liberarlo después (al acabar el nivel)
        hashMapBodiesTemporales.put(bodyObstaculo1, false);

        // Creamos un polígono en general
        PolygonShape shapeObstaculo1 = new PolygonShape();
        // Establecemos el polígono con la forma de un rectángulo vertical (barrera de madera)
        // MITAD de un lado X 25 (ancho de 50px) y en Y 250 (altura de 500px), creando una barrera vertical
        float mitadAnchoObstaculo1_m = 20 * PIXEL_A_METRO;
        float mitadAltoObstaculo1_m = 250 * PIXEL_A_METRO;
        shapeObstaculo1.setAsBox(mitadAnchoObstaculo1_m, mitadAltoObstaculo1_m); // Ajustamos la altura del obstáculo

        // Creamos una FixtureDef para definir las propiedades físicas de la pared
        FixtureDef fixtureDefObstaculo1 = new FixtureDef();
        // Ponemos la forma creada 
        fixtureDefObstaculo1.shape = shapeObstaculo1;
        fixtureDefObstaculo1.restitution = 1f; // Rebote
        fixtureDefObstaculo1.density = 0f; // 0 para bodies estáticos
        fixtureDefObstaculo1.friction = 0.2f;

        // Configuración de colisiones
        fixtureDefObstaculo1.filter.categoryBits = manejoEventos.CATEGORIA_PARED; // Pertenece a esta categoría
        fixtureDefObstaculo1.filter.maskBits = manejoEventos.CATEGORIA_BOLA; // Puede colisionar con las bolas

        // Creamos la fixture y la unimos al cuerpo de la pared
        bodyObstaculo1.createFixture(fixtureDefObstaculo1);
        // Liberamos el polígono
        shapeObstaculo1.dispose();

        // --- Representación Visual del Obstáculo 1 (Image con textura) ---
        // Creamos una imagen usando la textura de paredes
        Image imageObstaculo1 = new Image(textureParedes);

        // Calculamos las dimensiones en píxeles
        float obstaculo1AnchoPx = mitadAnchoObstaculo1_m * 2 / PIXEL_A_METRO; // Ancho total en píxeles
        float obstaculo1AltoPx = mitadAltoObstaculo1_m * 2 / PIXEL_A_METRO; // Alto total en píxeles

        // Calculamos la posición en píxeles (Box2D es centro, LibGDX es esquina inferior izquierda)
        float obstaculo1X_px = (bodyDefObstaculo1.position.x / PIXEL_A_METRO) - (obstaculo1AnchoPx / 2);
        float obstaculo1Y_px = (bodyDefObstaculo1.position.y / PIXEL_A_METRO) - (obstaculo1AltoPx / 2);

        // Establecemos el tamaño y la posición de la imagen
        imageObstaculo1.setSize(obstaculo1AnchoPx, obstaculo1AltoPx);
        imageObstaculo1.setPosition(obstaculo1X_px, obstaculo1Y_px);

        // Agregamos la imagen al Stage para que sea visible
        stage.addActor(imageObstaculo1);

        // Opcional: Puedes guardar la imagen en los datos de usuario del cuerpo de Box2D
        // para un acceso fácil en el futuro, si necesitas mover o eliminar la imagen junto con el cuerpo.
        bodyObstaculo1.setUserData(imageObstaculo1);

        return hashMapBodiesTemporales;
    }
}
