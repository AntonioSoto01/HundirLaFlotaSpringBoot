package com.antonio.hundirlaflota.Modelos;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

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
    @Getter
    private static final int x = 10;
    @Getter
    private static final int y = 10;
    private static final int[] longBarco = {1, 1, 1, 1, 2, 2, 2, 3, 3, 4};
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

    public int[] getLongBarco() {
        return longBarco;
    }

    public Casilla getCasilla(int x, int y) {
        return tablero.get(x * Jugador.getX() + y);
    }


}
