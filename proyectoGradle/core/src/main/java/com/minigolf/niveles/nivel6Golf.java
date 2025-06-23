package com.minigolf.niveles;

import java.util.HashMap;

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
    private static final float coordenadaHoyoX = 740;
    private static final float coordenadaHoyoY = 160; 

    // Areas válidas para colocar la bola (en el mundo)
    public static final float minX = 130 * PIXEL_A_METRO, maxX = 200 * PIXEL_A_METRO; // Ajustamos el área válida
    public static final float minY = 700 * PIXEL_A_METRO, maxY = 770 * PIXEL_A_METRO; // Ajustamos el área válida

    public static HashMap<Body, Boolean> crearNivel(Stage stage, World mundoBox2d, Image imagePuntoDeInicio,
            HashMap<Body, Boolean> hashMapBodiesTemporales) {

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

        /* --------- Paredes --------- */

        /* Obstáculo inferior del punto de inicio */
        BodyDef bodyDefParedInferior = new BodyDef();
        bodyDefParedInferior.position.set(390* PIXEL_A_METRO, 500 * PIXEL_A_METRO);
        Body bodyParedInferior = mundoBox2d.createBody(bodyDefParedInferior);
        hashMapBodiesTemporales.put(bodyParedInferior, false);
        PolygonShape shapeParedInferior = new PolygonShape();
        shapeParedInferior.setAsBox(300 * PIXEL_A_METRO, 20 * PIXEL_A_METRO); // Ancho y alto
        FixtureDef fixtureDefParedInferior = new FixtureDef();
        fixtureDefParedInferior.shape = shapeParedInferior;
        fixtureDefParedInferior.restitution = 0f; // Sin rebote
        fixtureDefParedInferior.density = 0f; // 0 para bodies estáticos
        fixtureDefParedInferior.friction = 0.2f;        
        fixtureDefParedInferior.filter.categoryBits = manejoEventos.CATEGORIA_PARED; // Pertenece a esta categoría
        fixtureDefParedInferior.filter.maskBits = manejoEventos.CATEGORIA_BOLA;
        bodyParedInferior.createFixture(fixtureDefParedInferior);
        shapeParedInferior.dispose();

        /* Obstáculo superior al Hoyo */
        BodyDef bodyDefParedSuperior = new BodyDef();
        bodyDefParedSuperior.position.set(510 * PIXEL_A_METRO, 330 * PIXEL_A_METRO);
        Body bodyParedSuperior = mundoBox2d.createBody(bodyDefParedSuperior);
        hashMapBodiesTemporales.put(bodyParedSuperior, false);
        PolygonShape shapeParedSuperior = new PolygonShape();
        shapeParedSuperior.setAsBox(300 * PIXEL_A_METRO, 20  * PIXEL_A_METRO); // Ancho y alto
        FixtureDef fixtureDefParedSuperior = new FixtureDef();
        fixtureDefParedSuperior.shape = shapeParedSuperior;
        fixtureDefParedSuperior.restitution = 0f; // Sin rebote
        fixtureDefParedSuperior.density = 0f; // 0 para bodies estáticos
        fixtureDefParedSuperior.friction = 0.2f;
        fixtureDefParedSuperior.filter.categoryBits = manejoEventos.CATEGORIA_PARED; // Pertenece a esta categoría
        fixtureDefParedSuperior.filter.maskBits = manejoEventos.CATEGORIA_BOLA;
        bodyParedSuperior.createFixture(fixtureDefParedSuperior);
        shapeParedSuperior.dispose();   

        return hashMapBodiesTemporales;

    }
}