package com.antonio.hundirlaflota;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class JugadorService {
   @Autowired
    private BarcoService barcoService;

    @Autowired
    private BarcoRepository barcoRepository;
    @Transactional
	public void generarcasillas(Jugador jugador) {
		for (int i = 0; i < Jugador.getX(); i++) {
			for (int j = 0; j < Jugador.getY(); j++) {
				jugador.getTablero().add( new Casilla(i, j));
			}
		}
	}
    @Transactional
    public void generarbarcos(Jugador jugador) {
        int[] longBarco = jugador.getLongBarco();
        for (int i = 0; i < jugador.getNbarcos(); i++) {
            Barco barco = new Barco(i, longBarco[i]);
            barcoRepository.save(barco);
            jugador.getBarcos().add(barco);
            barcoService.generarbarco(jugador,barco);
        }
    }
}