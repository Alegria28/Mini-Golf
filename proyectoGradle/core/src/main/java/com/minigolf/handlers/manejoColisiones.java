package com.minigolf.handlers;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.minigolf.models.Jugador;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;

public class manejoColisiones implements ContactListener {

	// Atributos
	private ArrayList<Jugador> jugadores;
	private HashMap<Body, Boolean> hashMapBodiesTemporales;
	private World mundoBox2d;
	private manejoEventos eventos;

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
		// Posteriormente, obtenemos el objeto que tiene la categoría de los bodies involucrados
		Filter categoriaA = fixtureA.getFilterData();
		Filter categoriaB = fixtureB.getFilterData();

		// Verificamos si la colisión fue entre una bola y el hoyo o entre el hoyo y la bola
		if (categoriaA.categoryBits == manejoEventos.CATEGORIA_BOLA
				&& categoriaB.categoryBits == manejoEventos.CATEGORIA_HOYO
				|| categoriaA.categoryBits == manejoEventos.CATEGORIA_HOYO
						&& categoriaB.categoryBits == manejoEventos.CATEGORIA_BOLA) {

			System.out.println("Colisión bola-hoyo detectada");

			// De ser asi, entonces identificamos cual fixture es la bola para borrarla del mundo
			// para hacerlo, lo agregamos a nuestro arrayList que se va a encargar de eliminar la bola
			// correspondiente después del método .step()
			if (categoriaA.categoryBits == manejoEventos.CATEGORIA_BOLA) {
				System.out.println(("El fixture A es la bola, marcándola para eliminación"));
				hashMapBodiesTemporales.put(fixtureA.getBody(), true);
			} else {
				System.out.println(("El fixture B es la bola, marcándola para eliminación"));
				hashMapBodiesTemporales.put(fixtureB.getBody(), true);
			}

			// Obtenemos el jugador actual para terminar el hoyo para este jugador
			Jugador jugadorTemporal = jugadores.get(eventos.getJugadorActual());

			// Este jugador ha terminado el hoyo
			jugadorTemporal.terminarHoyo();

			// Simple mensaje
			System.out.println("El jugador: " + jugadorTemporal.getNombre() + " ha terminado el hoyo");
		}

	}

	/** Called when two fixtures cease to touch. */
	@Override
	public void endContact(Contact contact) {
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
