/**
 * El concepto principal es la simulación de un videojuego en el que se generan y se eliminan 
 * enemigos de diferentes categorías. Para ello tendremos dos tipos de hilos: los enemigos y 
 * los aliados.
 * Durante la ejecución se crearán un número determinado de enemigos de cada tipo y el mismo 
 * número de hilos aliados para eliminar a los correspondientes enemigos.
 * 
 * Autor: Javier Martín Castro
 */

package main.java.pctr2c;

public class SistemaLanzador {

	public static void main(String[] args) {
		IJuego juego = new Juego();

		for (int i = 0; i < Integer.parseInt(args[0]); i++) {

			// Creación de hilos de generar enemigos
			ActividadAliada aliados = new ActividadAliada(i, juego);
			new Thread(aliados).start();

			// Creación de hilos de eliminar enemigos
			ActividadEnemiga enemigos = new ActividadEnemiga(i, juego);
			new Thread(enemigos).start();

		}
	}

}
