
package com.antonio.hundirlaflota.Controladores;

import com.antonio.hundirlaflota.Modelos.Usuario;
import com.antonio.hundirlaflota.Repositorios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@CrossOrigin(origins = "${frontend.url}")
@RestController
public class UsuarioController {
    @Autowired
    UsuarioRepository usuarioRepository;

    @RequestMapping("/user")
    public Usuario user(@AuthenticationPrincipal OAuth2User principal) {
        String googleId = principal.getAttribute("sub");
        String email = principal.getAttribute("email");
        String nombre = principal.getAttribute("name");


        Usuario usuario = usuarioRepository.findByGoogleId(googleId);

        if (usuario == null) {

            usuario = new Usuario();
            usuario.setGoogleId(googleId);
        }

        // Actualizar el email y nombre del usuario en la base de datos
        usuario.setEmail(email);
        usuario.setNombre(nombre);

        // Guardar o actualizar el usuario en la base de datos
        usuarioRepository.save(usuario);
        return usuario;
    }
}
