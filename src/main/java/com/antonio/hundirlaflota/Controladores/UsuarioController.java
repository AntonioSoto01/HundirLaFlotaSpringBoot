package com.antonio.hundirlaflota.Controladores;

import com.antonio.hundirlaflota.Excepciones.AppException;
import com.antonio.hundirlaflota.Modelos.Usuario;
import com.antonio.hundirlaflota.Repositorios.UsuarioRepository;
import com.antonio.hundirlaflota.Servicios.EmailService;
import com.antonio.hundirlaflota.Servicios.UsuarioService;
import com.antonio.hundirlaflota.config.jwt.JwtTokenProvider;
import com.antonio.hundirlaflota.dto.EmailData;
import com.antonio.hundirlaflota.dto.LoginDto;
import com.antonio.hundirlaflota.dto.UserDto;
import com.antonio.hundirlaflota.dto.UsuarionConf;
import com.antonio.hundirlaflota.mappers.JuegoMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioService usuarioService;
    @Value("${frontend.url}")
    private String frontendUrl;
    private final JuegoMapper mapper;
    //private final CsrfTokenRepository csrfTokenRepository;

    @GetMapping("/user")
    public Usuario user(@AuthenticationPrincipal UserDto userDto, HttpServletResponse response, HttpServletRequest request) {
        if (userDto == null) {
            return null;
        }
        return usuarioRepository.findById(userDto.getId()).orElse(null);

    }

    @PostMapping(value = "login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDto login, BindingResult bindingResult, HttpServletResponse response, HttpServletRequest request) {
        Usuario usuario = mapper.loginToUsuario(login);
        ResponseEntity<?> errors = erroresValidacion(bindingResult);
        if (errors != null) return errors;

        Usuario user = usuarioRepository.findByEmail(usuario.getEmail()).orElseThrow(() -> new AppException("Contraseña o usuario incorrecta", HttpStatus.UNAUTHORIZED));

        if (passwordEncoder.matches(usuario.getContrasena(), user.getContrasena())) {
            if (!usuario.isConfirmado()) {
                //emailService.sendEmail(usuario.getEmail());
                throw new AppException("Usuario no confirmado", HttpStatus.UNAUTHORIZED);
            }
            usuarioService.saveContext(user, request, response);
            return ResponseEntity.ok("");
        } else {
            throw new AppException("Contraseña o usuario incorrecta", HttpStatus.UNAUTHORIZED);
        }

    }


    private ResponseEntity<?> erroresValidacion(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }
        return null;
    }

    @PostMapping(value = "/registro")
    public ResponseEntity<?> registro(@RequestBody @Valid UsuarionConf usuarionConf, BindingResult bindingResult) {
        ResponseEntity<?> errors = erroresValidacion(bindingResult);
        if (errors != null) return errors;

        Usuario usuario = mapper.usarioDtoToUsuario(usuarionConf.getUsuario());
        String contrasena = usuarionConf.getContrasena();
        Optional<Usuario> user = usuarioRepository.findByEmail(usuario.getEmail());
        if (user.isEmpty()) {
            if (!contrasena.equals(usuario.getContrasena())) {
                // throw new AppException("Las contraseñas no coinciden", HttpStatus.CONFLICT);
                return ResponseEntity.badRequest().body("Las contraseñas no coinciden");
            }
            usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
            usuario.setConfirmado(false);
            usuario.setProveedor("local");
            String token = jwtTokenProvider.generateToken(usuario.getEmail(), JwtTokenProvider.getMIDDLEEXPIRATIONTIME());
            EmailData emailData = new EmailData("Email Confirmation", " to confirm your email", usuario.getEmail(), token, "/confirmar");
            emailService.sendEmail(emailData);
            usuarioRepository.save(usuario);

            return ResponseEntity.ok("");
        }

        throw new AppException("El usuario ya existe", HttpStatus.CONFLICT);
    }

    @SneakyThrows
    @GetMapping("/confirmar")
    public ResponseEntity<String> confirmar(@RequestParam("token") String token, HttpServletResponse response, HttpServletRequest request) {
        Authentication auth = jwtTokenProvider.validateToken(token);
        Usuario principal = (Usuario) auth.getPrincipal();
        Usuario user = usuarioRepository.findByEmail(principal.getEmail()).orElseThrow(() -> new AppException("Usuario no encontrado", HttpStatus.NOT_FOUND));
        user.setConfirmado(true);
        usuarioRepository.save(user);
        usuarioService.saveContext(user, request, response);
        response.sendRedirect(frontendUrl);
        return ResponseEntity.ok("Usuario confirmado");
    }


}
