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

public class nivel6Golf {

    // Factor de conversion para el mundo de Box2D, donde 100px = 1m 
    private static final float PIXEL_A_METRO = 0.01f;

    // Atributos
    public static final float coordenadaInicioX = 130;
    private static final float coordenadaInicioY = 700;
    public static final float coordenadaHoyoX = 740;
    private static final float coordenadaHoyoY = 160;

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

        return hashMapBodiesTemporales;
    }
}
