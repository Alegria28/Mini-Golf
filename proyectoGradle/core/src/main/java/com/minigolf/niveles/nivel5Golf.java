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

public class nivel5Golf {

    // Factor de conversion para el mundo de Box2D, donde 100px = 1m 
    private static final float PIXEL_A_METRO = 0.01f;

    // Atributos
    public static final float coordenadaInicioX = 700;
    private static final float coordenadaInicioY = 700;
    public static final float coordenadaHoyoX = 130;
    public static final float coordenadaHoyoY = 130;

    // Areas válidas para colocar la bola (en el mundo)
    public static final float minX = 700 * PIXEL_A_METRO, maxX = 770 * PIXEL_A_METRO; // Ajustamos el área válida
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

        /* Obstáculos que protegen al hoyo */

        /* PARTE DERECHA */

        // Definimos un Body para el obstáculo
        BodyDef bodyDefObstaculo3 = new BodyDef();
        // Establecemos su posicion en el mundo (lado derecho del hoyo)
        bodyDefObstaculo3.position.set(230 * PIXEL_A_METRO, 190 * PIXEL_A_METRO); // Ajustamos la posición
        // Creamos un Body a partir de la definición y lo agregamos a nuestro mundo
        Body bodyObstaculo3 = mundoBox2d.createBody(bodyDefObstaculo3);
        // Agregamos esta pared a la cola para liberarlo después (al acabar el nivel)
        hashMapBodiesTemporales.put(bodyObstaculo3, false);
        // Creamos un polígono en general
        PolygonShape shapeObstaculo3 = new PolygonShape();
        // Establecemos el polígono con la forma de un rectángulo vertical
        float mitadAnchoObstaculo3_m = 20 * PIXEL_A_METRO; // 40px de ancho total
        float mitadAltoObstaculo3_m = 60 * PIXEL_A_METRO; // 120px de alto total
        shapeObstaculo3.setAsBox(mitadAnchoObstaculo3_m, mitadAltoObstaculo3_m); // Ajustamos las dimensiones
        // Creamos una FixtureDef para definir las propiedades físicas del obstáculo
        FixtureDef fixtureDefObstaculo3 = new FixtureDef();
        // Ponemos la forma creada
        fixtureDefObstaculo3.shape = shapeObstaculo3;
        fixtureDefObstaculo3.restitution = 1f; // Rebote
        fixtureDefObstaculo3.density = 0f; // 0 para bodies estáticos
        fixtureDefObstaculo3.friction = 0.2f;
        // Configuración de colisiones
        fixtureDefObstaculo3.filter.categoryBits = manejoEventos.CATEGORIA_PARED; // Pertenece a esta categoría
        fixtureDefObstaculo3.filter.maskBits = manejoEventos.CATEGORIA_BOLA; // Puede colisionar con las bolas
        // Creamos la fixture y la unimos al cuerpo del obstáculo
        bodyObstaculo3.createFixture(fixtureDefObstaculo3);
        // Liberamos el polígono
        shapeObstaculo3.dispose();

        // --- Representación Visual del Obstáculo 3 (Image con textura) ---
        Image imageObstaculo3 = new Image(textureParedes);
        float obstaculo3AnchoPx = mitadAnchoObstaculo3_m * 2 / PIXEL_A_METRO;
        float obstaculo3AltoPx = mitadAltoObstaculo3_m * 2 / PIXEL_A_METRO;
        imageObstaculo3.setSize(obstaculo3AnchoPx, obstaculo3AltoPx);
        float obstaculo3X_px = (bodyDefObstaculo3.position.x / PIXEL_A_METRO) - (obstaculo3AnchoPx / 2);
        float obstaculo3Y_px = (bodyDefObstaculo3.position.y / PIXEL_A_METRO) - (obstaculo3AltoPx / 2);
        imageObstaculo3.setPosition(obstaculo3X_px, obstaculo3Y_px);
        stage.addActor(imageObstaculo3);
        bodyObstaculo3.setUserData(imageObstaculo3);

        /* PARTE SUPERIOR */

