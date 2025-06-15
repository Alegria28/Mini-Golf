package com.minigolf.models;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;

// Clase que va a representar a cada jugador
public class Jugador {

    // Atributos del jugador
    private String nombre;
    private Color colorBola;
    private ArrayList<Integer> puntajePorHoyo;
    private int puntajeTotal;
    private int strokesActuales;

    private Body bolaJugador;
    private boolean puedeGolpear = false;
    private boolean hoyoTerminado = false;

    // Constructor de la clase
    public Jugador(String nombre, Color colorBola) {
        this.nombre = nombre;
        this.colorBola = colorBola;
        this.puntajePorHoyo = new ArrayList<Integer>();
        this.puntajeTotal = 0;
        this.strokesActuales = 0;
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public Color getColorBola() {
        return colorBola;
    }

    public ArrayList<Integer> getPuntajePorHoyo() {
        return puntajePorHoyo;
    }

    public int getPuntajeTotal() {
        return puntajeTotal;
    }

    public int getStrokesActuales() {
        return strokesActuales;
    }

    public Body getBolaJugador() {
        return bolaJugador;
    }

    public int getPuntajeHoyo(int numeroHoyo) {
        // Verificamos si ya hemos jugado ese numero de hoyo
        if (numeroHoyo >= 0 && numeroHoyo < puntajePorHoyo.size()) {
            return puntajePorHoyo.get(numeroHoyo);
        }
        // En caso de que ese hoyo no este en nuestro ArrayList
        return -1;
    }

    public boolean isPuedeGolpear() {
        return puedeGolpear;
    }

    public boolean isHoyoTerminado() {
        return this.hoyoTerminado;
    }

    // Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setColorBola(Color colorBola) {
        this.colorBola = colorBola;
    }

    public void setBolaJugador(Body bolaJugador) {
        this.bolaJugador = bolaJugador;
    }

    public void setPuedeGolpear(boolean puedeGolpear) {
        this.puedeGolpear = puedeGolpear;
    }

    public void setHoyoTerminado(boolean hoyoTerminado) {
        this.hoyoTerminado = hoyoTerminado;
    }

    // Métodos de la clase
    public void incrementarStrokes() {
        this.strokesActuales++;
    }

    public void terminarHoyo() {
        this.hoyoTerminado = true;

        // Agregamos este puntaje al hoyo
        puntajePorHoyo.add(strokesActuales);
        // Aumentamos nuestro contador global
        puntajeTotal += strokesActuales;
        // Reiniciamos para el siguiente hoyo
        this.strokesActuales = 0;
        // El jugador ya no tiene bola
        this.bolaJugador = null;
        // Ya no puede golpear
        this.puedeGolpear = false;
    }

    // Sobrescribimos el método para imprimir
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Jugador {\n");
        sb.append("  Nombre: ").append(nombre).append(",\n");
        sb.append("  Puntaje por Hoyo: ").append(puntajePorHoyo).append(",\n");
        sb.append("  Puntaje Total: ").append(puntajeTotal).append("\n");
        sb.append("}");
        return sb.toString();
    }
}
