package main.java.pctr2c;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Juego implements IJuego {

	private int contadorEnemigosTotales;
	private Hashtable<Integer, Integer> contadoresEnemigosTipos;
	private Hashtable<Integer, Integer> contadoresEliminadosTipos;
	private final int MINENEMIGOS = 0;
	private final int MAXENEMIGOS = 10;

	public Juego() {
		contadorEnemigosTotales = 0;
		contadoresEnemigosTipos = new Hashtable<Integer, Integer>();
		contadoresEliminadosTipos = new Hashtable<Integer, Integer>();
	}

	@Override
	public synchronized void generarEnemigo(int numero) {
		comprobarAntesDeGenerar();

		// Si no existe valor en el contador lo inicializamos
		inicializarContadores(numero, contadoresEnemigosTipos);

		incrementar(numero);

		// Imprimimos el estado del enemigo
		imprimirInfo(numero, "Generado");
	}

	public synchronized void incrementar(int numero) {
		contadorEnemigosTotales++;

		// No podr? generarse un enemigo de un tipo 2 hasta que se hayan
		// comenzado a generar los enemigos de tipo 1.
		if (numero == 0) {
			contadoresEnemigosTipos.put(numero, contadoresEnemigosTipos.get(numero) + 1);
		} else if (numero > 0 && contadoresEnemigosTipos.containsKey(numero - 1)
				&& contadoresEnemigosTipos.get(numero - 1) > 0) {
			contadoresEnemigosTipos.put(numero, contadoresEnemigosTipos.get(numero) + 1);
		}
	}

	@Override
	public synchronized void eliminarEnemigo(int numero) {
		comprobarAntesDeEliminar();

		// Si no existe valor en el contador lo inicializamos
		inicializarContadores(numero, contadoresEliminadosTipos);

		decrementar(numero);
		// Imprimimos el estado del enemigo
		imprimirInfo(numero, "Eliminado");
	}

	public synchronized void decrementar(int numero) {
		contadorEnemigosTotales--;
		contadoresEliminadosTipos.put(numero, contadoresEliminadosTipos.get(numero) + 1);
	}

	private void inicializarContadores(int numero, Hashtable<Integer, Integer> contadores) {
		// inicializamos
		if (contadores.get(numero) == null) {
			contadores.put(numero, MINENEMIGOS);
		}
	}

	private void imprimirInfo(int numero, String movimiento) {
		System.out.println(movimiento + " tipo " + numero);
		System.out.println("--> Enemigos totales " + sumarContadores());

		Integer contIN, contOUT;

		// Iteramos todos los enemigos e imprimimos por pantalla
		Integer[] enemigos = (Integer[]) contadoresEnemigosTipos.keySet().toArray(new Integer[0]);
		Arrays.sort(enemigos);

		for (int num = enemigos.length - 1; num >= 0; num--) {

			contIN = contadoresEnemigosTipos.get(num);
			if (contIN == null) {
				contIN = 0;
			}

			contOUT = contadoresEliminadosTipos.get(num);
			if (contOUT == null) {
				contOUT = 0;
			}

			System.out
					.println("-----> Enemigos tipo " + num + ": " + contIN + " ------- [Eliminados: " + contOUT + "]");
		}
		System.out.println(" ");
	}

	public int sumarContadores() {
		int sumaContadoresEnemigos = 0;

		// contamos el n?mero de enemigos que hay
		Enumeration<Integer> iterPuertas = contadoresEnemigosTipos.elements();
		while (iterPuertas.hasMoreElements()) {
			sumaContadoresEnemigos += iterPuertas.nextElement();
		}
		return sumaContadoresEnemigos;
	}

	// Los contadores de enemigos vivos deben sumar lo mismo que el contador total
	// de enemigos
	protected void checkInvariante() {
		int sumarContadores = sumarContadores();
		assert sumarContadores == contadorEnemigosTotales : "INV: Los contadores de enemigos vivos deben sumar lo mismo que el contador total de enemigos";
	}

	protected synchronized void comprobarAntesDeGenerar() {
		checkInvariante();
		notifyAll();
		// Nunca podr? haber m?s de M enemigos
		while (contadorEnemigosTotales > MAXENEMIGOS) {
			try {
				wait();
			} catch (InterruptedException e) {
				Logger.getGlobal().log(Level.WARNING,
						"Operaci?n interrumpida, seguimos esperando. Enemigos: " + contadorEnemigosTotales);
			}
		}

	}

	protected synchronized void comprobarAntesDeEliminar() {
		checkInvariante();

		// Nunca podr? haber menos de 0 enemigos
		while (contadorEnemigosTotales < MINENEMIGOS) {
			try {
				wait();
			} catch (InterruptedException e) {
				Logger.getGlobal().log(Level.WARNING,
						"Operaci?n interrumpida, seguimos esperando. Enemigos: " + contadorEnemigosTotales);
			}
		}
	}

}
