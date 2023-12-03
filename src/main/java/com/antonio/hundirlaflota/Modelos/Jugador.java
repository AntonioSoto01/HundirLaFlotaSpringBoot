package com.antonio.hundirlaflota.Modelos;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.OneToMany;
import jakarta.transaction.Transactional;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.DiscriminatorType;
import lombok.Data;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Jugador {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private static final String FINAL = "Final";
	private static final String TOCADO = "Tocado";
		private static final String HUNDIDO = "hundido";
	private static final int x = 10;
	private static final int y = 10;
	private static final int[] longBarco = { 1, 1, 1, 1, 2, 2, 2, 3, 3, 4 };
	private static final int nbarcos = longBarco.length;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Casilla> tablero = new ArrayList<Casilla>();


	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Barco> barcos = new ArrayList<Barco>();
	private int barcoshundidos = 0;
	private String nombre;
	private boolean ver;

	public Jugador() {
		this.nombre = "jugador";
	}

	public static String getHundido() {
		return HUNDIDO;
	}

	public static String getTocado() {
        return TOCADO;
    }

    public static String getFinal() {
		return FINAL;
	}

	public int getNbarcos() {
		return nbarcos;
	}

	public boolean getVer() {
		return ver;
	}

	public static int getX() {
		return x;
	}

	public static int getY() {
		return y;
	}

	public int[] getLongBarco() {
		return longBarco;
	}

	public Casilla getCasilla(int x, int y) {
		return tablero.get(x * this.x + y);
	}



}
