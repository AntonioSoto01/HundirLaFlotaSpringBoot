package com.antonio.hundirlaflota.Modelos;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Partida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    Jugador jugador1;
    @ManyToOne
    Jugador jugador2;
    String turno;
    String ip;
    Boolean terminar;
    public Partida    (    Jugador jugador1,Jugador jugador2,String turno,boolean terminar   ){
        this.jugador1=jugador1;
        this.jugador2=jugador2;
        this.turno=turno;
        this.terminar=terminar;
    }
        public Partida    ( ){

    }
}
