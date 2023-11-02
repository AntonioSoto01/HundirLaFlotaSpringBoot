package com.antonio.hundirlaflota;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;
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


    @ManyToMany
    @JoinTable(
        name = "alrededor_barco",
        joinColumns = @JoinColumn(name = "barco_id"),
        inverseJoinColumns = @JoinColumn(name = "casilla_id")
    )
    private List<Casilla> alrededor = new ArrayList<>();


	public Barco(int ID, int longitud) {
		super();
		this.posicion = ID;
		this.longitud = longitud;
	}





}
