package com.antonio.hundirlaflota;

import lombok.Data;

@Data
public class Casilla {
	private boolean disparado;
	private Barco barco;
	private int x;
	private int y;
	private boolean puedebarco;
	private boolean puededisparar;

	@Override
	public String toString() {
		return ((char) ('A' + y) + "" + (x + 1));
	}
	public Casilla(int x, int y) {
		super();
		this.x = x;
		this.y = y;
		this.disparado = false;
		this.puedebarco = true;
		this.puededisparar = true;
		this.barco = null;
	}

}
