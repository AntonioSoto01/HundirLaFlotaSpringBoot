package com.antonio.hundirlaflota.Modelos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Barco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int posicion;
    private int longitud = 2;
    private int tocado = 0;
    private boolean hundido;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "alrededor_barco", joinColumns = @JoinColumn(name = "barco_id"), inverseJoinColumns = @JoinColumn(name = "casilla_id"))
    private List<Casilla> alrededor = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "posiciones_barco", joinColumns = @JoinColumn(name = "barco_id"), inverseJoinColumns = @JoinColumn(name = "casilla_id"))

    private List<Casilla> posiciones = new ArrayList<Casilla>();

    public Barco(int ID, int longitud) {
        super();
        this.posicion = ID;
        this.longitud = longitud;
    }
}
