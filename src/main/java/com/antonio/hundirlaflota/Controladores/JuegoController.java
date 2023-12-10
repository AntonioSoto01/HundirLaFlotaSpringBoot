package com.antonio.hundirlaflota.Controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.antonio.hundirlaflota.ResultadoTurno;
import com.antonio.hundirlaflota.Modelos.Jugador;
import com.antonio.hundirlaflota.Modelos.Jugador1;
import com.antonio.hundirlaflota.Modelos.Partida;
import com.antonio.hundirlaflota.Repositorios.PartidaRepository;
import com.antonio.hundirlaflota.Servicios.JuegoService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/juego")

@CrossOrigin(origins = "${frontend.url}")

public class JuegoController {
    @Value("${frontend.url}")
    private String frontendUrl;
    @Autowired
    private JuegoService juegoService;
    @Autowired
    private PartidaRepository partidaRepository;
    @GetMapping("/iniciar")
    public ResponseEntity<Partida> iniciarJuego(HttpServletRequest request) {
      
       Partida partida=juegoService.iniciarJuego();
       partida.setIp(request.getRemoteAddr());
       partidaRepository.save(partida);
           System.out.println(request.getRemoteAddr());
        return ResponseEntity.ok(partida);
    }

    @PostMapping("/realizar-turno-maquina")
    public ResponseEntity<ResultadoTurno> realizarTurno(@RequestParam int partidaId) {
       Partida partida= partidaRepository.findById(partidaId);
        Jugador jugadorActual = partida.getJugador2();
        ResultadoTurno resultadoTurno = juegoService.realizarTurno(jugadorActual, "",partida);
        if (resultadoTurno.isError()) {
            return ResponseEntity.badRequest().body(resultadoTurno);
        } else {
            return ResponseEntity.ok(resultadoTurno);
        }
    }

    @PostMapping("/realizar-turno-jugador")
    public ResponseEntity<ResultadoTurno> realizarTurno(@RequestParam String casilla,@RequestParam int partidaId) {
             Partida partida= partidaRepository.findById(partidaId);
        Jugador jugadorActual = partida.getJugador1();
        ResultadoTurno resultadoTurno = juegoService.realizarTurno(jugadorActual, casilla,partida);
        System.out.println("id siguiente jugador " + resultadoTurno.getNombreJugador());
        if (resultadoTurno.isError()) {
            return ResponseEntity.badRequest().body(resultadoTurno);
        } else {
            return ResponseEntity.ok(resultadoTurno);
        }
    }
@GetMapping("/cargar")
public Partida cargarPartida (HttpServletRequest request) {
    Partida partida=partidaRepository.findByIpAndTerminar(request.getRemoteAddr(),false);

    return partida;
}

}
