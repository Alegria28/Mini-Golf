package com.minigolf.niveles;

import java.util.ArrayList;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class nivel1Golf {

    // Factor de conversion para el mundo de Box2D, donde 100px = 1m 
    private static final float PIXEL_A_METRO = 0.01f;

    // Atributos
    public static final float coordenadaInicioX = 130;
    private static final float coordenadaInicioY = 130;
    private static final float coordenadaHoyoX = 750;
    private static final float coordenadaHoyoY = 750;

    // Areas validas para colocar la bola (en el mundo)
    public static final float minX = 130 * PIXEL_A_METRO, maxX = 200 * PIXEL_A_METRO; // 200 ya que el tamaño de la imagen es de 70px
    public static final float minY = 130 * PIXEL_A_METRO, maxY = 200 * PIXEL_A_METRO; // 200 ya que el tamaño de la imagen es de 70px

    private static ArrayList<Body> arrayListBodiesTemporales = new ArrayList<Body>();

    public static ArrayList<Body> crearNivel(Stage stage, World mundoBox2d, Image imagePuntoDeInicio) {

        // Quitamos la imagen del stage (si es que estaba en el)
        imagePuntoDeInicio.remove();

        // Cambiamos la posición de la imagen
        imagePuntoDeInicio.setPosition(coordenadaInicioX, coordenadaInicioY);

        // Agregamos la imagen a nuestro table con cierto tamaño
        stage.addActor(imagePuntoDeInicio);

        return arrayListBodiesTemporales;
    }
}