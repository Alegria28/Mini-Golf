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

public class nivel9Golf {

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
        Image imageHoyo = new Image(texturaHoyo);
        float hoyoAnchoPx = 2 * 13 * PIXEL_A_METRO / PIXEL_A_METRO; // Diámetro del hoyo
        float hoyoAltoPx = 2 * 13 * PIXEL_A_METRO / PIXEL_A_METRO; // Diámetro del hoyo
        imageHoyo.setSize(hoyoAnchoPx, hoyoAltoPx);
        float hoyoX_px = (bodyDefHoyo.position.x / PIXEL_A_METRO) - (hoyoAnchoPx / 2);
        float hoyoY_px = (bodyDefHoyo.position.y / PIXEL_A_METRO) - (hoyoAltoPx / 2);
        imageHoyo.setPosition(hoyoX_px, hoyoY_px);
        stage.addActor(imageHoyo);
        bodyHoyo.setUserData(imageHoyo);

        /* --------- Paredes --------- */

        /* Obstáculo inferior del punto de inicio */
        BodyDef bodyDefParedInferior = new BodyDef();
        bodyDefParedInferior.position.set(390 * PIXEL_A_METRO, 500 * PIXEL_A_METRO);
        Body bodyParedInferior = mundoBox2d.createBody(bodyDefParedInferior);
        hashMapBodiesTemporales.put(bodyParedInferior, false);
        PolygonShape shapeParedInferior = new PolygonShape();
        float mitadAnchoParedInferior_m = 300 * PIXEL_A_METRO; // Ancho y alto
        float mitadAltoParedInferior_m = 20 * PIXEL_A_METRO;
        shapeParedInferior.setAsBox(mitadAnchoParedInferior_m, mitadAltoParedInferior_m);
        FixtureDef fixtureDefParedInferior = new FixtureDef();
        fixtureDefParedInferior.shape = shapeParedInferior;
        fixtureDefParedInferior.restitution = 0f; // Sin rebote
        fixtureDefParedInferior.density = 0f; // 0 para bodies estáticos
        fixtureDefParedInferior.friction = 0.2f;
        fixtureDefParedInferior.filter.categoryBits = manejoEventos.CATEGORIA_PARED; // Pertenece a esta categoría
        fixtureDefParedInferior.filter.maskBits = manejoEventos.CATEGORIA_BOLA;
        bodyParedInferior.createFixture(fixtureDefParedInferior);
        shapeParedInferior.dispose();

        // --- Representación Visual del Obstáculo Inferior (Image con textura) ---
        Image imageParedInferior = new Image(textureParedes);
        float obstaculoInferiorAnchoPx = mitadAnchoParedInferior_m * 2 / PIXEL_A_METRO;
        float obstaculoInferiorAltoPx = mitadAltoParedInferior_m * 2 / PIXEL_A_METRO;
        imageParedInferior.setSize(obstaculoInferiorAnchoPx, obstaculoInferiorAltoPx);
        float obstaculoInferiorX_px = (bodyDefParedInferior.position.x / PIXEL_A_METRO)
                - (obstaculoInferiorAnchoPx / 2);
        float obstaculoInferiorY_px = (bodyDefParedInferior.position.y / PIXEL_A_METRO) - (obstaculoInferiorAltoPx / 2);
        imageParedInferior.setPosition(obstaculoInferiorX_px, obstaculoInferiorY_px);
        stage.addActor(imageParedInferior);
        bodyParedInferior.setUserData(imageParedInferior);

        /* Obstáculo superior al Hoyo */
        BodyDef bodyDefParedSuperior = new BodyDef();
        bodyDefParedSuperior.position.set(510 * PIXEL_A_METRO, 330 * PIXEL_A_METRO);
        Body bodyParedSuperior = mundoBox2d.createBody(bodyDefParedSuperior);
        hashMapBodiesTemporales.put(bodyParedSuperior, false);
        PolygonShape shapeParedSuperior = new PolygonShape();
        float mitadAnchoParedSuperior_m = 300 * PIXEL_A_METRO;
        float mitadAltoParedSuperior_m = 20 * PIXEL_A_METRO;
        shapeParedSuperior.setAsBox(mitadAnchoParedSuperior_m, mitadAltoParedSuperior_m); // Ancho y alto
        FixtureDef fixtureDefParedSuperior = new FixtureDef();
        fixtureDefParedSuperior.shape = shapeParedSuperior;
        fixtureDefParedSuperior.restitution = 0f; // Sin rebote
        fixtureDefParedSuperior.density = 0f; // 0 para bodies estáticos
        fixtureDefParedSuperior.friction = 0.2f;
        fixtureDefParedSuperior.filter.categoryBits = manejoEventos.CATEGORIA_PARED; // Pertenece a esta categoría
        fixtureDefParedSuperior.filter.maskBits = manejoEventos.CATEGORIA_BOLA;
        bodyParedSuperior.createFixture(fixtureDefParedSuperior);
        shapeParedSuperior.dispose();