        // Definimos un Body para el obstáculo
        BodyDef bodyDefObstaculo4 = new BodyDef();
        // Establecemos su posicion en el mundo (lado derecho del hoyo)
        bodyDefObstaculo4.position.set(190 * PIXEL_A_METRO, 230 * PIXEL_A_METRO); // Ajustamos la posición
        // Creamos un Body a partir de la definición y lo agregamos a nuestro mundo
        Body bodyObstaculo4 = mundoBox2d.createBody(bodyDefObstaculo4);
        // Agregamos esta pared a la cola para liberarlo después (al acabar el nivel)
        hashMapBodiesTemporales.put(bodyObstaculo4, false);
        // Creamos un polígono en general
        PolygonShape shapeObstaculo4 = new PolygonShape(); // Corregido: Usar shapeObstaculo4
        // Establecemos el polígono con la forma de un rectángulo vertical
        float mitadAnchoObstaculo4_m = 60 * PIXEL_A_METRO; // 120px de ancho total
        float mitadAltoObstaculo4_m = 20 * PIXEL_A_METRO; // 40px de alto total
        shapeObstaculo4.setAsBox(mitadAnchoObstaculo4_m, mitadAltoObstaculo4_m); // Ajustamos las dimensiones
        // Creamos una FixtureDef para definir las propiedades físicas del obstáculo
        FixtureDef fixtureDefObstaculo4 = new FixtureDef();
        // Ponemos la forma creada
        fixtureDefObstaculo4.shape = shapeObstaculo4;
        fixtureDefObstaculo4.restitution = 1f; // Rebote
        fixtureDefObstaculo4.density = 0f; // 0 para bodies estáticos
        fixtureDefObstaculo4.friction = 0.2f;
        // Configuración de colisiones
        fixtureDefObstaculo4.filter.categoryBits = manejoEventos.CATEGORIA_PARED; // Pertenece a esta categoría
        fixtureDefObstaculo4.filter.maskBits = manejoEventos.CATEGORIA_BOLA; // Puede colisionar con las bolas
        // Creamos la fixture y la unimos al cuerpo del obstáculo
        bodyObstaculo4.createFixture(fixtureDefObstaculo4);
        // Liberamos el polígono
        shapeObstaculo4.dispose();

        // --- Representación Visual del Obstáculo 4 (Image con textura) ---
        Image imageObstaculo4 = new Image(textureParedes);
        float obstaculo4AnchoPx = mitadAnchoObstaculo4_m * 2 / PIXEL_A_METRO;
        float obstaculo4AltoPx = mitadAltoObstaculo4_m * 2 / PIXEL_A_METRO;
        imageObstaculo4.setSize(obstaculo4AnchoPx, obstaculo4AltoPx);
        float obstaculo4X_px = (bodyDefObstaculo4.position.x / PIXEL_A_METRO) - (obstaculo4AnchoPx / 2);
        float obstaculo4Y_px = (bodyDefObstaculo4.position.y / PIXEL_A_METRO) - (obstaculo4AltoPx / 2);
        imageObstaculo4.setPosition(obstaculo4X_px, obstaculo4Y_px);
        stage.addActor(imageObstaculo4);
        bodyObstaculo4.setUserData(imageObstaculo4);

        /* Obstáculos que protegen la posicion inicial */
        /* PARTE IZQUIERDA */

        // Definimos un Body para el obstáculo
        BodyDef bodyDefObstaculo1 = new BodyDef();
        // Establecemos su posicion en el mundo (lado izquierdo del hoyo)
        bodyDefObstaculo1.position.set(650 * PIXEL_A_METRO, 700 * PIXEL_A_METRO); // Ajustamos la posición
        // Creamos un Body a partir de la definición y lo agregamos a nuestro mundo
        Body bodyObstaculo1 = mundoBox2d.createBody(bodyDefObstaculo1);
        // Agregamos esta pared a la cola para liberarlo después (al acabar el nivel)
        hashMapBodiesTemporales.put(bodyObstaculo1, false);
        // Creamos un polígono en general
        PolygonShape shapeObstaculo1 = new PolygonShape();
        // Establecemos el polígono con la forma de un rectángulo vertical
        float mitadAnchoObstaculo1_m = 20 * PIXEL_A_METRO;
        float mitadAltoObstaculo1_m = 70 * PIXEL_A_METRO;
        shapeObstaculo1.setAsBox(mitadAnchoObstaculo1_m, mitadAltoObstaculo1_m); // Ajustamos las dimensiones
        // Creamos una FixtureDef para definir las propiedades físicas del obstáculo
        FixtureDef fixtureDefObstaculo1 = new FixtureDef();
        // Ponemos la forma creada
        fixtureDefObstaculo1.shape = shapeObstaculo1;
        fixtureDefObstaculo1.restitution = 1f; // Rebote
        fixtureDefObstaculo1.density = 0f; // 0 para bodies estáticos
        fixtureDefObstaculo1.friction = 0.2f;
        // Configuración de colisiones
        fixtureDefObstaculo1.filter.categoryBits = manejoEventos.CATEGORIA_PARED; // Pertenece a esta categoría
        fixtureDefObstaculo1.filter.maskBits = manejoEventos.CATEGORIA_BOLA; // Puede colisionar con las bolas
        // Creamos la fixture y la unimos al cuerpo del obstáculo
        bodyObstaculo1.createFixture(fixtureDefObstaculo1);
        // Liberamos el polígono
        shapeObstaculo1.dispose();

