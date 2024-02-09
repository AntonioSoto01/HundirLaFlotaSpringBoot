package com.antonio.hundirlaflota.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UsuarionConf {
    @Valid
    UsuarioDto usuario;
    @NotNull(message = "Confirmar contraseña no puede estar vacío")
    String contrasena;
}
