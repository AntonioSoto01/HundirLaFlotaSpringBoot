
package com.antonio.hundirlaflota.Controladores;

import com.antonio.hundirlaflota.Modelos.Usuario;
import com.antonio.hundirlaflota.Repositorios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "${frontend.url}")
@RestController
public class UsuarioController {
    @Autowired
    UsuarioRepository usuarioRepository;

    @RequestMapping("/user")
    public Usuario user(@AuthenticationPrincipal Usuario usuario) {

        return usuario;
    }
}
