package Principal;

import java.util.ArrayList;

public class Barco {
	private int ID;
	private int longitud = 2;
	private int tocado = 0;
	private ArrayList<Casilla> posiciones = new ArrayList<Casilla>();

	public Barco(int ID, int longitud) {
		super();
		this.ID = ID;
		this.longitud = longitud;
	}

	public int getTocado() {
		return tocado;
	}

	public void setTocado(int tocado) {
		this.tocado = tocado;
	}

	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public int getLongitud() {
		return longitud;
	}

	public void setLongitud(int longitud) {
		this.longitud = longitud;
	}

	public Casilla getPosicion(int x) {
		return posiciones.get(x);
	}

	public void colocarbarco(Jugador jugador) {

		boolean valido;

		int y = 0;
		int x = 0;
		int aux1 = 0;
		int aux2 = 0;
		int horizontal = 0;
		do {
			valido = true;
			horizontal = (int) (Math.random() * 2);

			if (horizontal < 1) {
				x = (int) (Math.random() * (jugador.getX() - this.getLongitud() + 1));
				y = (int) (Math.random() * jugador.getY());
				aux1 = x;
				aux2 = y;
			} else {
				x = (int) (Math.random() * (jugador.getX()));
				y = (int) (Math.random() * (jugador.getY() - this.getLongitud() + 1));
				aux1 = y;
				aux2 = x;
			}

			int i = aux1 - 1;
			int j = aux2 - 1;

			while (i < aux1 + this.getLongitud() + 1 && valido) {
				j = aux2 - 1;
				while (j <= aux2 + 1 && valido) {
					try {

						if (((horizontal < 1) && (jugador.getCasilla(i, j).getBarco() != null)) || ((horizontal >= 1) && !(jugador.getCasilla(j, i).getBarco() != null))) {
							valido = false;
						}

					} catch (java.lang.ArrayIndexOutOfBoundsException e) {
					}
					// System.out .println(jugador.espacios()+horizontal + " " + valido + " x " + x + " y " + y+ " i" + i + " j" + j);
					j++;
				}
				i++;
			}
			// System.out.println(jugador.espacios()+valido);
		} while (!valido);
		for (int j = aux1; j < aux1 + this.getLongitud(); j++) {
			if (horizontal < 1) {
				posiciones.add(jugador.getCasilla(j, aux2));

			} else {
				posiciones.add(jugador.getCasilla(aux2, j));
			}
			// System.out.println(jugador.espacios()+((char)(posiciones.get(posiciones.size()-1).getY()+'A'))+""+(posiciones.get(posiciones.size()-1).getX()+1));
			posiciones.get(posiciones.size() - 1).setBarco(this);

		}

	}
}
