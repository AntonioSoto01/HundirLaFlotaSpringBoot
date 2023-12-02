package com.antonio.hundirlaflota.Controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.antonio.hundirlaflota.ResultadoTurno;
import com.antonio.hundirlaflota.Modelos.Jugador;
import com.antonio.hundirlaflota.Servicios.JuegoService;

@RestController
@RequestMapping("/api/juego")
@CrossOrigin(origins = "http://localhost:4200")
public class JuegoController {

    @Autowired
    private JuegoService juegoService;

    @GetMapping("/iniciar")
    public ResponseEntity<List<Jugador>> iniciarJuego() {
        juegoService.iniciarJuego();
        List<Jugador> jugadores = juegoService.getJugadores();
        return ResponseEntity.ok(jugadores);
    }

    @PostMapping("/realizar-turno-maquina")
    public ResponseEntity<ResultadoTurno> realizarTurno() {
        Jugador jugadorActual = juegoService.getJugadorPorId((long) 2);
        ResultadoTurno resultadoTurno = juegoService.realizarTurno(jugadorActual, "");
        if (resultadoTurno.isError()) {
            return ResponseEntity.badRequest().body(resultadoTurno);
        } else {
            return ResponseEntity.ok(resultadoTurno);
        }
    }

    @PostMapping("/realizar-turno-jugador")
    public ResponseEntity<ResultadoTurno> realizarTurno(@RequestParam String casilla) {
        Jugador jugadorActual = juegoService.getJugadorPorId((long) 1);
        ResultadoTurno resultadoTurno = juegoService.realizarTurno(jugadorActual, casilla);
        System.out.println("id siguiente jugador "+resultadoTurno.getNombreJugador());
        if (resultadoTurno.isError()) {
            return ResponseEntity.badRequest().body(resultadoTurno);
        } else {
            return ResponseEntity.ok(resultadoTurno);
        }
    }

}
