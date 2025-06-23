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

public class nivel5Golf {

    // Factor de conversion para el mundo de Box2D, donde 100px = 1m 
    private static final float PIXEL_A_METRO = 0.01f;


    // Atributos
    public static final float coordenadaInicioX = 700; 
    private static final float coordenadaInicioY = 700; 
    private static final float coordenadaHoyoX = 130;
    private static final float coordenadaHoyoY = 130; 

    // Areas válidas para colocar la bola (en el mundo)
    public static final float minX = 700 * PIXEL_A_METRO, maxX = 770 * PIXEL_A_METRO; // Ajustamos el área válida
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

        /* Obstáculos que protegen al hoyo */
        
        /* PARTE DERECHA */

        // Definimos un Body para el obstáculo
        BodyDef bodyDefObstaculo3 = new BodyDef();
        // Establecemos su posicion en el mundo (lado derecho del hoyo)
        bodyDefObstaculo3.position.set(230 * PIXEL_A_METRO, 190* PIXEL_A_METRO); // Ajustamos la posición
        // Creamos un Body a partir de la definición y lo agregamos a nuestro mundo
        Body bodyObstaculo3 = mundoBox2d.createBody(bodyDefObstaculo3);
        // Agregamos esta pared a la cola para liberarlo después (al acabar el nivel)
        hashMapBodiesTemporales.put(bodyObstaculo3, false);
        // Creamos un polígono en general
        PolygonShape shapeObstaculo3 = new PolygonShape();
        // Establecemos el polígono con la forma de un rectángulo vertical
        shapeObstaculo3.setAsBox(20 * PIXEL_A_METRO, 60 * PIXEL_A_METRO); // Ajustamos las dimensiones
        // Creamos una FixtureDef para definir las propiedades físicas del obstáculo
        FixtureDef fixtureDefObstaculo3 = new FixtureDef();
        // Ponemos la forma creada
        fixtureDefObstaculo3.shape = shapeObstaculo3;
        fixtureDefObstaculo3.restitution = 1f; // Rebote        return hashMapBodiesTemporales;

        fixtureDefObstaculo3.density = 0f; // 0 para
        fixtureDefObstaculo3.friction = 0.2f;
        // Configuración de colisiones
        fixtureDefObstaculo3.filter.categoryBits = manejoEventos.CATEGORIA_PARED
        ; // Pertenece a esta categoría
        fixtureDefObstaculo3.filter.maskBits = manejoEventos.CATEGORIA_BOLA

        ; // Puede colisionar con las bolas
        // Creamos la fixture y la unimos al cuerpo del obstáculo
        bodyObstaculo3.createFixture(fixtureDefObstaculo3);
        // Liberamos el polígono
        shapeObstaculo3.dispose();

        /* PARTE SUPERIOR */

        // Definimos un Body para el obstáculo
        BodyDef bodyDefObstaculo4 = new BodyDef();
        // Establecemos su posicion en el mundo (lado derecho del hoyo)
        bodyDefObstaculo4.position.set(190 * PIXEL_A_METRO, 230* PIXEL_A_METRO); // Ajustamos la posición
        // Creamos un Body a partir de la definición y lo agregamos a nuestro mundo
        Body bodyObstaculo4= mundoBox2d.createBody(bodyDefObstaculo4);
        // Agregamos esta pared a la cola para liberarlo después (al acabar el nivel)
        hashMapBodiesTemporales.put(bodyObstaculo4, false);
        // Creamos un polígono en general
        PolygonShape shapeObstaculo4 = new PolygonShape();
        // Establecemos el polígono con la forma de un rectángulo vertical
        shapeObstaculo3.setAsBox(60 * PIXEL_A_METRO, 20 * PIXEL_A_METRO); // Ajustamos las dimensiones
        // Creamos una FixtureDef para definir las propiedades físicas del obstáculo
        FixtureDef fixtureDefObstaculo4 = new FixtureDef();
        // Ponemos la forma creada
        fixtureDefObstaculo4.shape = shapeObstaculo4;
        fixtureDefObstaculo4.restitution = 1f; // Rebote        return hashMapBodiesTemporales;

        fixtureDefObstaculo4.density = 0f; // 0 para
        fixtureDefObstaculo4.friction = 0.2f;
        // Configuración de colisiones
        fixtureDefObstaculo4.filter.categoryBits = manejoEventos.CATEGORIA_PARED; // Pertenece a esta categoría
        fixtureDefObstaculo4.filter.maskBits = manejoEventos.CATEGORIA_BOLA; // Puede colisionar con las bolas
        // Creamos la fixture y la unimos al cuerpo del obstáculo
        bodyObstaculo4.createFixture(fixtureDefObstaculo4);
        // Liberamos el polígono
        shapeObstaculo4.dispose();
        
        /* Obstáculos que protegen la posicion inicial */
        /* PARTE IZQUIERDA */

        // Definimos un Body para el obstáculo
        BodyDef bodyDefObstaculo1 = new BodyDef();
        // Establecemos su posicion en el mundo (lado izquierdo del hoyo)
        bodyDefObstaculo1.position.set( 650 * PIXEL_A_METRO, 700* PIXEL_A_METRO); // Ajustamos la posición
        // Creamos un Body a partir de la definición y lo agregamos a nuestro mundo
        Body bodyObstaculo1 = mundoBox2d.createBody(bodyDefObstaculo1);
        // Agregamos esta pared a la cola para liberarlo después (al acabar el nivel)
        hashMapBodiesTemporales.put(bodyObstaculo1, false);
        // Creamos un polígono en general
        PolygonShape shapeObstaculo1 = new PolygonShape();
        // Establecemos el polígono con la forma de un rectángulo vertical
        shapeObstaculo1.setAsBox(20 * PIXEL_A_METRO, 70 * PIXEL_A_METRO); // Ajustamos las dimensiones
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

        /* PARTE INFERIOR */
        // Definimos un Body para el obstáculo
        BodyDef bodyDefObstaculo2 = new BodyDef();
        // Establecemos su posicion en el mundo (lado izquierdo del hoyo)
        bodyDefObstaculo2.position.set(700 * PIXEL_A_METRO, 650* PIXEL_A_METRO); // Ajustamos la posición
        // Creamos un Body a partir de la definición y lo agregamos a nuestro mundo
        Body bodyObstaculo2 = mundoBox2d.createBody(bodyDefObstaculo2);
        // Agregamos esta pared a la cola para liberarlo después (al acabar el nivel)
        hashMapBodiesTemporales.put(bodyObstaculo2, false);
        // Creamos un polígono en general
        PolygonShape shapeObstaculo2 = new PolygonShape();
        // Establecemos el polígono con la forma de un rectángulo vertical
        shapeObstaculo2.setAsBox(70 * PIXEL_A_METRO, 20 * PIXEL_A_METRO); // Ajustamos las dimensiones
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
        return hashMapBodiesTemporales;

    }
}