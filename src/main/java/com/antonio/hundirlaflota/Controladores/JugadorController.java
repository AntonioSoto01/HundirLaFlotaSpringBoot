package com.antonio.hundirlaflota.Controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;
import com.antonio.hundirlaflota.Modelos.Jugador;
import com.antonio.hundirlaflota.Repositorios.JugadorRepository;


@RestController
@CrossOrigin(origins = "${frontend.url}")
public class JugadorController {
    @Value("${frontend.url}")
private String frontendUrl;
    @Autowired
    private JugadorRepository jugadorRepository;

    @GetMapping("/jugadores")
    public List<Jugador> getJugadores() {
        return (List<Jugador>) jugadorRepository.findAll();
    }
    
    @GetMapping("/jugador/{id}")
    public ResponseEntity<Jugador> getJugadorById(@PathVariable Long id) {
        Optional<Jugador> optionalJugador = jugadorRepository.findById(id);

        if (optionalJugador.isPresent()) {
            return ResponseEntity.ok(optionalJugador.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/jugadores")
    void addPlayer(@RequestBody Jugador jugador) {
        jugadorRepository.save(jugador);
    }

}