        // --- Representación Visual del Obstáculo Superior (Image con textura) ---
        Image imageParedSuperior = new Image(textureParedes);
        float obstaculoSuperiorAnchoPx = mitadAnchoParedSuperior_m * 2 / PIXEL_A_METRO;
        float obstaculoSuperiorAltoPx = mitadAltoParedSuperior_m * 2 / PIXEL_A_METRO;
        imageParedSuperior.setSize(obstaculoSuperiorAnchoPx, obstaculoSuperiorAltoPx);
        float obstaculoSuperiorX_px = (bodyDefParedSuperior.position.x / PIXEL_A_METRO)
                - (obstaculoSuperiorAnchoPx / 2);
        float obstaculoSuperiorY_px = (bodyDefParedSuperior.position.y / PIXEL_A_METRO) - (obstaculoSuperiorAltoPx / 2);
        imageParedSuperior.setPosition(obstaculoSuperiorX_px, obstaculoSuperiorY_px);
        stage.addActor(imageParedSuperior);
        bodyParedSuperior.setUserData(imageParedSuperior);

        /* PAREDES INTERMEDIAS ENTRE LAS PAREDES ANTERIORES */

        BodyDef bodyDefParedIntermedia1 = new BodyDef();
        bodyDefParedIntermedia1.position.set(410 * PIXEL_A_METRO, 430 * PIXEL_A_METRO);
        Body bodyParedIntermedia1 = mundoBox2d.createBody(bodyDefParedIntermedia1);
        hashMapBodiesTemporales.put(bodyParedIntermedia1, false);
        PolygonShape shapeParedIntermedia1 = new PolygonShape();
        float mitadAnchoParedIntermedia1_m = 20 * PIXEL_A_METRO;
        float mitadAltoParedIntermedia1_m = 50 * PIXEL_A_METRO;
        shapeParedIntermedia1.setAsBox(mitadAnchoParedIntermedia1_m, mitadAltoParedIntermedia1_m); // Ancho y alto
        FixtureDef fixtureDefParedIntermedia1 = new FixtureDef();
        fixtureDefParedIntermedia1.shape = shapeParedIntermedia1;
        fixtureDefParedIntermedia1.restitution = 0f; // Sin rebote
        fixtureDefParedIntermedia1.density = 0f; // 0 para bodies estáticos
        fixtureDefParedIntermedia1.friction = 0.2f;
        fixtureDefParedIntermedia1.filter.categoryBits = manejoEventos.CATEGORIA_PARED; // Pertenece a esta categoría
        fixtureDefParedIntermedia1.filter.maskBits = manejoEventos.CATEGORIA_BOLA;
        bodyParedIntermedia1.createFixture(fixtureDefParedIntermedia1);
        shapeParedIntermedia1.dispose();

        // --- Representación Visual de Pared Intermedia 1 (Image con textura) ---
        Image imageParedIntermedia1 = new Image(textureParedes);
        float paredIntermedia1AnchoPx = mitadAnchoParedIntermedia1_m * 2 / PIXEL_A_METRO;
        float paredIntermedia1AltoPx = mitadAltoParedIntermedia1_m * 2 / PIXEL_A_METRO;
        imageParedIntermedia1.setSize(paredIntermedia1AnchoPx, paredIntermedia1AltoPx);
        float paredIntermedia1X_px = (bodyDefParedIntermedia1.position.x / PIXEL_A_METRO)
                - (paredIntermedia1AnchoPx / 2);
        float paredIntermedia1Y_px = (bodyDefParedIntermedia1.position.y / PIXEL_A_METRO)
                - (paredIntermedia1AltoPx / 2);
        imageParedIntermedia1.setPosition(paredIntermedia1X_px, paredIntermedia1Y_px);
        stage.addActor(imageParedIntermedia1);
        bodyParedIntermedia1.setUserData(imageParedIntermedia1);

