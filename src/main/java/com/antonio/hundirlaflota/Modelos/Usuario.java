package com.antonio.hundirlaflota.Modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@NotEmpty
@NotNull
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotEmpty (message = "El nombre no puede estar vacío")
    private String nombre;
    @NotEmpty (message = "El nombre no puede estar vacío")
    @Email(message = "Formato de email incorrecto")
    private String email;
    @NotEmpty (message = "La contraseña no puede estar vacía")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String contrasena;
    private String proveedor;
    private boolean confirmado = true;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_partidas", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "partida_id"))
    private List<Partida> partidas = new ArrayList<>();

}
