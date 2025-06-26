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

public class nivel14Golf {

    // Factor de conversion para el mundo de Box2D, donde 100px = 1m 
    private static final float PIXEL_A_METRO = 0.01f;

    // Atributos
    public static final float coordenadaInicioX = 130;
    private static final float coordenadaInicioY = 700;
    public static final float coordenadaHoyoX = 740;
    public static final float coordenadaHoyoY = 160;

    // HOYO FALSO
    public static final float coordenadaHoyoFalsoX = 740; // Coordenada X del hoyo falso
    public static final float coordenadaHoyoFalsoY = 700; // Coordenada Y del hoyo falso

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
        Image imageHoyo = new Image(texturaHoyo);
        float hoyoAnchoPx = 2 * 13 * PIXEL_A_METRO / PIXEL_A_METRO; // Diámetro del hoyo
        float hoyoAltoPx = 2 * 13 * PIXEL_A_METRO / PIXEL_A_METRO; // Diámetro del hoyo
        imageHoyo.setSize(hoyoAnchoPx, hoyoAltoPx);
        float hoyoX_px = (bodyDefHoyo.position.x / PIXEL_A_METRO) - (hoyoAnchoPx / 2);
        float hoyoY_px = (bodyDefHoyo.position.y / PIXEL_A_METRO) - (hoyoAltoPx / 2);
        imageHoyo.setPosition(hoyoX_px, hoyoY_px);
        stage.addActor(imageHoyo);
        bodyHoyo.setUserData(imageHoyo);

        /* HOYO FALSO */
        // Definimos un Body para el hoyo falso
        BodyDef bodyDefHoyoFalso = new BodyDef();
        // Establecemos su posición en el mundo (hoyo falso)
        bodyDefHoyoFalso.position.set(coordenadaHoyoFalsoX * PIXEL_A_METRO, coordenadaHoyoFalsoY * PIXEL_A_METRO);
        // Creamos un Body a partir de la definición y lo agregamos a nuestro mundo
        Body bodyHoyoFalso = mundoBox2d.createBody(bodyDefHoyoFalso);
        // Agregamos este hoyo falso a la cola para liberarlo después (al acabar el nivel)
        hashMapBodiesTemporales.put(bodyHoyoFalso, false);
        // Creamos un polígono en general para el hoyo falso
        CircleShape shapeHoyoFalso = new CircleShape();
        shapeHoyoFalso.setRadius(13 * PIXEL_A_METRO); // Hoyo falso mas grande que una bola
        // Creamos una FixtureDef para definir las propiedades físicas del hoyo falso
        FixtureDef fixtureDefHoyoFalso = new FixtureDef();
        // Ponemos la forma creada
        fixtureDefHoyoFalso.shape = shapeHoyoFalso;
        fixtureDefHoyoFalso.restitution = 1f; // Rebote
        fixtureDefHoyoFalso.density = 0f; // 0 para bodies
        fixtureDefHoyoFalso.friction = 0.2f;
        // Configuración de colisiones para el hoyo falso
        fixtureDefHoyoFalso.filter.maskBits = manejoEventos.CATEGORIA_BOLA; // Puede colisionar con las bolas

        // Creamos la fixture y la unimos al cuerpo del hoyo falso
        bodyHoyoFalso.createFixture(fixtureDefHoyoFalso);
        // Liberamos el polígono del hoyo falso
        shapeHoyoFalso.dispose();

