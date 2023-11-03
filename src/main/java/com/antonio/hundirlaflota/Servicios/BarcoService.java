package com.antonio.hundirlaflota.Servicios;

import java.util.ArrayList;
import java.util.Scanner;

import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.antonio.hundirlaflota.Modelos.Barco;
import com.antonio.hundirlaflota.Modelos.Casilla;
import com.antonio.hundirlaflota.Modelos.Jugador;
import com.antonio.hundirlaflota.Repositorios.BarcoRepository;
import com.antonio.hundirlaflota.Repositorios.CasillaRepository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Service
public class BarcoService {

	@Autowired
	private BarcoRepository barcoRepository;

	@Autowired
	private CasillaRepository casillaRepository;

	@Transactional
	public void generarbarco(Jugador jugador, Barco barco) {
		ArrayList<Casilla> posiciones = new ArrayList<Casilla>();

		boolean valido;

		int y = 0;
		int x = 0;

		int horizontal = 0;
		do {
			posiciones.clear();
			valido = true;
			horizontal = (int) (Math.random() * 2);

			if (horizontal < 1) {
				x = (int) (Math.random() * (Jugador.getX() - barco.getLongitud() + 1));
				y = (int) (Math.random() * Jugador.getY());

			} else {
				x = (int) (Math.random() * (Jugador.getX()));
				y = (int) (Math.random() * (Jugador.getY() - barco.getLongitud() + 1));
			}
			int x1 = x;
			int y1 = y;
			while (x1 <= x + barco.getLongitud() - 1 && y1 <= y + barco.getLongitud() - 1 && valido) {
				posiciones.add(jugador.getCasilla(x1, y1));
				if (!jugador.getCasilla(x1, y1).isPuedebarco()) {
					valido = false;
				}
				if (horizontal < 1) {
					x1++;

				} else {
					y1++;
				}
			}

		} while (!valido);

		colocarBarco(barco, posiciones, jugador);
		puedebarco(x, y, horizontal, jugador, barco);
	}

	@Transactional
	public void generarBarcoManual(Jugador jugador, Barco barco) {
		ArrayList<Casilla> posiciones = new ArrayList<Casilla>();

		boolean valido;

		int y = 0;
		int x = 0;
		Scanner s = new Scanner(System.in);
		int horizontal = 0;
		do {
			posiciones.clear();
			valido = true;
			horizontal = (int) (0);

			if (horizontal < 1) {
				System.out.println("Introduce una posicion y(de la A a la J) y x(de 1 a 10)");
				String cadena = s.next();
				y = (int) ((Character.toUpperCase(cadena.charAt(0))) - 'A');
				x = Integer.parseInt((cadena.substring(1))) - 1;

			} else {
				x = (int) (Math.random() * (Jugador.getX()));
				y = (int) (Math.random() * (Jugador.getY() - barco.getLongitud() + 1));
			}
			int x1 = x;
			int y1 = y;
			while (x1 <= x + barco.getLongitud() - 1 && y1 <= y + barco.getLongitud() - 1 && valido) {
				posiciones.add(jugador.getCasilla(x1, y1));
				if (!jugador.getCasilla(x1, y1).isPuedebarco()) {
					valido = false;
				}
				if (horizontal < 1) {
					x1++;

				} else {
					y1++;
				}
			}

		} while (!valido);
		colocarBarco(barco, posiciones, jugador);
		puedebarco(x, y, horizontal, jugador, barco);
	}

	@Transactional
	public void colocarBarco(Barco barco, ArrayList<Casilla> posiciones, Jugador jugador) {
		for (Casilla casilla : posiciones) {
			casilla.setBarco(barco);
		}
		barcoRepository.save(barco);

	}
		public void puedebarco(int x, int y, int horizontal, Jugador jugador, Barco barcor) {
		int aux1 = 0;
		int aux2 = 0;

		if (horizontal < 1) {
			aux1 = x + barcor.getLongitud();
			aux2 = y + 1;
		} else {
			aux1 = x + 1;
			aux2 = y + barcor.getLongitud();
		}
		for (int i = x - 1; i <= aux1; i++) {
			for (int j = y - 1; j <= aux2; j++) {
				try {
					if (j >= 0) {

						jugador.getCasilla(i, j).setPuedebarco(false);


							barcor.getAlrededor().add(jugador.getCasilla(i, j));
						}
					}
				 catch (Exception e) {
				}
			
		}}
		//jugador.ver(true);
	}
	
	
	@Transactional
	public void puededisparar(Barco barco) {
		for (Casilla casilla : barco.getAlrededor()) {
			casilla.setPuededisparar(false);
		}
	}

}