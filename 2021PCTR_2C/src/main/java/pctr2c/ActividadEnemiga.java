package main.java.pctr2c;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ActividadEnemiga implements Runnable {

	private final int NUMENEMIGOS = 20;
	private int numero;
	private IJuego juego;

	public ActividadEnemiga(int numero, IJuego juego) {
		this.numero = numero;
		this.juego = juego;
	}

	@Override
	public void run() {
		for (int i = 0; i < NUMENEMIGOS; i++) {
			try {
				Random rand = new Random();
				// Un enemigo simular� su actividad durmiendo un tiempo aleatorio entre 1 y 5
				// segundos
				int randnum = rand.nextInt((5 - 1) + 1) + 1;
				TimeUnit.MILLISECONDS.sleep(randnum * 1000);
				juego.eliminarEnemigo(numero);
			} catch (InterruptedException e) {
				Logger.getGlobal().log(Level.INFO, "Salida interrumpida");
				Logger.getGlobal().log(Level.INFO, e.toString());
				return;
			}
		}
	}

}
