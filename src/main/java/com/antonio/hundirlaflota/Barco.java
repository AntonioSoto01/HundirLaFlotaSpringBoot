package com.antonio.hundirlaflota;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Entity
@NoArgsConstructor
public class Barco {
		    @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	private int ID;
	private int longitud = 2;
	private int tocado = 0;
    @OneToMany( cascade = CascadeType.ALL)
    private List<Casilla> posiciones=new ArrayList<Casilla>();

    @OneToMany( cascade = CascadeType.ALL)
    private List<Casilla> alrededor=new ArrayList<Casilla>();


	public Barco(int ID, int longitud) {
		super();
		this.ID = ID;
		this.longitud = longitud;
	}


	public void generarbarco(Jugador jugador) {

		boolean valido;

		int y = 0;
		int x = 0;

		int horizontal = 0;
		do {
			posiciones.clear();
			valido = true;
			horizontal = (int) (Math.random() * 2);

			if (horizontal < 1) {
				x = (int) (Math.random() * (jugador.getX() - this.getLongitud() + 1));
				y = (int) (Math.random() * jugador.getY());

			} else {
				x = (int) (Math.random() * (jugador.getX()));
				y = (int) (Math.random() * (jugador.getY() - this.getLongitud() + 1));
			}
			int x1 = x;
			int y1 = y;
			while (x1 <= x + this.getLongitud() - 1 && y1 <= y + this.getLongitud() - 1 && valido) {
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
		colocarbarco();
		puedebarco(x, y, horizontal, jugador);
	}

	public void colocarbarco() {
		for (Casilla casilla : posiciones) {
			casilla.setBarco(this);
		}
		
	}

	public void puedebarco(int x, int y, int horizontal, Jugador jugador) {
		int aux1 = 0;
		int aux2 = 0;

		if (horizontal < 1) {
			aux1 = x + this.getLongitud();
			aux2 = y + 1;
		} else {
			aux1 = x + 1;
			aux2 = y + this.getLongitud();
		}
		for (int i = x - 1; i <= aux1; i++) {
			for (int j = y - 1; j <= aux2; j++) {
				try {
					jugador.getCasilla(i, j).setPuedebarco(false);
					alrededor.add(jugador.getCasilla(i, j));
				} catch (Exception e) {
				}
			}
		}
	}

	public void puededisparar() {
		for (Casilla casilla : alrededor) {
			casilla.setPuededisparar(false);
		}
	}

}
