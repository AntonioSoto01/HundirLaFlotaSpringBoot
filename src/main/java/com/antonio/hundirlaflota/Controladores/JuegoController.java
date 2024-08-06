package com.antonio.hundirlaflota.Controladores;

import com.antonio.hundirlaflota.Excepciones.AppException;
import com.antonio.hundirlaflota.Modelos.Jugador;
import com.antonio.hundirlaflota.Modelos.Partida;
import com.antonio.hundirlaflota.Modelos.Usuario;
import com.antonio.hundirlaflota.Repositorios.PartidaRepository;
import com.antonio.hundirlaflota.Repositorios.UsuarioRepository;
import com.antonio.hundirlaflota.Servicios.JuegoService;
import com.antonio.hundirlaflota.config.jwt.JwtTokenProvider;
import com.antonio.hundirlaflota.dto.ResultadoTurno;
import com.antonio.hundirlaflota.dto.TokenJugador;
import com.antonio.hundirlaflota.dto.UserDto;
import com.antonio.hundirlaflota.mappers.JuegoMapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController


@RequiredArgsConstructor
public class JuegoController {


    private static final Logger log = LoggerFactory.getLogger(JuegoController.class);
    private final JuegoService juegoService;

    private final PartidaRepository partidaRepository;

    private final UsuarioRepository usuarioRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JuegoMapper juegoMapper;
    private final SimpMessagingTemplate template;

    @GetMapping("/iniciar")
    public ResponseEntity<Partida> iniciarJuego(@AuthenticationPrincipal UserDto userDto) {
        Usuario usuario = (userDto != null && userDto.getId() != null) ? usuarioRepository.findById(userDto.getId()).orElse(null) : null;
        Partida partida = juegoService.iniciarJuego();

        if (usuario != null) {
            partida.getJugador1().setNombre(usuario.getNombre());
            partida.setTurno(partida.getJugador1().getRol());
            partida.getUsuarios().add(usuario);
            partidaRepository.save(partida);
            usuarioRepository.save(usuario);
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
    public ResponseEntity<ResultadoTurno> realizarTurno(@RequestParam String casilla, @RequestParam int partidaId, @AuthenticationPrincipal UserDto userDto) {
        Partida partida = partidaRepository.findById(partidaId);
        String rol = partida.getUsuarios().size() == 2 && partida.getJugador2().getUsuario().getId() == (userDto.getId()) ? "jugador2" : "jugador1";
        Jugador jugadorActual = rol.equals(partida.getJugador1().getRol()) ? partida.getJugador1() : partida.getJugador2();
        Jugador jugadorContrario = rol.equals(partida.getJugador1().getRol()) ? partida.getJugador2() : partida.getJugador1();
        if (userDto != null && jugadorActual.getUsuario().getId() != userDto.getId()) {
            throw new AppException("No puedes jugar en el turno de otro jugador", HttpStatus.BAD_REQUEST);
        }
        ResultadoTurno resultadoTurno = juegoService.realizarTurno(jugadorActual, casilla, partida);
        if (userDto != null) {
            this.template.convertAndSendToUser(jugadorContrario.getUsuario().getEmail(), "/topic/game." + partidaId, "holi");
        }
        if (resultadoTurno.isError()) {
            return ResponseEntity.badRequest().body(resultadoTurno);
        } else {
            return ResponseEntity.ok(resultadoTurno);
        }
    }

    @PostMapping("/cargar")
    public TokenJugador cargarPartida(@AuthenticationPrincipal UserDto userDto, @RequestBody(required = false) String token) {
        Usuario usuario = (userDto != null && userDto.getId() != null) ? usuarioRepository.findById(userDto.getId()).orElse(null) : null;
        if (usuario != null) {
            List<Partida> partidas = partidaRepository.findByUsuarios(usuario);
            List<Partida> partidasNoTerminadas = partidas.stream()
                    .filter(partida -> !partida.getTerminar())
                    .toList();
            if (!partidasNoTerminadas.isEmpty()) {
                String rol = partidasNoTerminadas.get(0).getUsuarios().size() == 2 && partidasNoTerminadas.get(0).getJugador2().getUsuario().getId() == (usuario.getId()) ? "jugador2" : "jugador1";
                return new TokenJugador(partidasNoTerminadas.get(0), rol);

            } else {
                return null;
            }
        }

        return new TokenJugador(partidaRepository.findByTokenPartida(token).orElse(null), "jugador1");
    }

    @PostMapping("/iniciar-juego-2-jugadores")
    public ResponseEntity<Partida> iniciarJuego2Jugadores(@AuthenticationPrincipal UserDto userDto, @RequestBody @Email String email) {
        Usuario usuario = (userDto != null && userDto.getId() != null) ? usuarioRepository.findById(userDto.getId()).orElse(null) : null;
        if (usuario != null) {
            Partida partida = juegoService.iniciarJuego2Jugadores(email, usuario);
            return ResponseEntity.ok(partida);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/email-recibido")
    public ResponseEntity<Partida> emailRecibido(@AuthenticationPrincipal UserDto userDto, @RequestParam("token") String token, HttpServletResponse response) throws IOException {
        Usuario usuario = (userDto != null && userDto.getId() != null) ? usuarioRepository.findById(userDto.getId()).orElse(null) : null;
        if (usuario != null) {
            juegoService.emailRecibido(token, usuario, response);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


}
