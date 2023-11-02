package com.antonio.hundirlaflota;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class JuegoService {
        @Autowired
    private  JugadorRepository jugadorRepository;
    private Jugador jugador;
    private Jugador maquina;

    @Autowired
    private JugadorService jugadorService;

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

    public Jugador realizarTurno(Jugador jugadorActual) {
        String resultadoDisparo = "";
        jugadorActual.enter();
        jugadorActual.mostrarMensajeTurno(resultadoDisparo);
        resultadoDisparo = jugadorActual.disparado();

        if (resultadoDisparo.equals("Final")) {
            jugadorActual.setTerminar(true);
            jugadorRepository.save(jugadorActual);
            return jugadorActual;
        } else if (resultadoDisparo.equals("Tocado")) {
            jugadorRepository.save(jugadorActual);
            return jugadorActual;
        } else {
            Jugador siguienteJugador = (jugadorActual.equals(jugador)) ? maquina : jugador;
            jugadorRepository.save(jugadorActual);
            jugadorRepository.save(siguienteJugador);
            return siguienteJugador;
        }
    }

    public Jugador getJugadorPorNombre(String nombre) {
        Jugador jugador = jugadorRepository.findByNombre(nombre).orElse(null);
        return jugador;
    }

    @Transactional
    public void iniciarJuego() {
        Jugador jugador = new Jugador();
        setJugador(jugador);
        jugadorService.generarcasillas(jugador);
        jugadorService.generarbarcos(jugador);
        jugadorRepository.save(jugador);
    }
}
