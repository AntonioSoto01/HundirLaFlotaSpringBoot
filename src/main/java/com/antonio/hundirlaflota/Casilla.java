package com.antonio.hundirlaflota;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@Data
@Entity
public class Casilla {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private boolean disparado;
	@ManyToOne
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
