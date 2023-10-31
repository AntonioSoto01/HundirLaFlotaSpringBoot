package com.antonio.hundirlaflota;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/juego")
public class JuegoController {

    @Autowired
    private JuegoService juegoService;

    @PostMapping("/iniciar")
    public ResponseEntity<String> iniciarJuego() {
        Jugador jugador = new Jugador();
        Jugador maquina = new Jugador();
        
        // Luego, asigna estas instancias a los atributos de tu servicio
        juegoService.setJugador(jugador);
        juegoService.setMaquina(maquina);
        return ResponseEntity.ok("Juego iniciado.");
    }

    @PostMapping("/realizar-turno")
    public ResponseEntity<Jugador> realizarTurno(@RequestParam String jugador) {
        Jugador jugadorActual = juegoService.getJugadorPorNombre(jugador);
        Jugador siguienteJugador = juegoService.realizarTurno(jugadorActual);
        return ResponseEntity.ok(siguienteJugador);
    }
}
