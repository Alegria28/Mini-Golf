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

public class nivel14Golf {

    // Factor de conversion para el mundo de Box2D, donde 100px = 1m 
    private static final float PIXEL_A_METRO = 0.01f;


    // Atributos
    public static final float coordenadaInicioX = 130; 
    private static final float coordenadaInicioY = 700; 
    private static final float coordenadaHoyoX = 740;
    private static final float coordenadaHoyoY = 160; 

    // HOYO FALSO
    public static final float coordenadaHoyoFalsoX = 740; // Coordenada X del hoyo falso
    public static final float coordenadaHoyoFalsoY = 700; // Coordenada Y del hoyo falso

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
        bodyDefParedDerecha2.position.set(510 * PIXEL_A_METRO, 480 * PIXEL_A_METRO);
        Body bodyParedDerecha2 = mundoBox2d.createBody(bodyDefParedDerecha2);
        hashMapBodiesTemporales.put(bodyParedDerecha2, false);
        PolygonShape shapeParedDerecha2 = new PolygonShape();
        shapeParedDerecha2.setAsBox(20 * PIXEL_A_METRO, 330 * PIXEL_A_METRO); // Ancho y alto
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
        bodyDefParedIzquierda2.position.set(400 * PIXEL_A_METRO, 420* PIXEL_A_METRO);
        Body bodyParedIzquierda2 = mundoBox2d.createBody(bodyDefParedIzquierda2);
        hashMapBodiesTemporales.put(bodyParedIzquierda2, false);
        PolygonShape shapeParedIzquierda2 = new PolygonShape();
        shapeParedIzquierda2.setAsBox(20 * PIXEL_A_METRO, 330* PIXEL_A_METRO); // Ancho y alto
        FixtureDef fixtureDefParedIzquierda2 = new FixtureDef();
        fixtureDefParedIzquierda2.shape = shapeParedIzquierda2;
        fixtureDefParedIzquierda2.restitution = 0f; // Sin rebote
        fixtureDefParedIzquierda2.density = 0f; // 0 para bodies estáticos
        fixtureDefParedIzquierda2.friction = 0.2f;
        fixtureDefParedIzquierda2.filter.categoryBits = manejoEventos.CATEGORIA_PARED; // Pertenece a esta categoría
        fixtureDefParedIzquierda2.filter.maskBits = manejoEventos.CATEGORIA_BOLA;
        bodyParedIzquierda2.createFixture(fixtureDefParedIzquierda2);
        shapeParedIzquierda2.dispose();

        /* PAREDES HORIZONTALES */

        /* Pared superior */
        BodyDef bodyDefParedSuperior = new BodyDef();
        bodyDefParedSuperior.position.set(140 * PIXEL_A_METRO, 600 * PIXEL_A_METRO);
        Body bodyParedSuperior = mundoBox2d.createBody(bodyDefParedSuperior);
        hashMapBodiesTemporales.put(bodyParedSuperior, false);
        PolygonShape shapeParedSuperior = new PolygonShape();
        shapeParedSuperior.setAsBox(50 * PIXEL_A_METRO, 20 * PIXEL_A_METRO); // Ancho y alto
        FixtureDef fixtureDefParedSuperior = new FixtureDef();
        fixtureDefParedSuperior.shape = shapeParedSuperior;
        fixtureDefParedSuperior.restitution = 0f; // Sin rebote
        fixtureDefParedSuperior.density = 0f; // 0 para bodies estáticos
        fixtureDefParedSuperior.friction = 0.2f;
        fixtureDefParedSuperior.filter.categoryBits = manejoEventos.CATEGORIA_PARED; // Pertenece a esta categoría
        fixtureDefParedSuperior.filter.maskBits = manejoEventos.CATEGORIA_BOLA;
        bodyParedSuperior.createFixture(fixtureDefParedSuperior);
        shapeParedSuperior.dispose();

        /* Pared inferior */
        BodyDef bodyDefParedInferior = new BodyDef();
        bodyDefParedInferior.position.set(210* PIXEL_A_METRO, 470 * PIXEL_A_METRO);
        Body bodyParedInferior = mundoBox2d.createBody(bodyDefParedInferior);
        hashMapBodiesTemporales.put(bodyParedInferior, false);
        PolygonShape shapeParedInferior = new PolygonShape();
        shapeParedInferior.setAsBox(50 * PIXEL_A_METRO, 20 * PIXEL_A_METRO); // Ancho y alto
        FixtureDef fixtureDefParedInferior = new FixtureDef();
        fixtureDefParedInferior.shape = shapeParedInferior;
        fixtureDefParedInferior.restitution = 0f; // Sin rebote
        fixtureDefParedInferior.density = 0f; // 0 para bodies estáticos
        fixtureDefParedInferior.friction = 0.2f;
        fixtureDefParedInferior.filter.categoryBits = manejoEventos.CATEGORIA_PARED; // Pertenece a esta categoría
        fixtureDefParedInferior.filter.maskBits = manejoEventos.CATEGORIA_BOLA;
        bodyParedInferior.createFixture(fixtureDefParedInferior);
        shapeParedInferior.dispose();