        // --- Representación Visual del Hoyo Falso (Image con textura) ---
        Image imageHoyoFalso = new Image(texturaHoyo);
        float hoyoFalsoAnchoPx = 2 * 13 * PIXEL_A_METRO / PIXEL_A_METRO; // Diámetro del hoyo
        float hoyoFalsoAltoPx = 2 * 13 * PIXEL_A_METRO / PIXEL_A_METRO; // Diámetro del hoyo
        imageHoyoFalso.setSize(hoyoFalsoAnchoPx, hoyoFalsoAltoPx);
        float hoyoFalsoX_px = (bodyDefHoyoFalso.position.x / PIXEL_A_METRO) - (hoyoFalsoAnchoPx / 2);
        float hoyoFalsoY_px = (bodyDefHoyoFalso.position.y / PIXEL_A_METRO) - (hoyoFalsoAltoPx / 2);
        imageHoyoFalso.setPosition(hoyoFalsoX_px, hoyoFalsoY_px);
        stage.addActor(imageHoyoFalso);
        bodyHoyoFalso.setUserData(imageHoyoFalso);

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
        bodyDefParedDerecha2.position.set(510 * PIXEL_A_METRO, 480 * PIXEL_A_METRO);
        Body bodyParedDerecha2 = mundoBox2d.createBody(bodyDefParedDerecha2);
        hashMapBodiesTemporales.put(bodyParedDerecha2, false);
        PolygonShape shapeParedDerecha2 = new PolygonShape();
        float mitadAnchoParedDerecha2_m = 20 * PIXEL_A_METRO;
        float mitadAltoParedDerecha2_m = 330 * PIXEL_A_METRO;
        shapeParedDerecha2.setAsBox(mitadAnchoParedDerecha2_m, mitadAltoParedDerecha2_m); // Ancho y alto
        FixtureDef fixtureDefParedDerecha2 = new FixtureDef();
        fixtureDefParedDerecha2.shape = shapeParedDerecha2;
        fixtureDefParedDerecha2.restitution = 0f; // Sin rebote
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
        bodyDefParedIzquierda2.position.set(400 * PIXEL_A_METRO, 420 * PIXEL_A_METRO);
        Body bodyParedIzquierda2 = mundoBox2d.createBody(bodyDefParedIzquierda2);
        hashMapBodiesTemporales.put(bodyParedIzquierda2, false);
        PolygonShape shapeParedIzquierda2 = new PolygonShape();
        float mitadAnchoParedIzquierda2_m = 20 * PIXEL_A_METRO;
        float mitadAltoParedIzquierda2_m = 330 * PIXEL_A_METRO;
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

        /* PAREDES HORIZONTALES */

        /* Pared superior */
        BodyDef bodyDefParedSuperior = new BodyDef();
        bodyDefParedSuperior.position.set(140 * PIXEL_A_METRO, 600 * PIXEL_A_METRO);
        Body bodyParedSuperior = mundoBox2d.createBody(bodyDefParedSuperior);
        hashMapBodiesTemporales.put(bodyParedSuperior, false);
        PolygonShape shapeParedSuperior = new PolygonShape();
        float mitadAnchoParedSuperior_m = 50 * PIXEL_A_METRO;
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

        // --- Representación Visual de Pared Superior (Image con textura) ---
        Image imageParedSuperior = new Image(textureParedes);
        float paredSuperiorAnchoPx = mitadAnchoParedSuperior_m * 2 / PIXEL_A_METRO;
        float paredSuperiorAltoPx = mitadAltoParedSuperior_m * 2 / PIXEL_A_METRO;
        imageParedSuperior.setSize(paredSuperiorAnchoPx, paredSuperiorAltoPx);
        float paredSuperiorX_px = (bodyDefParedSuperior.position.x / PIXEL_A_METRO) - (paredSuperiorAnchoPx / 2);
        float paredSuperiorY_px = (bodyDefParedSuperior.position.y / PIXEL_A_METRO) - (paredSuperiorAltoPx / 2);
        imageParedSuperior.setPosition(paredSuperiorX_px, paredSuperiorY_px);
        stage.addActor(imageParedSuperior);
        bodyParedSuperior.setUserData(imageParedSuperior);

        /* Pared inferior */
        BodyDef bodyDefParedInferior = new BodyDef();
        bodyDefParedInferior.position.set(210 * PIXEL_A_METRO, 470 * PIXEL_A_METRO);
        Body bodyParedInferior = mundoBox2d.createBody(bodyDefParedInferior);
        hashMapBodiesTemporales.put(bodyParedInferior, false);
        PolygonShape shapeParedInferior = new PolygonShape();
        float mitadAnchoParedInferior_m = 50 * PIXEL_A_METRO;
        float mitadAltoParedInferior_m = 20 * PIXEL_A_METRO;
        shapeParedInferior.setAsBox(mitadAnchoParedInferior_m, mitadAltoParedInferior_m); // Ancho y alto
        FixtureDef fixtureDefParedInferior = new FixtureDef();
        fixtureDefParedInferior.shape = shapeParedInferior;
        fixtureDefParedInferior.restitution = 0f; // Sin rebote
        fixtureDefParedInferior.density = 0f; // 0 para bodies estáticos
        fixtureDefParedInferior.friction = 0.2f;
        fixtureDefParedInferior.filter.categoryBits = manejoEventos.CATEGORIA_PARED; // Pertenece a esta categoría
        fixtureDefParedInferior.filter.maskBits = manejoEventos.CATEGORIA_BOLA;
        bodyParedInferior.createFixture(fixtureDefParedInferior);
        shapeParedInferior.dispose();

