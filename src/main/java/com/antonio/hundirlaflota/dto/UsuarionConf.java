package com.antonio.hundirlaflota.dto;

import com.antonio.hundirlaflota.Modelos.Usuario;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UsuarionConf {
    Usuario usuario;
    String contrasena;
}
