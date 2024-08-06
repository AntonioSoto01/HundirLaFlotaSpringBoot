package com.antonio.hundirlaflota.dto;

import com.antonio.hundirlaflota.Modelos.Partida;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenJugador {
    Partida partida;
    String rol;


}
