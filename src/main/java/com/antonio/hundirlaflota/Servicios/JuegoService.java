package com.antonio.hundirlaflota.Servicios;

import com.antonio.hundirlaflota.Modelos.*;
import com.antonio.hundirlaflota.Repositorios.JugadorRepository;
import com.antonio.hundirlaflota.Repositorios.PartidaRepository;
import com.antonio.hundirlaflota.Repositorios.UsuarioRepository;
import com.antonio.hundirlaflota.config.jwt.JwtTokenProvider;
import com.antonio.hundirlaflota.dto.EmailData;
import com.antonio.hundirlaflota.dto.ResultadoTurno;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JuegoService {

    private final JugadorRepository jugadorRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final PartidaRepository partidaRepository;
    private final JugadorService jugadorService;

    private final Jugador1Service jugador1Service;
    private final EmailService emailService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UsuarioRepository usuarioRepository;
    @Value("${frontend.url}")
    private String frontendUrl;

    public Jugador obtenerJugadorPorId(Long id) {
        return jugadorRepository.findById(id).orElse(null);
    }

    @Transactional
    public ResultadoTurno realizarTurno(Jugador jugadorActual, String casilla, Partida partida) {

        ResultadoTurno resultadoTurno = new ResultadoTurno();
        String resultadoDisparo = "";
        // jugadorActual.enter();

        Casilla casillaDisparada = obtenerCasillaDisparada(jugadorActual, casilla);
        if (casillaDisparada == null) {
            resultadoTurno.setError(true);
            resultadoTurno.setMensajeError("Casilla no válida o ya disparada. Inténtalo de nuevo.");
            return resultadoTurno;
        } else {
            resultadoDisparo = jugadorService.disparado(jugadorActual, casillaDisparada);
            resultadoTurno.setResultadoDisparo(resultadoDisparo);
            resultadoTurno.setCasillaDisparada(casillaDisparada);
            if (resultadoDisparo.equals("Final")) {
                resultadoTurno.setTerminar(true);
                partida.setTerminar(true);
            } else if (resultadoDisparo.equals("Agua")) {

                Jugador siguienteJugador = obtenerSiguienteJugador(jugadorActual, partida);
                partida.setTurno(siguienteJugador.getRol());
                resultadoTurno.setNombreJugador(siguienteJugador.getRol());
                return resultadoTurno;

            }
        }

        jugadorRepository.save(jugadorActual);
        resultadoTurno.setNombreJugador(jugadorActual.getRol());
        return resultadoTurno;
    }

    @Transactional
    public Casilla obtenerCasillaDisparada(Jugador jugadorActual, String casilla) {
        Casilla casillaDis;
        if (jugadorActual instanceof Jugador1) {
            casillaDis = jugador1Service.casillaDisparada((Jugador1) jugadorActual);
        } else {
            casillaDis = jugadorService.casillaDisparada(jugadorActual, casilla);
        }
        System.out.println(jugadorActual.getNombre() + "disparaste a " + casillaDis.getCadena());
        return casillaDis;
    }

    @Transactional
    public Partida iniciarJuego() {
        Partida partida = new Partida(new Jugador(), new Jugador1(), null, false);
        partidaRepository.save(partida);
        Jugador jugador = partida.getJugador1();
        Jugador maquina = partida.getJugador2();

        jugador.setRol("jugador1");
        maquina.setRol("jugador2");
        partida.setTurno(jugador.getRol());
        iniciarJugador(jugador);
        iniciarJugador(maquina);
        return partida;
    }

    @Transactional
    public Jugador iniciarJugador(Jugador jugador) {
        jugadorService.generarcasillas(jugador);
        jugadorService.generarbarcos(jugador);
        jugadorRepository.save(jugador);

        return jugador;
    }

    @Transactional
    public Jugador obtenerSiguienteJugador(Jugador jugadorActual, Partida partida) {
        Jugador siguienteJugador;
        siguienteJugador = (jugadorActual == partida.getJugador1()) ? partida.getJugador2() : partida.getJugador1();

        return siguienteJugador;
    }

    public Jugador getJugadorPorId(long id) {
        Jugador jugador = jugadorRepository.findById(id);

        return jugador;
    }

    public List<Jugador> getJugadores() {
        return (List<Jugador>) jugadorRepository.findAll();
    }

    @Transactional
    public Partida iniciarJuego2Jugadores(String email, Usuario usuario) {
        Jugador jugador1 = new Jugador();
        Jugador jugador2 = new Jugador();
        jugador1.setRol("jugador1");
        jugador2.setRol("jugador2");
        jugadorRepository.save(jugador1);
        jugadorRepository.save(jugador2);
        Partida partida = new Partida(jugador1, jugador2, null, false);
        partida.getUsuarios().add(usuario);
        partidaRepository.save(partida);
        String token = jwtTokenProvider.generateToken(String.valueOf(partida.getId()), JwtTokenProvider.getMIDDLEEXPIRATIONTIME());
        EmailData emailData = new EmailData("Game invitation", " to join to the game", email, token, "/email-recibido");
        emailService.sendEmail(emailData);
        partida.setTokenPartida(token);
        partidaRepository.save(partida);
        return partida;
    }

    @Transactional
    public void emailRecibido(String token, Usuario usuario, HttpServletResponse response) throws IOException {
        Partida partida = partidaRepository.findByTokenPartida(token).orElse(null);
        // Comprueba que la partida no tenga ya dos jugadores
        if (partida.getUsuarios().size() < 2) {
            partida.getUsuarios().add(usuario);
            randomJugador(partida);
            partida.setTurno(partida.getJugador1().getRol());
            iniciarJugador(partida.getJugador1());
            iniciarJugador(partida.getJugador2());
            partidaRepository.save(partida);
            messagingTemplate.convertAndSend("/topic/partida/" + partida.getId(), partida);
            response.sendRedirect(frontendUrl);
        }
    }

    private void randomJugador(Partida partida) {
        int random = (int) (Math.random() * 2);
        if (random == 0) {
            partida.getJugador1().setUsuario(partida.getUsuarios().get(0));
            partida.getJugador2().setUsuario(partida.getUsuarios().get(1));
        } else {
            partida.getJugador1().setUsuario(partida.getUsuarios().get(1));
            partida.getJugador2().setUsuario(partida.getUsuarios().get(0));
        }
        partida.getJugador1().setNombre(partida.getJugador1().getUsuario().getNombre());
        partida.getJugador2().setNombre(partida.getJugador2().getUsuario().getNombre());
        partidaRepository.save(partida);
    }

}