        BodyDef bodyDefParedIntermedia2 = new BodyDef();
        bodyDefParedIntermedia2.position.set(610 * PIXEL_A_METRO, 400 * PIXEL_A_METRO);
        Body bodyParedIntermedia2 = mundoBox2d.createBody(bodyDefParedIntermedia2);
        hashMapBodiesTemporales.put(bodyParedIntermedia2, false);
        PolygonShape shapeParedIntermedia2 = new PolygonShape();
        float mitadAnchoParedIntermedia2_m = 20 * PIXEL_A_METRO;
        float mitadAltoParedIntermedia2_m = 50 * PIXEL_A_METRO;
        shapeParedIntermedia2.setAsBox(mitadAnchoParedIntermedia2_m, mitadAltoParedIntermedia2_m); // Ancho y alto
        FixtureDef fixtureDefParedIntermedia2 = new FixtureDef();
        fixtureDefParedIntermedia2.shape = shapeParedIntermedia2;
        fixtureDefParedIntermedia2.restitution = 0f; // Sin rebote
        fixtureDefParedIntermedia2.density = 0f; // 0 para bodies estáticos
        fixtureDefParedIntermedia2.friction = 0.2f;
        fixtureDefParedIntermedia2.filter.categoryBits = manejoEventos.CATEGORIA_PARED; // Pertenece a esta categoría
        fixtureDefParedIntermedia2.filter.maskBits = manejoEventos.CATEGORIA_BOLA;
        bodyParedIntermedia2.createFixture(fixtureDefParedIntermedia2);
        shapeParedIntermedia2.dispose();

        // --- Representación Visual de Pared Intermedia 2 (Image con textura) ---
        Image imageParedIntermedia2 = new Image(textureParedes);
        float paredIntermedia2AnchoPx = mitadAnchoParedIntermedia2_m * 2 / PIXEL_A_METRO;
        float paredIntermedia2AltoPx = mitadAltoParedIntermedia2_m * 2 / PIXEL_A_METRO;
        imageParedIntermedia2.setSize(paredIntermedia2AnchoPx, paredIntermedia2AltoPx);
        float paredIntermedia2X_px = (bodyDefParedIntermedia2.position.x / PIXEL_A_METRO)
                - (paredIntermedia2AnchoPx / 2);
        float paredIntermedia2Y_px = (bodyDefParedIntermedia2.position.y / PIXEL_A_METRO)
                - (paredIntermedia2AltoPx / 2);
        imageParedIntermedia2.setPosition(paredIntermedia2X_px, paredIntermedia2Y_px);
        stage.addActor(imageParedIntermedia2);
        bodyParedIntermedia2.setUserData(imageParedIntermedia2);

        /* Obstáculo que cubre al punto de inicio parte derecha */

        BodyDef bodyDefParedDerecha = new BodyDef();
        bodyDefParedDerecha.position.set(280 * PIXEL_A_METRO, 710 * PIXEL_A_METRO);
        Body bodyParedDerecha = mundoBox2d.createBody(bodyDefParedDerecha);
        hashMapBodiesTemporales.put(bodyParedDerecha, false);
        PolygonShape shapeParedDerecha = new PolygonShape();
        float mitadAnchoParedDerecha_m = 20 * PIXEL_A_METRO;
        float mitadAltoParedDerecha_m = 100 * PIXEL_A_METRO;
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
        bodyDefParedIzquierda.position.set(590 * PIXEL_A_METRO, 180 * PIXEL_A_METRO);
        Body bodyParedIzquierda = mundoBox2d.createBody(bodyDefParedIzquierda);
        hashMapBodiesTemporales.put(bodyParedIzquierda, false);
        PolygonShape shapeParedIzquierda = new PolygonShape();
        float mitadAnchoParedIzquierda_m = 20 * PIXEL_A_METRO;
        float mitadAltoParedIzquierda_m = 90 * PIXEL_A_METRO;
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

        // PAREDES AL BORDE DE LAS PAREDES HORIZONTALES
        BodyDef bodyDefParedBordeIzquierda = new BodyDef();
        bodyDefParedBordeIzquierda.position.set(670 * PIXEL_A_METRO, 610 * PIXEL_A_METRO);
        Body bodyParedBordeIzquierda = mundoBox2d.createBody(bodyDefParedBordeIzquierda);
        hashMapBodiesTemporales.put(bodyParedBordeIzquierda, false);
        PolygonShape shapeParedBordeIzquierda = new PolygonShape();
        float mitadAnchoParedBordeIzquierda_m = 20 * PIXEL_A_METRO;
        float mitadAltoParedBordeIzquierda_m = 90 * PIXEL_A_METRO;
        shapeParedBordeIzquierda.setAsBox(mitadAnchoParedBordeIzquierda_m, mitadAltoParedBordeIzquierda_m); // Ancho y alto
        FixtureDef fixtureDefParedBordeIzquierda = new FixtureDef();
        fixtureDefParedBordeIzquierda.shape = shapeParedBordeIzquierda;
        fixtureDefParedBordeIzquierda.restitution = 0f; // Sin rebote
        fixtureDefParedBordeIzquierda.density = 0f; // 0 para bodies estáticos
        fixtureDefParedBordeIzquierda.friction = 0.2f;
        fixtureDefParedBordeIzquierda.filter.categoryBits = manejoEventos.CATEGORIA_PARED; // Pertenece a esta categoría
        fixtureDefParedBordeIzquierda.filter.maskBits = manejoEventos.CATEGORIA_BOLA;
        bodyParedBordeIzquierda.createFixture(fixtureDefParedBordeIzquierda);
        shapeParedBordeIzquierda.dispose();

