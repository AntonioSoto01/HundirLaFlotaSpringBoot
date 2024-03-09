package com.antonio.hundirlaflota.Controladores;

import com.antonio.hundirlaflota.Excepciones.AppException;
import com.antonio.hundirlaflota.Modelos.Usuario;
import com.antonio.hundirlaflota.Repositorios.UsuarioRepository;
import com.antonio.hundirlaflota.Servicios.EmailService;
import com.antonio.hundirlaflota.config.jwt.JwtTokenProvider;
import com.antonio.hundirlaflota.dto.LoginDto;
import com.antonio.hundirlaflota.dto.UserDto;
import com.antonio.hundirlaflota.dto.UsuarionConf;
import com.antonio.hundirlaflota.mappers.JuegoMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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
    private final JuegoMapper mapper;
    private final CsrfTokenRepository csrfTokenRepository;
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    @GetMapping("/user")
    public Usuario user(@AuthenticationPrincipal UserDto userDto, HttpServletResponse response, HttpServletRequest request) {
        Usuario usuario = mapper.userDtoToUsuario(userDto);
        CsrfToken csrfToken = csrfTokenRepository.generateToken(request);
        csrfTokenRepository.saveToken(csrfToken, request, response);
        return usuario;
    }

    @GetMapping(value = "/cambiarToken")
    public String cambiarToken(@AuthenticationPrincipal UserDto userDto) {
        Usuario usuario = mapper.userDtoToUsuario(userDto);
        String token = jwtTokenProvider.generateToken(usuario.getEmail(), JwtTokenProvider.getLONGEXPIRATIONTIME());
        return token;
    }

    @PostMapping(value = "login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDto login, BindingResult bindingResult, HttpServletResponse response, HttpServletRequest request) {
        Usuario usuario = mapper.loginToUsuario(login);
        ResponseEntity<?> errors = erroresValidacion(bindingResult);
        if (errors != null) return errors;

        Optional<Usuario> user = usuarioRepository.findByEmail(usuario.getEmail());
        if (user.isPresent()) {
            System.out.println(CharBuffer.wrap(usuario.getContrasena()));
            System.out.println(user.get().getContrasena());
            if (passwordEncoder.matches(CharBuffer.wrap(usuario.getContrasena()), user.get().getContrasena())) {
                if (!usuario.isConfirmado()) {
                    emailService.sendEmail(usuario);
                    throw new AppException("Usuario no confirmado", HttpStatus.UNAUTHORIZED);
                }
                UserDto userDto = mapper.usuarioToUserDto(usuario);
                SecurityContext context = SecurityContextHolder.getContext();
                context.setAuthentication(new UsernamePasswordAuthenticationToken(userDto, null, Collections.emptyList()));
                securityContextRepository.saveContext(context, request, response);
                return ResponseEntity.ok("");
            } else {
                throw new AppException("Contraseña o usuario incorrecta", HttpStatus.UNAUTHORIZED);
            }

        }
        throw new AppException("Contraseña o usuario incorrecta", HttpStatus.UNAUTHORIZED);
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
                throw new AppException("Las contraseñas no coinciden", HttpStatus.CONFLICT);
            }
            usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
            usuario.setConfirmado(false);
            usuario.setProveedor("local");
            emailService.sendEmail(usuario);
            usuarioRepository.save(usuario);

            return ResponseEntity.ok("");
        }

        throw new AppException("El usuario ya existe", HttpStatus.CONFLICT);
    }

    @GetMapping("/confirmar")
    public ResponseEntity<String> confirmar(@RequestParam("token") String token, HttpServletResponse response, HttpServletRequest request) {
        Authentication auth = jwtTokenProvider.validateToken(token);
        Usuario usuario = (Usuario) auth.getPrincipal();
        Optional<Usuario> optionalUser = usuarioRepository.findByEmail(usuario.getEmail());
        if (optionalUser.isPresent()) {
            Usuario user = optionalUser.get();
            user.setConfirmado(true);
            usuarioRepository.save(user);
            SecurityContext context = SecurityContextHolder.getContext();
            UserDto userDto = mapper.usuarioToUserDto(user);
            context.setAuthentication(new UsernamePasswordAuthenticationToken(userDto, null, Collections.emptyList()));
            securityContextRepository.saveContext(context, request, response);
            try {
                response.sendRedirect(frontendUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            throw new AppException("Usuario confirmado", HttpStatus.OK);
        }
        throw new AppException("Usuario no encontrado", HttpStatus.NOT_FOUND);
    }


}
