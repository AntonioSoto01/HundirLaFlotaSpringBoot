/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.antonio.hundirlaflota.Controladores;

import com.antonio.hundirlaflota.Modelos.Usuario;
import com.antonio.hundirlaflota.Repositorios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsuarioController {
    @Autowired
    UsuarioRepository usuarioRepository;

    @RequestMapping("/user")
    public Usuario user(@AuthenticationPrincipal OAuth2User principal) {
        String googleId = principal.getAttribute("sub");
        String email = principal.getAttribute("email");
        String nombre = principal.getAttribute("name");

        // Buscar al usuario por su Google ID en la base de datos
        Usuario usuario = usuarioRepository.findByGoogleId(googleId);

        if (usuario == null) {
            // Si el usuario no existe, crear un nuevo Usuario con el Google ID, email y nombre
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