        // --- Representación Visual de Pared Inferior (Image con textura) ---
        Image imageParedInferior = new Image(textureParedes);
        float paredInferiorAnchoPx = mitadAnchoParedInferior_m * 2 / PIXEL_A_METRO;
        float paredInferiorAltoPx = mitadAltoParedInferior_m * 2 / PIXEL_A_METRO;
        imageParedInferior.setSize(paredInferiorAnchoPx, paredInferiorAltoPx);
        float paredInferiorX_px = (bodyDefParedInferior.position.x / PIXEL_A_METRO) - (paredInferiorAnchoPx / 2);
        float paredInferiorY_px = (bodyDefParedInferior.position.y / PIXEL_A_METRO) - (paredInferiorAltoPx / 2);
        imageParedInferior.setPosition(paredInferiorX_px, paredInferiorY_px);
        stage.addActor(imageParedInferior);
        bodyParedInferior.setUserData(imageParedInferior);

        /* Pared derecha */
        BodyDef bodyDefParedDerecha3 = new BodyDef();
        bodyDefParedDerecha3.position.set(760 * PIXEL_A_METRO, 430 * PIXEL_A_METRO);
        Body bodyParedDerecha3 = mundoBox2d.createBody(bodyDefParedDerecha3);
        hashMapBodiesTemporales.put(bodyParedDerecha3, false);
        PolygonShape shapeParedDerecha3 = new PolygonShape();
        float mitadAnchoParedDerecha3_m = 50 * PIXEL_A_METRO;
        float mitadAltoParedDerecha3_m = 20 * PIXEL_A_METRO;
        shapeParedDerecha3.setAsBox(mitadAnchoParedDerecha3_m, mitadAltoParedDerecha3_m); // Ancho y alto
        FixtureDef fixtureDefParedDerecha3 = new FixtureDef();
        fixtureDefParedDerecha3.shape = shapeParedDerecha3;
        fixtureDefParedDerecha3.restitution = 0f; // Sin rebote
        fixtureDefParedDerecha3.density = 0f; // 0 para bodies estáticos
        fixtureDefParedDerecha3.friction = 0.2f;
        fixtureDefParedDerecha3.filter.categoryBits = manejoEventos.CATEGORIA_PARED; // Pertenece a esta categoría
        fixtureDefParedDerecha3.filter.maskBits = manejoEventos.CATEGORIA_BOLA;
        bodyParedDerecha3.createFixture(fixtureDefParedDerecha3);
        shapeParedDerecha3.dispose();

        // --- Representación Visual de Pared Derecha 3 (Image con textura) ---
        Image imageParedDerecha3 = new Image(textureParedes);
        float paredDerecha3AnchoPx = mitadAnchoParedDerecha3_m * 2 / PIXEL_A_METRO;
        float paredDerecha3AltoPx = mitadAltoParedDerecha3_m * 2 / PIXEL_A_METRO;
        imageParedDerecha3.setSize(paredDerecha3AnchoPx, paredDerecha3AltoPx);
        float paredDerecha3X_px = (bodyDefParedDerecha3.position.x / PIXEL_A_METRO) - (paredDerecha3AnchoPx / 2);
        float paredDerecha3Y_px = (bodyDefParedDerecha3.position.y / PIXEL_A_METRO) - (paredDerecha3AltoPx / 2);
        imageParedDerecha3.setPosition(paredDerecha3X_px, paredDerecha3Y_px);
        stage.addActor(imageParedDerecha3);
        bodyParedDerecha3.setUserData(imageParedDerecha3);

