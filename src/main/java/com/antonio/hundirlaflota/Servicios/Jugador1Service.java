package com.antonio.hundirlaflota.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.antonio.hundirlaflota.Modelos.Casilla;
import com.antonio.hundirlaflota.Modelos.Jugador;
import com.antonio.hundirlaflota.Modelos.Jugador1;

import jakarta.transaction.Transactional;

@Service
public class Jugador1Service {
		@Autowired
	private BarcoService barcoService;
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
					this.actuUllTocado(jugador.getEstado(),jugador);
					jugador.setEstado(jugador.getEstado() + 1);
					valido = false;
					
				}
				if (!valido) {
					this.cambiarDireccion(jugador);
				}
			} while (!valido);
		}
		casillaDisparada = jugador.getCasilla(x, y);
		// try {
		// System.out.println(espacios() + "UltDisparo" +
		// getCasilla(getUllTocado().getX(), getUllTocado().getY()).toString());
		// } catch (java.lang.NullPointerException e) {
		// }
		return casillaDisparada;
	}
    @Transactional
    public void cambiarDireccion(Jugador1 jugador) {
        int tocado = jugador.getUllTocado().getBarco().getTocado();

        if (tocado > 1) {
            this.actuUllTocado(jugador.getEstado(),jugador);
            jugador.setEstado(jugador.getEstado() + ((jugador.getEstado() % 2 != 0) ? 1 : -1));
        } else {
            jugador.setEstado((int) (Math.floor(Math.random() * 4) + 1));
        }
    }

	public void IATocado(Casilla casillaDisparada,Jugador1 jugador) {

		if (jugador.getUllTocado() == null) {
            jugador.setEstado((int)(Math.floor(Math.random() * 4) + 1));
        }
				jugador.setUllTocado(casillaDisparada);
	}

	public void IAHundido(Jugador1 jugador) {
		barcoService.puededisparar( jugador.getUllTocado().getBarco());
		jugador.setUllTocado(null);
		jugador.setEstado(0);
	}

	public void IAgua(Jugador1 jugador) {
		if (jugador.getUllTocado() != null) {
			this.cambiarDireccion(jugador);
		}
	}
	public void actuUllTocado(int estado,Jugador1 jugador) {
		int tocado = jugador.getUllTocado().getBarco().getTocado();
		switch (estado) {
		case 1:
			jugador.setUllTocado(jugador.getCasilla(jugador.getUllTocado().getX() - tocado + 1,jugador.getUllTocado().getY()));
			break;
	case 2:
		jugador.setUllTocado(jugador.getCasilla(jugador.getUllTocado().getX() + tocado - 1,jugador. getUllTocado().getY()));
			break;
		case 3:
			jugador.setUllTocado(jugador.getCasilla(jugador.getUllTocado().getX(), jugador.getUllTocado().getY() - tocado + 1));
			break;
		case 4:
		jugador.setUllTocado(jugador.getCasilla(jugador.getUllTocado().getX(),jugador. getUllTocado().getY() + tocado - 1));
		break;
		default:
			break;
		}

	}

}