        // --- Representación Visual de Pared Borde Izquierda (Image con textura) ---
        Image imageParedBordeIzquierda = new Image(textureParedes);
        float paredBordeIzquierdaAnchoPx = mitadAnchoParedBordeIzquierda_m * 2 / PIXEL_A_METRO;
        float paredBordeIzquierdaAltoPx = mitadAltoParedBordeIzquierda_m * 2 / PIXEL_A_METRO;
        imageParedBordeIzquierda.setSize(paredBordeIzquierdaAnchoPx, paredBordeIzquierdaAltoPx);
        float paredBordeIzquierdaX_px = (bodyDefParedBordeIzquierda.position.x / PIXEL_A_METRO)
                - (paredBordeIzquierdaAnchoPx / 2);
        float paredBordeIzquierdaY_px = (bodyDefParedBordeIzquierda.position.y / PIXEL_A_METRO)
                - (paredBordeIzquierdaAltoPx / 2);
        imageParedBordeIzquierda.setPosition(paredBordeIzquierdaX_px, paredBordeIzquierdaY_px);
        stage.addActor(imageParedBordeIzquierda);
        bodyParedBordeIzquierda.setUserData(imageParedBordeIzquierda);

        BodyDef bodyDefParedBordeDerecha = new BodyDef();
        bodyDefParedBordeDerecha.position.set(230 * PIXEL_A_METRO, 220 * PIXEL_A_METRO);
        Body bodyParedBordeDerecha = mundoBox2d.createBody(bodyDefParedBordeDerecha);
        hashMapBodiesTemporales.put(bodyParedBordeDerecha, false);
        PolygonShape shapeParedBordeDerecha = new PolygonShape();
        float mitadAnchoParedBordeDerecha_m = 20 * PIXEL_A_METRO;
        float mitadAltoParedBordeDerecha_m = 90 * PIXEL_A_METRO;
        shapeParedBordeDerecha.setAsBox(mitadAnchoParedBordeDerecha_m, mitadAltoParedBordeDerecha_m); // Ancho y alto
        FixtureDef fixtureDefParedBordeDerecha = new FixtureDef();
        fixtureDefParedBordeDerecha.shape = shapeParedBordeDerecha;
        fixtureDefParedBordeDerecha.restitution = 0f; // Sin rebote
        fixtureDefParedBordeDerecha.density = 0f; // 0 para bodies estáticos
        fixtureDefParedBordeDerecha.friction = 0.2f;
        fixtureDefParedBordeDerecha.filter.categoryBits = manejoEventos.CATEGORIA_PARED; // Pertenece a esta categoría
        fixtureDefParedBordeDerecha.filter.maskBits = manejoEventos.CATEGORIA_BOLA;
        bodyParedBordeDerecha.createFixture(fixtureDefParedBordeDerecha);
        shapeParedBordeDerecha.dispose();

        // --- Representación Visual de Pared Borde Derecha (Image con textura) ---
        Image imageParedBordeDerecha = new Image(textureParedes);
        float paredBordeDerechaAnchoPx = mitadAnchoParedBordeDerecha_m * 2 / PIXEL_A_METRO;
        float paredBordeDerechaAltoPx = mitadAltoParedBordeDerecha_m * 2 / PIXEL_A_METRO;
        imageParedBordeDerecha.setSize(paredBordeDerechaAnchoPx, paredBordeDerechaAltoPx);
        float paredBordeDerechaX_px = (bodyDefParedBordeDerecha.position.x / PIXEL_A_METRO)
                - (paredBordeDerechaAnchoPx / 2);
        float paredBordeDerechaY_px = (bodyDefParedBordeDerecha.position.y / PIXEL_A_METRO)
                - (paredBordeDerechaAltoPx / 2);
        imageParedBordeDerecha.setPosition(paredBordeDerechaX_px, paredBordeDerechaY_px);
        stage.addActor(imageParedBordeDerecha);
        bodyParedBordeDerecha.setUserData(imageParedBordeDerecha);

        return hashMapBodiesTemporales;

    }
}