        // --- Representación Visual del Obstáculo 1 (Image con textura) ---
        Image imageObstaculo1 = new Image(textureParedes);
        float obstaculo1AnchoPx = mitadAnchoObstaculo1_m * 2 / PIXEL_A_METRO;
        float obstaculo1AltoPx = mitadAltoObstaculo1_m * 2 / PIXEL_A_METRO;
        imageObstaculo1.setSize(obstaculo1AnchoPx, obstaculo1AltoPx);
        float obstaculo1X_px = (bodyDefObstaculo1.position.x / PIXEL_A_METRO) - (obstaculo1AnchoPx / 2);
        float obstaculo1Y_px = (bodyDefObstaculo1.position.y / PIXEL_A_METRO) - (obstaculo1AltoPx / 2);
        imageObstaculo1.setPosition(obstaculo1X_px, obstaculo1Y_px);
        stage.addActor(imageObstaculo1);
        bodyObstaculo1.setUserData(imageObstaculo1);

        /* PARTE INFERIOR */
        // Definimos un Body para el obstáculo
        BodyDef bodyDefObstaculo2 = new BodyDef();
        // Establecemos su posicion en el mundo (lado izquierdo del hoyo)
        bodyDefObstaculo2.position.set(700 * PIXEL_A_METRO, 650 * PIXEL_A_METRO); // Ajustamos la posición
        // Creamos un Body a partir de la definición y lo agregamos a nuestro mundo
        Body bodyObstaculo2 = mundoBox2d.createBody(bodyDefObstaculo2);
        // Agregamos esta pared a la cola para liberarlo después (al acabar el nivel)
        hashMapBodiesTemporales.put(bodyObstaculo2, false);
        // Creamos un polígono en general
        PolygonShape shapeObstaculo2 = new PolygonShape();
        // Establecemos el polígono con la forma de un rectángulo vertical
        float mitadAnchoObstaculo2_m = 70 * PIXEL_A_METRO;
        float mitadAltoObstaculo2_m = 20 * PIXEL_A_METRO;
        shapeObstaculo2.setAsBox(mitadAnchoObstaculo2_m, mitadAltoObstaculo2_m); // Ajustamos las dimensiones
        // Creamos una FixtureDef para definir las propiedades físicas del obstáculo
        FixtureDef fixtureDefObstaculo2 = new FixtureDef();
        // Ponemos la forma creada
        fixtureDefObstaculo2.shape = shapeObstaculo2;
        fixtureDefObstaculo2.restitution = 1f; // Rebote
        fixtureDefObstaculo2.density = 0f; // 0 para bodies estáticos
        fixtureDefObstaculo2.friction = 0.2f;
        // Configuración de colisiones
        fixtureDefObstaculo2.filter.categoryBits = manejoEventos.CATEGORIA_PARED; // Pertenece a esta categoría
        fixtureDefObstaculo2.filter.maskBits = manejoEventos.CATEGORIA_BOLA; // Puede colisionar con las bolas
        // Creamos la fixture y la unimos al cuerpo del obstáculo
        bodyObstaculo2.createFixture(fixtureDefObstaculo2);
        // Liberamos el polígono
        shapeObstaculo2.dispose();

        // --- Representación Visual del Obstáculo 2 (Image con textura) ---
        Image imageObstaculo2 = new Image(textureParedes);
        float obstaculo2AnchoPx = mitadAnchoObstaculo2_m * 2 / PIXEL_A_METRO;
        float obstaculo2AltoPx = mitadAltoObstaculo2_m * 2 / PIXEL_A_METRO;
        imageObstaculo2.setSize(obstaculo2AnchoPx, obstaculo2AltoPx);
        float obstaculo2X_px = (bodyDefObstaculo2.position.x / PIXEL_A_METRO) - (obstaculo2AnchoPx / 2);
        float obstaculo2Y_px = (bodyDefObstaculo2.position.y / PIXEL_A_METRO) - (obstaculo2AltoPx / 2);
        imageObstaculo2.setPosition(obstaculo2X_px, obstaculo2Y_px);
        stage.addActor(imageObstaculo2);
        bodyObstaculo2.setUserData(imageObstaculo2);

        return hashMapBodiesTemporales;
    }
}