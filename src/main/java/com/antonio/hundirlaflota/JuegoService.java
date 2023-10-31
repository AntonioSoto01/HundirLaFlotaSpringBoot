package com.antonio.hundirlaflota;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JuegoService {
    private final JugadorRepository jugadorRepository;
    private Jugador jugador;
    private Jugador maquina;
    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public void setMaquina(Jugador maquina) {
        this.maquina = maquina;
    }
    @Autowired
    public JuegoService(JugadorRepository jugadorRepository) {
        this.jugadorRepository = jugadorRepository;
    }

    public void guardarJugador(Jugador jugador) {
        jugadorRepository.save(jugador);
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
            // El juego ha terminado, actualiza el estado del jugador y retorna el mismo jugador.
            jugadorActual.setTerminar(true);
            return jugadorActual;
        } else if (resultadoDisparo.equals("Tocado")) {
            // El jugador ha tocado un barco, retorna el mismo jugador.
            return jugadorActual;
        } else {
            Jugador otroJugador = (Jugador) ((jugadorActual.equals(jugadorRepository.findById(1L).orElse(null))) ? 
            jugadorRepository.findById(2L).orElse(null) : jugadorRepository.findById(1L).orElse(null));
    return otroJugador;
        }
    }

    public Jugador getJugadorPorNombre(String nombre) {
        return jugadorRepository.findByNombre(nombre).orElse(null);
    }
}
