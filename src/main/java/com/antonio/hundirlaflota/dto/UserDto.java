package com.antonio.hundirlaflota.dto;

import com.antonio.hundirlaflota.Modelos.Partida;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.AuthenticatedPrincipal;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class UserDto implements AuthenticatedPrincipal, Serializable {
    private long id;
    private String nombre;
    private String email;
    private String contrasena;
    private String proveedor;
    private boolean confirmado;

    private List<Partida> partidas;

    @Override
    public String getName() {
        return email;
    }

}
