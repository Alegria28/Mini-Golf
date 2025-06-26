package com.minigolf.handlers;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.minigolf.models.Jugador;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;

public class manejoColisiones implements ContactListener {

	// Atributos
	private ArrayList<Jugador> jugadores;
	private HashMap<Body, Boolean> hashMapBodiesTemporales;
	private World mundoBox2d;
	private manejoEventos eventos;

	// Puedes ajustar este umbral de velocidad según lo necesites
    private static final float UMBRAL_VELOCIDAD_ATRAVESAR = 4.5f; // Velocidad mínima para 'ignorar' la zona
    // Puedes ajustar este umbral angular para determinar qué tan 'contraria' es la dirección
    private static final float UMBRAL_ANGULO_CONTRARIO = 45.0f; // Ángulo en grados (ej: >150 grados es casi opuesto)



	// Constructor
	public manejoColisiones(ArrayList<Jugador> jugadores, HashMap<Body, Boolean> hashMapBodiesTemporales,
			World mundoBox2d, manejoEventos eventos) {
		this.jugadores = jugadores;
		this.hashMapBodiesTemporales = hashMapBodiesTemporales;
		this.mundoBox2d = mundoBox2d;
		this.eventos = eventos;
	}

	/** Called when two fixtures begin to touch. */
	@Override
	public void beginContact(Contact contact) {

		// Obtenemos los fixtures involucrados en el contacto
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		// Obtenemos los bodies involucrados en el contacto
		Body bodyA = fixtureA.getBody();
		Body bodyB = fixtureB.getBody(); 
		// Posteriormente, obtenemos el objeto que tiene la categoría de los bodies involucrados
		Filter categoriaA = fixtureA.getFilterData();
		Filter categoriaB = fixtureB.getFilterData();

		// Verificamos si la colisión fue entre una bola y el hoyo o entre el hoyo y la bola
		if ((categoriaA.categoryBits == manejoEventos.CATEGORIA_BOLA && categoriaB.categoryBits == manejoEventos.CATEGORIA_HOYO) ||
            (categoriaA.categoryBits == manejoEventos.CATEGORIA_HOYO && categoriaB.categoryBits == manejoEventos.CATEGORIA_BOLA)) {

            System.out.println("Colisión bola-hoyo detectada");

            // Identificamos cuál 'body' es la bola para marcarla para eliminación
            Body bolaHoyo = null;
            if (categoriaA.categoryBits == manejoEventos.CATEGORIA_BOLA) {
                bolaHoyo = bodyA;
            } else {
                bolaHoyo = bodyB;
            }

            // Nos aseguramos de que la bola a eliminar es la del jugador actual
            if (eventos.getJugadorActual() != -1 && jugadores.get(eventos.getJugadorActual()).getBolaJugador() == bolaHoyo) {
                System.out.println("La bola del jugador actual entró al hoyo, marcándola para eliminación.");
                hashMapBodiesTemporales.put(bolaHoyo, true); // True indica que es una bola para eliminar

                // Obtenemos el jugador actual para terminar el hoyo
                Jugador jugadorTemporal = jugadores.get(eventos.getJugadorActual());
                jugadorTemporal.terminarHoyo();
                System.out.println("El jugador: " + jugadorTemporal.getNombre() + " ha terminado el hoyo");
            } else {
                 System.out.println("Una bola que no es la del jugador actual entró al hoyo, ignorando.");
            }
        }
        
        // Primero, identificamos la bola del jugador actual.
        Body bolaActual = null;
        if (eventos.getJugadorActual() != -1) { // Aseguramos que hay un jugador actual válido
            Jugador jugador = jugadores.get(eventos.getJugadorActual());
            if (jugador.getBolaJugador() == bodyA) {
                bolaActual = bodyA;
            } else if (jugador.getBolaJugador() == bodyB) {
                bolaActual = bodyB;
            }
        }

		// Si la bola del jugador actual está involucrada en el contacto con una zona de aceleración...
        if (bolaActual != null) {
            // El 'otroBody' es la zona de aceleración
            Body otroBody = (bolaActual == bodyA) ? bodyB : bodyA; 
            
            // Usamos las FilterData para identificar la categoría de la zona
            // Asegúrate de que las zonas tienen al menos una Fixture
            Filter otroCategoria = otroBody.getFixtureList().get(0).getFilterData(); 

            // Fuerza de aceleración común para todas las zonas direccionales
            float fuerzaAceleracion = 1.0f; 
            Vector2 direccionAceleracion = null; // Dirección que la zona de aceleración intentaría aplicar

            // Determinar la dirección de la zona de aceleración basada en su categoría
            if (otroCategoria.categoryBits == manejoEventos.CATEGORIA_ACELERA_ARRIBA) {
                direccionAceleracion = new Vector2(0, 1);
            } else if (otroCategoria.categoryBits == manejoEventos.CATEGORIA_ACELERA_ABAJO) {
                direccionAceleracion = new Vector2(0, -1);
            } else if (otroCategoria.categoryBits == manejoEventos.CATEGORIA_ACELERA_IZQUIERDA) {
                direccionAceleracion = new Vector2(-1, 0);
            } else if (otroCategoria.categoryBits == manejoEventos.CATEGORIA_ACELERA_DERECHA) {
                direccionAceleracion = new Vector2(1, 0);
            }

            // Si es una zona de aceleración válida
            if (direccionAceleracion != null) {
                // Obtenemos la velocidad actual de la bola
                Vector2 velocidadBola = bolaActual.getLinearVelocity();

                // Calculamos el ángulo entre la velocidad de la bola y la dirección de la zona.
                // Usamos Vector2.angleDeg() para obtener el ángulo en grados.
                // Normalizamos la velocidad de la bola para evitar problemas con NaNs si la velocidad es muy baja.
                float angle = velocidadBola.len() > 0.01f ? velocidadBola.angleDeg(direccionAceleracion) : 0; // Si no hay velocidad, el ángulo es 0 para evitar NaNs

                // Si la bola tiene suficiente velocidad Y su dirección es significativamente contraria
                if (velocidadBola.len() >= UMBRAL_VELOCIDAD_ATRAVESAR && angle > UMBRAL_ANGULO_CONTRARIO) {
                    System.out.println("Bola atraviesa zona de aceleración " + tipoZonaParaDebug(otroCategoria.categoryBits) + " (velocidad: " + velocidadBola.len() + ", ángulo: " + angle + " grados).");
                    // No hacemos nada, la bola atraviesa la zona sin ser afectada
                } else {
                    // Si no cumple la condición para atravesar, aplicamos la aceleración
                    System.out.println("¡Bola entró en zona de aceleración " + tipoZonaParaDebug(otroCategoria.categoryBits) + "!");
                    aplicarAceleracion(bolaActual, fuerzaAceleracion, direccionAceleracion);
                }
            }
        }
    }