        /* Pared izquierda */
        BodyDef bodyDefParedIzquierda3 = new BodyDef();
        bodyDefParedIzquierda3.position.set(720 * PIXEL_A_METRO, 260 * PIXEL_A_METRO);
        Body bodyParedIzquierda3 = mundoBox2d.createBody(bodyDefParedIzquierda3);
        hashMapBodiesTemporales.put(bodyParedIzquierda3, false);
        PolygonShape shapeParedIzquierda3 = new PolygonShape();
        float mitadAnchoParedIzquierda3_m = 50 * PIXEL_A_METRO;
        float mitadAltoParedIzquierda3_m = 20 * PIXEL_A_METRO;
        shapeParedIzquierda3.setAsBox(mitadAnchoParedIzquierda3_m, mitadAltoParedIzquierda3_m); // Ancho y alto
        FixtureDef fixtureDefParedIzquierda3 = new FixtureDef();
        fixtureDefParedIzquierda3.shape = shapeParedIzquierda3;
        fixtureDefParedIzquierda3.restitution = 0f; // Sin rebote
        fixtureDefParedIzquierda3.density = 0f; // 0 para bodies estáticos
        fixtureDefParedIzquierda3.friction = 0.2f;
        fixtureDefParedIzquierda3.filter.categoryBits = manejoEventos.CATEGORIA_PARED; // Pertenece a esta categoría
        fixtureDefParedIzquierda3.filter.maskBits = manejoEventos.CATEGORIA_BOLA;
        bodyParedIzquierda3.createFixture(fixtureDefParedIzquierda3);
        shapeParedIzquierda3.dispose();

        // --- Representación Visual de Pared Izquierda 3 (Image con textura) ---
        Image imageParedIzquierda3 = new Image(textureParedes);
        float paredIzquierda3AnchoPx = mitadAnchoParedIzquierda3_m * 2 / PIXEL_A_METRO;
        float paredIzquierda3AltoPx = mitadAltoParedIzquierda3_m * 2 / PIXEL_A_METRO;
        imageParedIzquierda3.setSize(paredIzquierda3AnchoPx, paredIzquierda3AltoPx);
        float paredIzquierda3X_px = (bodyDefParedIzquierda3.position.x / PIXEL_A_METRO) - (paredIzquierda3AnchoPx / 2);
        float paredIzquierda3Y_px = (bodyDefParedIzquierda3.position.y / PIXEL_A_METRO) - (paredIzquierda3AltoPx / 2);
        imageParedIzquierda3.setPosition(paredIzquierda3X_px, paredIzquierda3Y_px);
        stage.addActor(imageParedIzquierda3);
        bodyParedIzquierda3.setUserData(imageParedIzquierda3);

        /* PAREDES ESPEJO DE LAS VERTICALES */

        /* Pared superior espejo */
        BodyDef bodyDefParedSuperiorEspejo = new BodyDef();
        bodyDefParedSuperiorEspejo.position.set(280 * PIXEL_A_METRO, 250 * PIXEL_A_METRO);
        Body bodyParedSuperiorEspejo = mundoBox2d.createBody(bodyDefParedSuperiorEspejo);
        hashMapBodiesTemporales.put(bodyParedSuperiorEspejo, false);
        PolygonShape shapeParedSuperiorEspejo = new PolygonShape();
        float mitadAnchoParedSuperiorEspejo_m = 20 * PIXEL_A_METRO;
        float mitadAltoParedSuperiorEspejo_m = 160 * PIXEL_A_METRO;
        shapeParedSuperiorEspejo.setAsBox(mitadAnchoParedSuperiorEspejo_m, mitadAltoParedSuperiorEspejo_m); // Ancho y alto
        FixtureDef fixtureDefParedSuperiorEspejo = new FixtureDef();
        fixtureDefParedSuperiorEspejo.shape = shapeParedSuperiorEspejo;
        fixtureDefParedSuperiorEspejo.restitution = 0f; // Sin rebote
        fixtureDefParedSuperiorEspejo.density = 0f; // 0 para bodies estáticos
        fixtureDefParedSuperiorEspejo.friction = 0.2f;
        fixtureDefParedSuperiorEspejo.filter.categoryBits = manejoEventos.CATEGORIA_PARED; // Pertenece a esta categoría
        fixtureDefParedSuperiorEspejo.filter.maskBits = manejoEventos.CATEGORIA_BOLA;
        bodyParedSuperiorEspejo.createFixture(fixtureDefParedSuperiorEspejo);
        shapeParedSuperiorEspejo.dispose();

