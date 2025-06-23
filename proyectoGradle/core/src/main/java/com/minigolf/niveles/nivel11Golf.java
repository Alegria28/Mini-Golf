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

public class nivel11Golf {

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

        /* Obstáculo que cubre al punto de inicio parte derecha */

        BodyDef bodyDefParedDerecha = new BodyDef();
        bodyDefParedDerecha.position.set(280 * PIXEL_A_METRO, 630 * PIXEL_A_METRO);
        Body bodyParedDerecha = mundoBox2d.createBody(bodyDefParedDerecha);
        hashMapBodiesTemporales.put(bodyParedDerecha, false);
        PolygonShape shapeParedDerecha = new PolygonShape();
        shapeParedDerecha.setAsBox(20 * PIXEL_A_METRO, 180 * PIXEL_A_METRO); // Ancho y alto
        FixtureDef fixtureDefParedDerecha = new FixtureDef();
        fixtureDefParedDerecha.shape = shapeParedDerecha;
        fixtureDefParedDerecha.restitution = 0f; // Sin rebote
        fixtureDefParedDerecha.density = 0f; // 0 para bodies estáticos
        fixtureDefParedDerecha.friction = 0.2f;
        fixtureDefParedDerecha.filter.categoryBits = manejoEventos.CATEGORIA_PARED; // Pertenece a esta categoría
        fixtureDefParedDerecha.filter.maskBits = manejoEventos.CATEGORIA_BOLA;
        bodyParedDerecha.createFixture(fixtureDefParedDerecha);
        shapeParedDerecha.dispose();    

        /* Obstáculo que cubre el hoyo parte izquierda */
        BodyDef bodyDefParedIzquierda = new BodyDef();
        bodyDefParedIzquierda.position.set(650 * PIXEL_A_METRO, 270 * PIXEL_A_METRO);
        Body bodyParedIzquierda = mundoBox2d.createBody(bodyDefParedIzquierda);
        hashMapBodiesTemporales.put(bodyParedIzquierda, false);
        PolygonShape shapeParedIzquierda = new PolygonShape();
        shapeParedIzquierda.setAsBox(20 * PIXEL_A_METRO, 180 * PIXEL_A_METRO); // Ancho y alto
        FixtureDef fixtureDefParedIzquierda = new FixtureDef();
        fixtureDefParedIzquierda.shape = shapeParedIzquierda;
        fixtureDefParedIzquierda.restitution = 0f; // Sin rebote
        fixtureDefParedIzquierda.density = 0f; // 0 para bodies estáticos
        fixtureDefParedIzquierda.friction = 0.2f;
        fixtureDefParedIzquierda.filter.categoryBits = manejoEventos.CATEGORIA_PARED; // Pertenece a esta categoría
        fixtureDefParedIzquierda.filter.maskBits = manejoEventos.CATEGORIA_BOLA;
        bodyParedIzquierda.createFixture(fixtureDefParedIzquierda);
        shapeParedIzquierda.dispose();

        /* SEGUNDA PARED ARRIBA DERECHA*/
        BodyDef bodyDefParedDerecha2 = new BodyDef();
        bodyDefParedDerecha2.position.set(510 * PIXEL_A_METRO, 630 * PIXEL_A_METRO);
        Body bodyParedDerecha2 = mundoBox2d.createBody(bodyDefParedDerecha2);
        hashMapBodiesTemporales.put(bodyParedDerecha2, false);
        PolygonShape shapeParedDerecha2 = new PolygonShape();
        shapeParedDerecha2.setAsBox(20 * PIXEL_A_METRO, 180 * PIXEL_A_METRO); // Ancho y alto
        FixtureDef fixtureDefParedDerecha2 = new FixtureDef();
        fixtureDefParedDerecha2.shape = shapeParedDerecha2;
        fixtureDefParedDerecha2.restitution =   0f; // Sin rebote
        fixtureDefParedDerecha2.density = 0f; // 0 para bodies estáticos
        fixtureDefParedDerecha2.friction = 0.2f;
        fixtureDefParedDerecha2.filter.categoryBits = manejoEventos.CATEGORIA_PARED; // Pertenece a esta categoría
        fixtureDefParedDerecha2.filter.maskBits = manejoEventos.CATEGORIA_BOLA;
        bodyParedDerecha2.createFixture(fixtureDefParedDerecha2);
        shapeParedDerecha2.dispose();   

        /* SEGUNDA PARED ABAJO IZQUIERDA*/

        BodyDef bodyDefParedIzquierda2 = new BodyDef();
        bodyDefParedIzquierda2.position.set(400 * PIXEL_A_METRO, 270 * PIXEL_A_METRO);
        Body bodyParedIzquierda2 = mundoBox2d.createBody(bodyDefParedIzquierda2);
        hashMapBodiesTemporales.put(bodyParedIzquierda2, false);
        PolygonShape shapeParedIzquierda2 = new PolygonShape();
        shapeParedIzquierda2.setAsBox(20 * PIXEL_A_METRO, 180 * PIXEL_A_METRO); // Ancho y alto
        FixtureDef fixtureDefParedIzquierda2 = new FixtureDef();
        fixtureDefParedIzquierda2.shape = shapeParedIzquierda2;
        fixtureDefParedIzquierda2.restitution = 0f; // Sin rebote
        fixtureDefParedIzquierda2.density = 0f; // 0 para bodies estáticos
        fixtureDefParedIzquierda2.friction = 0.2f;
        fixtureDefParedIzquierda2.filter.categoryBits = manejoEventos.CATEGORIA_PARED; // Pertenece a esta categoría
        fixtureDefParedIzquierda2.filter.maskBits = manejoEventos.CATEGORIA_BOLA;
        bodyParedIzquierda2.createFixture(fixtureDefParedIzquierda2);
        shapeParedIzquierda2.dispose();
        
        return hashMapBodiesTemporales;

    }
}