package com.antonio.hundirlaflota.Servicios;

import org.springframework.stereotype.Service;

import com.antonio.hundirlaflota.Modelos.Casilla;
import com.antonio.hundirlaflota.Modelos.Jugador;
import com.antonio.hundirlaflota.Modelos.Jugador1;

import jakarta.transaction.Transactional;

@Service
public class Jugador1Service {
	@Transactional
	public Casilla casillaDisparada(Jugador1 jugador) {

		int x = 0;
		int y = 0;
		Casilla casillaDisparada = null;
		if (jugador.getUllTocado() == null) {

			do {
				x = (int) (Math.random() * (jugador.getX()));
				y = (int) (Math.random() * jugador.getY());
			} while (jugador.getCasilla(x, y).isDisparado() || !jugador.getCasilla(x, y).isPuededisparar());

		} else {

			boolean valido = true;

			do {
				valido = true;
				x = jugador.getUllTocado().getX();
				y = jugador.getUllTocado().getY();

				switch (jugador.getEstado()) {
					case 1:
						x++;
						break;
					case 2:
						x--;
						break;
					case 3:
						y++;
						break;
					case 4:
						y--;
						break;
				}
				if (y >= jugador.getY() || y < 0 || x < 0 || x >= jugador.getX()
						|| jugador.getCasilla(x, y).isDisparado() || !jugador.getCasilla(x, y).isPuededisparar()) {
					jugador.actuUllTocado(jugador.getEstado());
					jugador.setEstado(jugador.getEstado() + 1);
					valido = false;
				}
			} while (!valido);
		}
		casillaDisparada = jugador.getCasilla(x, y);
		// try {
		// System.out.println(espacios() + "UltDisparo" +
		// getCasilla(getUllTocado().getX(), getUllTocado().getY()).toString());
		// } catch (java.lang.NullPointerException e) {
		// }
		System.out.println(jugador.espacios() + "Dispara a " + jugador.getCasilla(x, y).toString());
		return casillaDisparada;
	}

}