        // --- Representación Visual de Pared Superior Espejo (Image con textura) ---
        Image imageParedSuperiorEspejo = new Image(textureParedes);
        float paredSuperiorEspejoAnchoPx = mitadAnchoParedSuperiorEspejo_m * 2 / PIXEL_A_METRO;
        float paredSuperiorEspejoAltoPx = mitadAltoParedSuperiorEspejo_m * 2 / PIXEL_A_METRO;
        imageParedSuperiorEspejo.setSize(paredSuperiorEspejoAnchoPx, paredSuperiorEspejoAltoPx);
        float paredSuperiorEspejoX_px = (bodyDefParedSuperiorEspejo.position.x / PIXEL_A_METRO)
                - (paredSuperiorEspejoAnchoPx / 2);
        float paredSuperiorEspejoY_px = (bodyDefParedSuperiorEspejo.position.y / PIXEL_A_METRO)
                - (paredSuperiorEspejoAltoPx / 2);
        imageParedSuperiorEspejo.setPosition(paredSuperiorEspejoX_px, paredSuperiorEspejoY_px);
        stage.addActor(imageParedSuperiorEspejo);
        bodyParedSuperiorEspejo.setUserData(imageParedSuperiorEspejo);

        /* Pared inferior espejo que cubre al HOYO */
        BodyDef bodyDefParedInferiorEspejo = new BodyDef();
        bodyDefParedInferiorEspejo.position.set(650 * PIXEL_A_METRO, 650 * PIXEL_A_METRO);
        Body bodyParedInferiorEspejo = mundoBox2d.createBody(bodyDefParedInferiorEspejo);
        hashMapBodiesTemporales.put(bodyParedInferiorEspejo, false);
        PolygonShape shapeParedInferiorEspejo = new PolygonShape();
        float mitadAnchoParedInferiorEspejo_m = 20 * PIXEL_A_METRO;
        float mitadAltoParedInferiorEspejo_m = 160 * PIXEL_A_METRO;
        shapeParedInferiorEspejo.setAsBox(mitadAnchoParedInferiorEspejo_m, mitadAltoParedInferiorEspejo_m); // Ancho y alto
        FixtureDef fixtureDefParedInferiorEspejo = new FixtureDef();
        fixtureDefParedInferiorEspejo.shape = shapeParedInferiorEspejo;
        fixtureDefParedInferiorEspejo.restitution = 0f; // Sin rebote
        fixtureDefParedInferiorEspejo.density = 0f; // 0 para bodies estáticos
        fixtureDefParedInferiorEspejo.friction = 0.2f;
        fixtureDefParedInferiorEspejo.filter.categoryBits = manejoEventos.CATEGORIA_PARED; // Pertenece a esta categoría
        fixtureDefParedInferiorEspejo.filter.maskBits = manejoEventos.CATEGORIA_BOLA;
        bodyParedInferiorEspejo.createFixture(fixtureDefParedInferiorEspejo);
        shapeParedInferiorEspejo.dispose();

        // --- Representación Visual de Pared Inferior Espejo (Image con textura) ---
        Image imageParedInferiorEspejo = new Image(textureParedes);
        float paredInferiorEspejoAnchoPx = mitadAnchoParedInferiorEspejo_m * 2 / PIXEL_A_METRO;
        float paredInferiorEspejoAltoPx = mitadAltoParedInferiorEspejo_m * 2 / PIXEL_A_METRO;
        imageParedInferiorEspejo.setSize(paredInferiorEspejoAnchoPx, paredInferiorEspejoAltoPx);
        float paredInferiorEspejoX_px = (bodyDefParedInferiorEspejo.position.x / PIXEL_A_METRO)
                - (paredInferiorEspejoAnchoPx / 2);
        float paredInferiorEspejoY_px = (bodyDefParedInferiorEspejo.position.y / PIXEL_A_METRO)
                - (paredInferiorEspejoAltoPx / 2);
        imageParedInferiorEspejo.setPosition(paredInferiorEspejoX_px, paredInferiorEspejoY_px);
        stage.addActor(imageParedInferiorEspejo);
        bodyParedInferiorEspejo.setUserData(imageParedInferiorEspejo);

        return hashMapBodiesTemporales;

    }
}