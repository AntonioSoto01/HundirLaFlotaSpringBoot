package com.antonio.hundirlaflota;

import com.antonio.hundirlaflota.Modelos.Casilla;


import lombok.Data;

@Data
public class ResultadoTurno {
    private boolean terminar;
    private boolean error;
    private String mensajeError;
    private String resultadoDisparo;
    private String nombreJugador;
    private Casilla casillaDisparada;

}
