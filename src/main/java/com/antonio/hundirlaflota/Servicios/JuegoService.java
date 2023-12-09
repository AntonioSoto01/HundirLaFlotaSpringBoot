package com.antonio.hundirlaflota.Servicios;

import org.springframework.stereotype.Service;

import com.antonio.hundirlaflota.ResultadoTurno;
import com.antonio.hundirlaflota.Modelos.Casilla;
import com.antonio.hundirlaflota.Modelos.Jugador;
import com.antonio.hundirlaflota.Modelos.Jugador1;
import com.antonio.hundirlaflota.Modelos.Partida;
import com.antonio.hundirlaflota.Repositorios.JugadorRepository;
import com.antonio.hundirlaflota.Repositorios.PartidaRepository;

import jakarta.transaction.Transactional;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;

@Service
public class JuegoService {
    @Autowired
    private JugadorRepository jugadorRepository;
    @Autowired
    private PartidaRepository partidaRepository;
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
    public ResultadoTurno realizarTurno(Jugador jugadorActual, String casilla,Partida partida) {

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
            } else if (resultadoDisparo.equals("Agua")) {

                Jugador siguienteJugador = obtenerSiguienteJugador(jugadorActual);
                partida.setTurno(siguienteJugador.getNombre());
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
        Partida partida = new Partida(new Jugador(), new Jugador1(), null);
        partidaRepository.save(partida);
        Jugador jugador = partida.getJugador1();
        Jugador maquina = partida.getJugador2();
       partida.setTurno(jugador.getNombre());
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
