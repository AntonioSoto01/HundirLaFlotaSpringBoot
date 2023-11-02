package com.antonio.hundirlaflota.Controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.antonio.hundirlaflota.Modelos.Casilla;
import com.antonio.hundirlaflota.Modelos.Jugador;
import com.antonio.hundirlaflota.Servicios.JuegoService;


@RestController
@RequestMapping("/api/juego")
public class JuegoController {

    @Autowired
    private JuegoService juegoService;

    @GetMapping("/iniciar")
    public ResponseEntity<List<Jugador>> iniciarJuego() {
        juegoService.iniciarJuego();
        List<Jugador> jugadores = juegoService.getJugadores();
        return ResponseEntity.ok(jugadores);
    }
    @PostMapping("/realizar-turno")
    public  ResponseEntity<Jugador> realizarTurno() {
        Jugador jugadorActual = juegoService.getJugadorPorId((long)2);
        Jugador siguienteJugador = juegoService.realizarTurno(jugadorActual,"");
        return ResponseEntity.ok(jugadorActual);
    }
        @PostMapping("/realizar-turno-jugador")
    public  ResponseEntity<Jugador> realizarTurno(@RequestParam String casilla) {
        Jugador jugadorActual = juegoService.getJugadorPorId((long)1);
        Jugador siguienteJugador = juegoService.realizarTurno(jugadorActual,casilla);
        return ResponseEntity.ok(jugadorActual);
    }
}
