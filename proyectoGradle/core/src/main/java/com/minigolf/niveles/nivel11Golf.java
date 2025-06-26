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

public class nivel11Golf {

    // Factor de conversion para el mundo de Box2D, donde 100px = 1m 
    private static final float PIXEL_A_METRO = 0.01f;


    // Atributos
    public static final float coordenadaInicioX = 130; 
    private static final float coordenadaInicioY = 700; 
    public static final float coordenadaHoyoX = 740;
    public static final float coordenadaHoyoY = 160; 

    // Areas válidas para colocar la bola (en el mundo)
    public static final float minX = 130 * PIXEL_A_METRO, maxX = 200 * PIXEL_A_METRO; // Ajustamos el área válida
    public static final float minY = 700 * PIXEL_A_METRO, maxY = 770 * PIXEL_A_METRO; // Ajustamos el área válida

    public static HashMap<Body, Boolean> crearNivel(Stage stage, World mundoBox2d, Image imagePuntoDeInicio,
            HashMap<Body, Boolean> hashMapBodiesTemporales, Texture textureParedes, Texture texturaHoyo) { // Agregado Texture textureParedes

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

        /* --------- Paredes --------- */

        /* Obstáculo que cubre al punto de inicio parte derecha */

        BodyDef bodyDefParedDerecha = new BodyDef();
        bodyDefParedDerecha.position.set(280 * PIXEL_A_METRO, 630 * PIXEL_A_METRO);
        Body bodyParedDerecha = mundoBox2d.createBody(bodyDefParedDerecha);
        hashMapBodiesTemporales.put(bodyParedDerecha, false);
        PolygonShape shapeParedDerecha = new PolygonShape();
        float mitadAnchoParedDerecha_m = 20 * PIXEL_A_METRO;
        float mitadAltoParedDerecha_m = 180 * PIXEL_A_METRO;
        shapeParedDerecha.setAsBox(mitadAnchoParedDerecha_m, mitadAltoParedDerecha_m); // Ancho y alto
        FixtureDef fixtureDefParedDerecha = new FixtureDef();
        fixtureDefParedDerecha.shape = shapeParedDerecha;
        fixtureDefParedDerecha.restitution = 0f; // Sin rebote
        fixtureDefParedDerecha.density = 0f; // 0 para bodies estáticos
        fixtureDefParedDerecha.friction = 0.2f;
        fixtureDefParedDerecha.filter.categoryBits = manejoEventos.CATEGORIA_PARED; // Pertenece a esta categoría
        fixtureDefParedDerecha.filter.maskBits = manejoEventos.CATEGORIA_BOLA;
        bodyParedDerecha.createFixture(fixtureDefParedDerecha);
        shapeParedDerecha.dispose();    

        // --- Representación Visual de Pared Derecha (Image con textura) ---
        Image imageParedDerecha = new Image(textureParedes);
        float paredDerechaAnchoPx = mitadAnchoParedDerecha_m * 2 / PIXEL_A_METRO;
        float paredDerechaAltoPx = mitadAltoParedDerecha_m * 2 / PIXEL_A_METRO;
        imageParedDerecha.setSize(paredDerechaAnchoPx, paredDerechaAltoPx);
        float paredDerechaX_px = (bodyDefParedDerecha.position.x / PIXEL_A_METRO) - (paredDerechaAnchoPx / 2);
        float paredDerechaY_px = (bodyDefParedDerecha.position.y / PIXEL_A_METRO) - (paredDerechaAltoPx / 2);
        imageParedDerecha.setPosition(paredDerechaX_px, paredDerechaY_px);
        stage.addActor(imageParedDerecha);
        bodyParedDerecha.setUserData(imageParedDerecha);


        /* Obstáculo que cubre el hoyo parte izquierda */
        BodyDef bodyDefParedIzquierda = new BodyDef();
        bodyDefParedIzquierda.position.set(650 * PIXEL_A_METRO, 270 * PIXEL_A_METRO);
        Body bodyParedIzquierda = mundoBox2d.createBody(bodyDefParedIzquierda);
        hashMapBodiesTemporales.put(bodyParedIzquierda, false);
        PolygonShape shapeParedIzquierda = new PolygonShape();
        float mitadAnchoParedIzquierda_m = 20 * PIXEL_A_METRO;
        float mitadAltoParedIzquierda_m = 180 * PIXEL_A_METRO;
        shapeParedIzquierda.setAsBox(mitadAnchoParedIzquierda_m, mitadAltoParedIzquierda_m); // Ancho y alto
        FixtureDef fixtureDefParedIzquierda = new FixtureDef();
        fixtureDefParedIzquierda.shape = shapeParedIzquierda;
        fixtureDefParedIzquierda.restitution = 0f; // Sin rebote
        fixtureDefParedIzquierda.density = 0f; // 0 para bodies estáticos
        fixtureDefParedIzquierda.friction = 0.2f;
        fixtureDefParedIzquierda.filter.categoryBits = manejoEventos.CATEGORIA_PARED; // Pertenece a esta categoría
        fixtureDefParedIzquierda.filter.maskBits = manejoEventos.CATEGORIA_BOLA;
        bodyParedIzquierda.createFixture(fixtureDefParedIzquierda);
        shapeParedIzquierda.dispose();

        // --- Representación Visual de Pared Izquierda (Image con textura) ---
        Image imageParedIzquierda = new Image(textureParedes);
        float paredIzquierdaAnchoPx = mitadAnchoParedIzquierda_m * 2 / PIXEL_A_METRO;
        float paredIzquierdaAltoPx = mitadAltoParedIzquierda_m * 2 / PIXEL_A_METRO;
        imageParedIzquierda.setSize(paredIzquierdaAnchoPx, paredIzquierdaAltoPx);
        float paredIzquierdaX_px = (bodyDefParedIzquierda.position.x / PIXEL_A_METRO) - (paredIzquierdaAnchoPx / 2);
        float paredIzquierdaY_px = (bodyDefParedIzquierda.position.y / PIXEL_A_METRO) - (paredIzquierdaAltoPx / 2);
        imageParedIzquierda.setPosition(paredIzquierdaX_px, paredIzquierdaY_px);
        stage.addActor(imageParedIzquierda);
        bodyParedIzquierda.setUserData(imageParedIzquierda);

        /* SEGUNDA PARED ARRIBA DERECHA*/
        BodyDef bodyDefParedDerecha2 = new BodyDef();
        bodyDefParedDerecha2.position.set(510 * PIXEL_A_METRO, 630 * PIXEL_A_METRO);
        Body bodyParedDerecha2 = mundoBox2d.createBody(bodyDefParedDerecha2);
        hashMapBodiesTemporales.put(bodyParedDerecha2, false);
        PolygonShape shapeParedDerecha2 = new PolygonShape();
        float mitadAnchoParedDerecha2_m = 20 * PIXEL_A_METRO;
        float mitadAltoParedDerecha2_m = 180 * PIXEL_A_METRO;
        shapeParedDerecha2.setAsBox(mitadAnchoParedDerecha2_m, mitadAltoParedDerecha2_m); // Ancho y alto
        FixtureDef fixtureDefParedDerecha2 = new FixtureDef();
        fixtureDefParedDerecha2.shape = shapeParedDerecha2;
        fixtureDefParedDerecha2.restitution =   0f; // Sin rebote
        fixtureDefParedDerecha2.density = 0f; // 0 para bodies estáticos
        fixtureDefParedDerecha2.friction = 0.2f;
        fixtureDefParedDerecha2.filter.categoryBits = manejoEventos.CATEGORIA_PARED; // Pertenece a esta categoría
        fixtureDefParedDerecha2.filter.maskBits = manejoEventos.CATEGORIA_BOLA;
        bodyParedDerecha2.createFixture(fixtureDefParedDerecha2);
        shapeParedDerecha2.dispose();   

        // --- Representación Visual de Pared Derecha 2 (Image con textura) ---
        Image imageParedDerecha2 = new Image(textureParedes);
        float paredDerecha2AnchoPx = mitadAnchoParedDerecha2_m * 2 / PIXEL_A_METRO;
        float paredDerecha2AltoPx = mitadAltoParedDerecha2_m * 2 / PIXEL_A_METRO;
        imageParedDerecha2.setSize(paredDerecha2AnchoPx, paredDerecha2AltoPx);
        float paredDerecha2X_px = (bodyDefParedDerecha2.position.x / PIXEL_A_METRO) - (paredDerecha2AnchoPx / 2);
        float paredDerecha2Y_px = (bodyDefParedDerecha2.position.y / PIXEL_A_METRO) - (paredDerecha2AltoPx / 2);
        imageParedDerecha2.setPosition(paredDerecha2X_px, paredDerecha2Y_px);
        stage.addActor(imageParedDerecha2);
        bodyParedDerecha2.setUserData(imageParedDerecha2);


        /* SEGUNDA PARED ABAJO IZQUIERDA*/

        BodyDef bodyDefParedIzquierda2 = new BodyDef();
        bodyDefParedIzquierda2.position.set(400 * PIXEL_A_METRO, 270 * PIXEL_A_METRO);
        Body bodyParedIzquierda2 = mundoBox2d.createBody(bodyDefParedIzquierda2);
        hashMapBodiesTemporales.put(bodyParedIzquierda2, false);
        PolygonShape shapeParedIzquierda2 = new PolygonShape();
        float mitadAnchoParedIzquierda2_m = 20 * PIXEL_A_METRO;
        float mitadAltoParedIzquierda2_m = 180 * PIXEL_A_METRO;
        shapeParedIzquierda2.setAsBox(mitadAnchoParedIzquierda2_m, mitadAltoParedIzquierda2_m); // Ancho y alto
        FixtureDef fixtureDefParedIzquierda2 = new FixtureDef();
        fixtureDefParedIzquierda2.shape = shapeParedIzquierda2;
        fixtureDefParedIzquierda2.restitution = 0f; // Sin rebote
        fixtureDefParedIzquierda2.density = 0f; // 0 para bodies estáticos
        fixtureDefParedIzquierda2.friction = 0.2f;
        fixtureDefParedIzquierda2.filter.categoryBits = manejoEventos.CATEGORIA_PARED; // Pertenece a esta categoría
        fixtureDefParedIzquierda2.filter.maskBits = manejoEventos.CATEGORIA_BOLA;
        bodyParedIzquierda2.createFixture(fixtureDefParedIzquierda2);
        shapeParedIzquierda2.dispose();
        
        // --- Representación Visual de Pared Izquierda 2 (Image con textura) ---
        Image imageParedIzquierda2 = new Image(textureParedes);
        float paredIzquierda2AnchoPx = mitadAnchoParedIzquierda2_m * 2 / PIXEL_A_METRO;
        float paredIzquierda2AltoPx = mitadAltoParedIzquierda2_m * 2 / PIXEL_A_METRO;
        imageParedIzquierda2.setSize(paredIzquierda2AnchoPx, paredIzquierda2AltoPx);
        float paredIzquierda2X_px = (bodyDefParedIzquierda2.position.x / PIXEL_A_METRO) - (paredIzquierda2AnchoPx / 2);
        float paredIzquierda2Y_px = (bodyDefParedIzquierda2.position.y / PIXEL_A_METRO) - (paredIzquierda2AltoPx / 2);
        imageParedIzquierda2.setPosition(paredIzquierda2X_px, paredIzquierda2Y_px);
        stage.addActor(imageParedIzquierda2);
        bodyParedIzquierda2.setUserData(imageParedIzquierda2);

        return hashMapBodiesTemporales;

    }

}
