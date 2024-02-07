package com.antonio.hundirlaflota.dto;

import com.antonio.hundirlaflota.Modelos.Usuario;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UsuarionConf {
    @Valid
    Usuario usuario;
    String contrasena;
}
