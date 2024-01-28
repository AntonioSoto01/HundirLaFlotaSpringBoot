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
    public String nombre;
    public String email;
    public String contraseña;
    public String proveedor;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_partidas", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "partida_id"))
    private List<Partida> partidas = new ArrayList<>();

}
