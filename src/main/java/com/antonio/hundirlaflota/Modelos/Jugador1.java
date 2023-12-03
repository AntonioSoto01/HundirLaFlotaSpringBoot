package com.antonio.hundirlaflota.Modelos;


import java.util.Scanner;
import java.util.concurrent.Semaphore;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Data;
@Data
@Entity
public class Jugador1 extends Jugador {
	public Jugador1() {
		super();
		this.setNombre("maquina");
		this.setVer(true);
	}
@OneToOne
	private Casilla ultTocado = null;
	private int estado = 0;

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
	

	
}
