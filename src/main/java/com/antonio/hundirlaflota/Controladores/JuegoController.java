package com.antonio.hundirlaflota.Controladores;

import com.antonio.hundirlaflota.Modelos.Jugador;
import com.antonio.hundirlaflota.Modelos.Partida;
import com.antonio.hundirlaflota.Modelos.Usuario;
import com.antonio.hundirlaflota.Repositorios.PartidaRepository;
import com.antonio.hundirlaflota.Repositorios.UsuarioRepository;
import com.antonio.hundirlaflota.Servicios.JuegoService;
import com.antonio.hundirlaflota.config.jwt.JwtTokenProvider;
import com.antonio.hundirlaflota.dto.ResultadoTurno;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/juego")

@CrossOrigin(origins = "${frontend.url}")
@RequiredArgsConstructor
public class JuegoController {
    @Value("${frontend.url}")
    private String frontendUrl;

    private final JuegoService juegoService;

    private final PartidaRepository partidaRepository;

    private final UsuarioRepository usuarioRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/iniciar")
    public ResponseEntity<Partida> iniciarJuego(@AuthenticationPrincipal Usuario usuario,
                                                HttpServletRequest request) {

        Partida partida = juegoService.iniciarJuego();
        if (usuario != null) {
            usuario.getPartidas().add(partida);
            partidaRepository.save(partida);
            usuarioRepository.save(usuario);
            System.out.println(usuario);
            return ResponseEntity.ok(partida);
        }
        String token = jwtTokenProvider.generateToken(String.valueOf(partida.getId()), JwtTokenProvider.getSHORTEXPIRATIONTIME());
        partida.setTokenPartida(token);
        partidaRepository.save(partida);

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

    @PostMapping("/cargar")
    public Partida cargarPartida(@AuthenticationPrincipal Usuario usuario, @RequestBody Map<String, Object> requestBody) {
        String token = (String) requestBody.get("token");

        if (usuario != null) {
            List<Partida> partidasNoTerminadas = usuario.getPartidas().stream()
                    .filter(partida -> !partida.getTerminar())
                    .toList();

            if (!partidasNoTerminadas.isEmpty()) {
                return partidasNoTerminadas.get(0);
            } else {
                return null;
            }
        }

        return partidaRepository.findByTokenPartida(token);
    }


}
