package com.antonio.hundirlaflota;

import java.util.Scanner;
import java.util.concurrent.Semaphore;

import jakarta.persistence.Entity;
import lombok.Data;
@Data
public class Jugador1 extends Jugador {
	public Jugador1() {
		super();
		this.setNombre("maquina");
		this.setVer(true);
	}

	private Casilla ultTocado = null;
	private int estado = 1;

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public Casilla getUllTocado() {
		return ultTocado;
	}

	public void setUllTocado(Casilla ultidisparo) {
		this.ultTocado = ultidisparo;
	}
	public Casilla casillaDisparada() {

		int x = 0;
		int y = 0;
		Casilla casillaDisparada = null;
		if (getUllTocado() == null) {

			do {
				x = (int) (Math.random() * (this.getX()));
				y = (int) (Math.random() * this.getY());
			} while (this.getCasilla(x, y).isDisparado() || ! this.getCasilla(x, y).isPuededisparar());

		} else {

			boolean valido = true;

			do {
				valido = true;
				x = getUllTocado().getX();
				y = getUllTocado().getY();

				switch (getEstado()) {
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
				if (y >= this.getY() || y < 0 || x < 0 || x >= this.getX() || this.getCasilla(x, y).isDisparado() || ! this.getCasilla(x, y).isPuededisparar()) {
					actuUllTocado(getEstado());
					setEstado(getEstado() + 1);
					valido = false;
				}
			} while (!valido);
		}
		casillaDisparada = getCasilla(x, y);
//		try {
//			System.out.println(espacios() + "UltDisparo" + getCasilla(getUllTocado().getX(), getUllTocado().getY()).toString());
//		} catch (java.lang.NullPointerException e) {
//		}
		System.out.println(espacios() + "Dispara a " + getCasilla(x, y).toString());
		return casillaDisparada;
	}

	public void actuUllTocado(int estado) {
		int tocado = getUllTocado().getBarco().getTocado();
		switch (estado) {
		case 1:
			setUllTocado(getCasilla(getUllTocado().getX() - tocado + 1, getUllTocado().getY()));
			break;
//		case 2:
//			setUllTocado(getCasilla(getUllTocado().getX() + tocado - 1, getUllTocado().getY()));
//			break;
		case 3:
			setUllTocado(getCasilla(getUllTocado().getX(), getUllTocado().getY() - tocado + 1));
			break;
//		case 4:
//			setUllTocado(getCasilla(getUllTocado().getX(), getUllTocado().getY() + tocado - 1));
//			break;
		default:
			break;
		}

	}

	public void IATocado(Casilla casillaDisparada) {
		setUllTocado(casillaDisparada);
	}

	public void IAHundido() {
		getUllTocado().getBarco().puededisparar();
		setUllTocado(null);

		setEstado(1);
	}

	public void IAgua() {
		if (getUllTocado() != null) {
			actuUllTocado(getEstado());
			setEstado(getEstado() + 1);
		}
	}

	public String espacios() {
		return String.format("%-90s", "");
	};

	public void enter() {
		Scanner s = new Scanner(System.in);
		System.out.println();
		System.out.println(this.espacios() + "Pulsa enter para continuar");
		s.nextLine();
	}
}
