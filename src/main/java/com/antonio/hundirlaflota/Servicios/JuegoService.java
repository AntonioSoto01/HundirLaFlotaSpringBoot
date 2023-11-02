package com.antonio.hundirlaflota.Servicios;

import org.springframework.stereotype.Service;

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

    private Jugador jugador;
    private Jugador maquina;

    @Autowired
    private JugadorService jugadorService;
    @Autowired
    private Jugador1Service jugador1Service;

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public void setMaquina(Jugador maquina) {
        this.maquina = maquina;
    }

    public JuegoService(JugadorRepository jugadorRepository) {
        this.jugadorRepository = jugadorRepository;
    }

    public Jugador obtenerJugadorPorId(Long id) {
        return jugadorRepository.findById(id).orElse(null);
    }

    public Jugador realizarTurno(Jugador jugadorActual, String casilla) {
        String resultadoDisparo = "";
        //jugadorActual.enter();
        jugadorActual.mostrarMensajeTurno(resultadoDisparo);
        Casilla casillaDisparada = null;
        if (jugadorActual instanceof Jugador1) {
            casillaDisparada = jugador1Service.casillaDisparada((Jugador1) (jugadorActual));
        } else {
            casillaDisparada = jugadorService.casillaDisparada(jugadorActual, casilla);

        }
        if (casillaDisparada != null) {
            resultadoDisparo = jugadorService.disparado(jugadorActual, casillaDisparada);
        }
        if (resultadoDisparo.equals("Final")) {
            jugadorActual.setTerminar(true);
        } else if (resultadoDisparo.equals("Tocado")) {
            jugadorRepository.save(jugadorActual);
        } else if (resultadoDisparo.equals("Agua")) {
            Jugador siguienteJugador = obtenerSiguienteJugador(jugadorActual);
            jugadorRepository.save(jugadorActual);
            jugadorRepository.save(siguienteJugador);
            return siguienteJugador;
        }
        jugadorRepository.save(jugadorActual);
        return jugadorActual;
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
