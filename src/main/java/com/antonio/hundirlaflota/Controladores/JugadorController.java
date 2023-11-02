package com.antonio.hundirlaflota.Controladores;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.antonio.hundirlaflota.Modelos.Jugador;
import com.antonio.hundirlaflota.Repositorios.JugadorRepository;



@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class JugadorController {
	@Autowired
private JugadorRepository jugadorRepository;
@GetMapping("/jugadores")
    public List<Jugador> getJugadores() {
        return (List<Jugador>) jugadorRepository.findAll();
    }

    @PostMapping("/jugadores")
    void addPlayer(@RequestBody Jugador jugador) {
    	jugadorRepository.save(jugador);
    }
}