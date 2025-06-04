package com.minigolf;

import com.badlogic.gdx.graphics.Color;
import java.util.ArrayList;

// Clase que va a representar a cada jugador
public class Jugador {

    // Atributos del jugador
    private String nombre;
    private Color colorBola;
    private ArrayList<Integer> puntajePorHoyo;
    private int puntajeTotal;
    private int strokesActuales;

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

    // Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setColorBola(Color colorBola) {
        this.colorBola = colorBola;
    }

    public void incrementarStrokes() {
        this.strokesActuales++;
    }

    // Métodos de la clase
    public void reiniciarStrokes() {
        this.strokesActuales = 0;
    }

    public void terminarHoyo(int strokes) {
        puntajePorHoyo.add(strokes);
        puntajeTotal += strokes;
        reiniciarStrokes();
    }

    public int getPuntajeHoyo(int numeroHoyo) {
        // Verificamos si ya hemos jugado ese numero de hoyo
        if (numeroHoyo >= 0 && numeroHoyo < puntajePorHoyo.size()) {
            return puntajePorHoyo.get(numeroHoyo);
        }
        // En caso de que ese hoyo no este en nuestro ArrayList
        return -1;
    }

    // Sobrescribimos el método para imprimir
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Jugador {\n");
        sb.append("  Nombre: ").append(nombre).append(",\n");
        sb.append("  Color de Bola: ").append(colorBola).append(",\n");
        sb.append("  Puntaje por Hoyo: ").append(puntajePorHoyo).append(",\n");
        sb.append("  Puntaje Total: ").append(puntajeTotal).append(",\n");
        sb.append("  Strokes Actuales: ").append(strokesActuales).append("\n");
        sb.append("}");
        return sb.toString();
    }
}