        /* Pared derecha */
        BodyDef bodyDefParedDerecha3 = new BodyDef();
        bodyDefParedDerecha3.position.set(760 * PIXEL_A_METRO, 430 * PIXEL_A_METRO);
        Body bodyParedDerecha3 = mundoBox2d.createBody(bodyDefParedDerecha3);
        hashMapBodiesTemporales.put(bodyParedDerecha3, false);
        PolygonShape shapeParedDerecha3 = new PolygonShape();
        shapeParedDerecha3.setAsBox(50 * PIXEL_A_METRO, 20 * PIXEL_A_METRO); // Ancho y alto
        FixtureDef fixtureDefParedDerecha3 = new FixtureDef();
        fixtureDefParedDerecha3.shape = shapeParedDerecha3;
        fixtureDefParedDerecha3.restitution = 0f; // Sin rebote
        fixtureDefParedDerecha3.density = 0f; // 0 para bodies estáticos
        fixtureDefParedDerecha3.friction = 0.2f;
        fixtureDefParedDerecha3.filter.categoryBits = manejoEventos.CATEGORIA_PARED; // Pertenece a esta categoría
        fixtureDefParedDerecha3.filter.maskBits = manejoEventos.CATEGORIA_BOLA;
        bodyParedDerecha3.createFixture(fixtureDefParedDerecha3);
        shapeParedDerecha3.dispose();   

        
        /* Pared izquierda */
        BodyDef bodyDefParedIzquierda3 = new BodyDef();
        bodyDefParedIzquierda3.position.set(720 * PIXEL_A_METRO, 260 * PIXEL_A_METRO);
        Body bodyParedIzquierda3 = mundoBox2d.createBody(bodyDefParedIzquierda3);
        hashMapBodiesTemporales.put(bodyParedIzquierda3, false);
        PolygonShape shapeParedIzquierda3 = new PolygonShape();
        shapeParedIzquierda3.setAsBox(50 * PIXEL_A_METRO, 20 * PIXEL_A_METRO); // Ancho y alto
        FixtureDef fixtureDefParedIzquierda3 = new FixtureDef();
        fixtureDefParedIzquierda3.shape = shapeParedIzquierda3;
        fixtureDefParedIzquierda3.restitution = 0f; // Sin rebote
        fixtureDefParedIzquierda3.density = 0f; // 0 para bodies estáticos
        fixtureDefParedIzquierda3.friction = 0.2f;
        fixtureDefParedIzquierda3.filter.categoryBits = manejoEventos.CATEGORIA_PARED; // Pertenece a esta categoría
        fixtureDefParedIzquierda3.filter.maskBits = manejoEventos.CATEGORIA_BOLA;
        bodyParedIzquierda3.createFixture(fixtureDefParedIzquierda3);
        shapeParedIzquierda3.dispose(); 

        /* PAREDES ESPEJO DE LAS VERTICALES */

        /* Pared superior espejo */
        BodyDef bodyDefParedSuperiorEspejo = new BodyDef();
        bodyDefParedSuperiorEspejo.position.set(280 * PIXEL_A_METRO, 250 * PIXEL_A_METRO);
        Body bodyParedSuperiorEspejo = mundoBox2d.createBody(bodyDefParedSuperiorEspejo);
        hashMapBodiesTemporales.put(bodyParedSuperiorEspejo, false);
        PolygonShape shapeParedSuperiorEspejo = new PolygonShape();
        shapeParedSuperiorEspejo.setAsBox(20 * PIXEL_A_METRO, 160 * PIXEL_A_METRO); // Ancho y alto
        FixtureDef fixtureDefParedSuperiorEspejo = new FixtureDef();
        fixtureDefParedSuperiorEspejo.shape = shapeParedSuperiorEspejo;
        fixtureDefParedSuperiorEspejo.restitution = 0f; // Sin rebote
        fixtureDefParedSuperiorEspejo.density = 0f; // 0 para bodies estáticos
        fixtureDefParedSuperiorEspejo.friction = 0.2f;
        fixtureDefParedSuperiorEspejo.filter.categoryBits = manejoEventos.CATEGORIA_PARED; // Pertenece a esta categoría
        fixtureDefParedSuperiorEspejo.filter.maskBits = manejoEventos.CATEGORIA_BOLA;
        bodyParedSuperiorEspejo.createFixture(fixtureDefParedSuperiorEspejo);
        shapeParedSuperiorEspejo.dispose();

        /* Pared inferior espejo que cubre al HOYO */
        BodyDef bodyDefParedInferiorEspejo = new BodyDef();
        bodyDefParedInferiorEspejo.position.set(650 * PIXEL_A_METRO, 650* PIXEL_A_METRO);
        Body bodyParedInferiorEspejo = mundoBox2d.createBody(bodyDefParedInferiorEspejo);
        hashMapBodiesTemporales.put(bodyParedInferiorEspejo, false);
        PolygonShape shapeParedInferiorEspejo = new PolygonShape();
        shapeParedInferiorEspejo.setAsBox(20 * PIXEL_A_METRO, 160 * PIXEL_A_METRO); // Ancho y alto
        FixtureDef fixtureDefParedInferiorEspejo = new FixtureDef();
        fixtureDefParedInferiorEspejo.shape = shapeParedInferiorEspejo;
        fixtureDefParedInferiorEspejo.restitution = 0f; // Sin rebote
        fixtureDefParedInferiorEspejo.density = 0f; // 0 para bodies estáticos
        fixtureDefParedInferiorEspejo.friction = 0.2f;
        fixtureDefParedInferiorEspejo.filter.categoryBits = manejoEventos.CATEGORIA_PARED; // Pertenece a esta categoría
        fixtureDefParedInferiorEspejo.filter.maskBits = manejoEventos.CATEGORIA_BOLA;
        bodyParedInferiorEspejo.createFixture(fixtureDefParedInferiorEspejo);
        shapeParedInferiorEspejo.dispose(); 

        return hashMapBodiesTemporales;

    }
}