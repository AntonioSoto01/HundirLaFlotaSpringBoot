package com.antonio.hundirlaflota.Controladores;

import com.antonio.hundirlaflota.Modelos.Jugador;
import com.antonio.hundirlaflota.Modelos.Partida;
import com.antonio.hundirlaflota.Modelos.Usuario;
import com.antonio.hundirlaflota.Repositorios.PartidaRepository;
import com.antonio.hundirlaflota.Repositorios.UsuarioRepository;
import com.antonio.hundirlaflota.ResultadoTurno;
import com.antonio.hundirlaflota.Servicios.JuegoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/juego")

@CrossOrigin(origins = "${frontend.url}")

public class JuegoController {
    @Value("${frontend.url}")
    private String frontendUrl;
    @Autowired
    private JuegoService juegoService;
    @Autowired
    private PartidaRepository partidaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/iniciar")
    public ResponseEntity<Partida> iniciarJuego(@AuthenticationPrincipal OAuth2User principal,
                                                @RequestHeader(value = "X-Forwarded-For", required = false) String forwardedFor,
                                                HttpServletRequest request) {

        Partida partida = juegoService.iniciarJuego();

        if (principal != null) {
            String googleId = principal.getAttribute("sub");
            Usuario usuario = usuarioRepository.findByGoogleId(googleId);

            if (usuario != null) {
                usuario.getPartidas().add(partida);
                partidaRepository.save(partida);
                return ResponseEntity.ok(partida);
            }
        }

        String clientIpAddress = forwardedFor != null ? forwardedFor : request.getRemoteAddr();
        partida.setIp(clientIpAddress);
        partidaRepository.save(partida);

        System.out.println(clientIpAddress);
        return ResponseEntity.ok(partida);
    }


    @PostMapping("/realizar-turno-maquina")
    public ResponseEntity<ResultadoTurno> realizarTurno(@RequestParam int partidaId) {
        Partida partida = partidaRepository.findById(partidaId);
        Jugador jugadorActual = partida.getJugador2();
        ResultadoTurno resultadoTurno = juegoService.realizarTurno(jugadorActual, "", partida);
        if (resultadoTurno.isError()) {
            return ResponseEntity.badRequest().body(resultadoTurno);
        } else {
            return ResponseEntity.ok(resultadoTurno);
        }
    }

    @PostMapping("/realizar-turno-jugador")
    public ResponseEntity<ResultadoTurno> realizarTurno(@RequestParam String casilla, @RequestParam int partidaId) {
        Partida partida = partidaRepository.findById(partidaId);
        Jugador jugadorActual = partida.getJugador1();
        ResultadoTurno resultadoTurno = juegoService.realizarTurno(jugadorActual, casilla, partida);
        System.out.println("id siguiente jugador " + resultadoTurno.getNombreJugador());
        if (resultadoTurno.isError()) {
            return ResponseEntity.badRequest().body(resultadoTurno);
        } else {
            return ResponseEntity.ok(resultadoTurno);
        }
    }

    @GetMapping("/cargar")
    public Partida cargarPartida(@AuthenticationPrincipal OAuth2User principal,
                                 @RequestHeader(value = "X-Forwarded-For", required = false) String forwardedFor,
                                 HttpServletRequest request) {

        if (principal != null) {
            String googleId = principal.getAttribute("sub");
            Usuario usuario = usuarioRepository.findByGoogleId(googleId);

            if (usuario != null) {
                List<Partida> partidasNoTerminadas = usuario.getPartidas().stream()
                        .filter(partida -> !partida.getTerminar())
                        .collect(Collectors.toList());

                if (!partidasNoTerminadas.isEmpty()) {

                    return partidasNoTerminadas.get(0);
                } else {
                    return null;
                }
            }
        }
        String clientIpAddress = forwardedFor != null ? forwardedFor : request.getRemoteAddr();
        return partidaRepository.findByIpAndTerminar(clientIpAddress, false);
    }


}
