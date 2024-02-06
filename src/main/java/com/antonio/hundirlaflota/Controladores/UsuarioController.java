
package com.antonio.hundirlaflota.Controladores;

import com.antonio.hundirlaflota.Modelos.Usuario;
import com.antonio.hundirlaflota.Repositorios.UsuarioRepository;
import com.antonio.hundirlaflota.Servicios.EmailService;
import com.antonio.hundirlaflota.config.jwt.JwtTokenProvider;
import com.antonio.hundirlaflota.dto.UsuarionConf;

import io.netty.handler.codec.http.HttpResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.Optional;

@CrossOrigin(origins = "${frontend.url}")
@RestController
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    @Value("${frontend.url}")
    private String frontendUrl;

    @RequestMapping("/user")
    public Usuario user(@AuthenticationPrincipal Usuario usuario) {
        return usuario;
    }

    @GetMapping(value = "/cambiarToken")
    public String cambiarToken(@AuthenticationPrincipal Usuario usuario) {

        String token = jwtTokenProvider.generateToken(usuario.getEmail(), JwtTokenProvider.getLONGEXPIRATIONTIME());
        return token;
    }

    @GetMapping(value = "login")
    public ResponseEntity<String> login(Usuario usuario) {

        Optional<Usuario> user = usuarioRepository.findByEmail(usuario.getEmail());
        if (user.isPresent()) {
            if (passwordEncoder.matches(CharBuffer.wrap(usuario.getContrasena()), user.get().getContrasena())) {
                String token = jwtTokenProvider.generateToken(usuario.getEmail(),
                        JwtTokenProvider.getLONGEXPIRATIONTIME());

                return ResponseEntity.ok(token);
            }
        }
        return null;
    }

    @PostMapping(value = "/registro")
    public ResponseEntity<String> registro(@RequestBody UsuarionConf usuarionConf) {
        System.out.println(usuarionConf);
        // Extraer el usuario y la contraseña de la solicitud
        Usuario usuario = usuarionConf.getUsuario();
        String contrasena = usuarionConf.getContrasena();
        Optional<Usuario> user = usuarioRepository.findByEmail(usuario.getEmail());
        if (user.isEmpty()) {

            usuario.setContrasena(contrasena); 
            usuario.setConfirmado(false);
            emailService.sendEmail(
                    jwtTokenProvider.generateToken(usuario.getEmail(), JwtTokenProvider.getMIDDLEEXPIRATIONTIME()),
                    usuario);
            usuarioRepository.save(usuario);


            return ResponseEntity
                    .ok("");
        }


        return ResponseEntity.status(HttpStatus.CONFLICT).body("El usuario ya existe");
    }

    @GetMapping("/confirmar")
    public ResponseEntity<String> confirmar(@RequestParam("token") String token, HttpServletResponse response) {
        Authentication auth = jwtTokenProvider.validateToken(token);
        Usuario usuario = (Usuario) auth.getPrincipal();
        Optional<Usuario> optionalUser = usuarioRepository.findByEmail(usuario.getEmail());
        if (optionalUser.isPresent()) {
            Usuario user = optionalUser.get();
            user.setConfirmado(true);
            usuarioRepository.save(user);
            String jwtToken = jwtTokenProvider.generateToken(usuario.getEmail(), JwtTokenProvider.getLONGEXPIRATIONTIME());
            try {
                response.sendRedirect(frontendUrl + "/token/?token=" + jwtToken);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ResponseEntity.ok("");
        }
        return ResponseEntity.notFound().build();
    }

}
