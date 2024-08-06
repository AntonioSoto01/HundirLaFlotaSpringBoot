package com.antonio.hundirlaflota.Controladores;

import com.antonio.hundirlaflota.Excepciones.AppException;
import com.antonio.hundirlaflota.Modelos.Jugador;
import com.antonio.hundirlaflota.Repositorios.JugadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class JugadorController {

    private final JugadorRepository jugadorRepository;

    @GetMapping("/jugador/{id}")
    public ResponseEntity<Jugador> getJugadorById(@PathVariable Long id) {
        Jugador jugador = jugadorRepository.findById(id).orElseThrow(() -> new AppException("Jugador no encontrado", HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(jugador);
    }


}