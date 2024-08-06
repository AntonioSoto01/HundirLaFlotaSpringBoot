package com.antonio.hundirlaflota.Modelos;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Partida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    Jugador jugador1;
    @ManyToOne
    Jugador jugador2;
    String turno;
    String tokenPartida;
    Boolean terminar;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_partidas", joinColumns = @JoinColumn(name = "partida_id"), inverseJoinColumns = @JoinColumn(name = "usuario_id"))
    private List<Usuario> usuarios = new ArrayList<>();

    public Partida(Jugador jugador1, Jugador jugador2, String turno, boolean terminar) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.turno = turno;
        this.terminar = terminar;
    }
}
