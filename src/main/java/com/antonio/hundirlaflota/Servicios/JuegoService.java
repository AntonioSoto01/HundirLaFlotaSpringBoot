package com.antonio.hundirlaflota.Servicios;

import org.springframework.stereotype.Service;

import com.antonio.hundirlaflota.ResultadoTurno;
import com.antonio.hundirlaflota.Modelos.Casilla;
import com.antonio.hundirlaflota.Modelos.Jugador;
import com.antonio.hundirlaflota.Modelos.Jugador1;

import com.antonio.hundirlaflota.Repositorios.JugadorRepository;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class JuegoService {
    @Autowired
    private JugadorRepository jugadorRepository;

    @Autowired
    private JugadorService jugadorService;
    @Autowired
    private Jugador1Service jugador1Service;

    public JuegoService(JugadorRepository jugadorRepository) {
        this.jugadorRepository = jugadorRepository;
    }

    public Jugador obtenerJugadorPorId(Long id) {
        return jugadorRepository.findById(id).orElse(null);
    }

    @Transactional
    public ResultadoTurno realizarTurno(Jugador jugadorActual, String casilla) {

        ResultadoTurno resultadoTurno = new ResultadoTurno();
        String resultadoDisparo = "";
        // jugadorActual.enter();
        jugadorActual.mostrarMensajeTurno(resultadoDisparo);

        Casilla casillaDisparada = obtenerCasillaDisparada(jugadorActual, casilla);
        if (casillaDisparada == null) {
            resultadoTurno.setError(true);
            resultadoTurno.setMensajeError("Casilla no válida o ya disparada. Inténtalo de nuevo.");
            return resultadoTurno;
        } else {

            resultadoDisparo = jugadorService.disparado(jugadorActual, casillaDisparada);
            resultadoTurno.setResultadoDisparo(resultadoDisparo);
            resultadoTurno.setCasillaDisparada(casillaDisparada);
        jugadorActual.ver(jugadorActual.getVer());
            if (resultadoDisparo.equals("Final")) {
                resultadoTurno.setTerminar(true);
            } else if (resultadoDisparo.equals("Agua")) {

                Jugador siguienteJugador = obtenerSiguienteJugador(jugadorActual);
                resultadoTurno.setNombreJugador(siguienteJugador.getNombre());
                return resultadoTurno;

            }
        }

        jugadorRepository.save(jugadorActual);
        resultadoTurno.setNombreJugador(jugadorActual.getNombre());
        return resultadoTurno;
    }

    @Transactional
    private Casilla obtenerCasillaDisparada(Jugador jugadorActual, String casilla) {
        if (jugadorActual instanceof Jugador1) {
            return jugador1Service.casillaDisparada((Jugador1) jugadorActual);
        } else {
            return jugadorService.casillaDisparada(jugadorActual, casilla);
        }
    }

    @Transactional
    public void iniciarJuego() {
        iniciarJugador(new Jugador());
        iniciarJugador(new Jugador1());
    }

    public Jugador iniciarJugador(Jugador jugador) {
        jugadorService.generarcasillas(jugador);
        jugadorService.generarbarcos(jugador);
        jugadorRepository.save(jugador);

        return jugador;
    }

    @Transactional
    public Jugador obtenerSiguienteJugador(Jugador jugadorActual) {

        long siguienteJugadorId = (jugadorActual.getId() == 1) ? 2 : 1;

        Jugador siguienteJugador = jugadorRepository.findById(siguienteJugadorId);
        return siguienteJugador;
    }

    public Jugador getJugadorPorId(long id) {
        Jugador jugador = jugadorRepository.findById(id);

        return jugador;
    }

    public List<Jugador> getJugadores() {
        return (List<Jugador>) jugadorRepository.findAll();
    }
}
