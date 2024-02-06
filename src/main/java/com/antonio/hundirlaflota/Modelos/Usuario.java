package com.antonio.hundirlaflota.Modelos;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nombre;
    private String email;
    private String contrasena;
    private String proveedor;
    private boolean confirmado = true;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_partidas", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "partida_id"))
    private List<Partida> partidas = new ArrayList<>();

}