	/* 
     * Aplica un impulso de aceleración a la bola en una dirección específica y limita su velocidad.
     * @param bola El Body de la bola.
     * @param fuerzaAceleracion La magnitud del impulso a aplicar.
     * @param direccion El vector de dirección hacia donde se aplicará la aceleración.
     */
    private void aplicarAceleracion(Body bola, float fuerzaAceleracion, Vector2 direccion) {
        // Aplica el impulso a la bola en la dirección especificada.
        bola.applyLinearImpulse(direccion.nor().scl(fuerzaAceleracion), bola.getWorldCenter(), true);
        
        // Opcional: Para evitar que la bola se vuelva loca con demasiada aceleración,
        // puedes limitar su velocidad máxima después de la aceleración.
        float velocidadMaxima = 2.0f; // Define tu velocidad máxima deseada (ej. 2 metros/segundo)
        Vector2 velocidadActual = bola.getLinearVelocity();
        if (velocidadActual.len() > velocidadMaxima) {
            bola.setLinearVelocity(velocidadActual.nor().scl(velocidadMaxima));
        }
    }
	
	/** Called when two fixtures cease to touch. */
	@Override
	public void endContact(Contact contact) {
		// Para las zonas de aceleración/frenado que aplican impulsos, no es necesario hacer nada aquí,
        // ya que el efecto es instantáneo al entrar. Si hubieras usado fuerzas continuas (applyForce),
        // aquí es donde las detendrías o cambiarías el comportamiento de la bola al salir de la zona.

        // Puedes añadir mensajes de depuración para confirmar que la bola sale de las zonas si lo deseas.
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();

        Body bolaActual = null;
        if (eventos.getJugadorActual() != -1) {
            Jugador jugador = jugadores.get(eventos.getJugadorActual());
            if (jugador.getBolaJugador() == bodyA) {
                bolaActual = bodyA;
            } else if (jugador.getBolaJugador() == bodyB) {
                bolaActual = bodyB;
            }
        }

        if (bolaActual != null) {
            Body otroBody = (bolaActual == bodyA) ? bodyB : bodyA;
            Object userData = otroBody.getUserData();

            if (userData instanceof String) {
                String tipoZona = (String) userData;
                if ("zonaAceleracion".equals(tipoZona)) {
                    System.out.println("Bola salió de zona de aceleración.");
				}
            }
        }
	}

	// DEBUGGER PARA ZONAS DE ACELERACIÓN
	// Este método es para imprimir en consola el tipo de zona de aceleración que se ha
	// detectado en el contacto.
    private String tipoZonaParaDebug(short categoryBits) {
        if (categoryBits == manejoEventos.CATEGORIA_ACELERA_ARRIBA) return "ARRIBA";
        if (categoryBits == manejoEventos.CATEGORIA_ACELERA_ABAJO) return "ABAJO";
        if (categoryBits == manejoEventos.CATEGORIA_ACELERA_IZQUIERDA) return "IZQUIERDA";
        if (categoryBits == manejoEventos.CATEGORIA_ACELERA_DERECHA) return "DERECHA";
        return "DESCONOCIDA";
    }
	/*
	* This is called after a contact is updated. This allows you to inspect a contact before it goes to the solver. If you are
	* careful, you can modify the contact manifold (e.g. disable contact). A copy of the old manifold is provided so that you can
	* detect changes. Note: this is called only for awake bodies. Note: this is called even when the number of contact points is
	* zero. Note: this is not called for sensors. Note: if you set the number of contact points to zero, you will not get an
	* EndContact callback. However, you may get a BeginContact callback the next step.
	*/
	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	/*
	* This lets you inspect a contact after the solver is finished. This is useful for inspecting impulses. Note: the contact
	* manifold does not include time of impact impulses, which can be arbitrarily large if the sub-step is small. Hence the
	* impulse is provided explicitly in a separate data structure. Note: this is only called for contacts that are touching,
	* solid, and awake.
	*/
	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}



};
