package com.antonio.hundirlaflota.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.antonio.hundirlaflota.Modelos.Barco;
import com.antonio.hundirlaflota.Modelos.Casilla;
import com.antonio.hundirlaflota.Modelos.Jugador;
import com.antonio.hundirlaflota.Repositorios.CasillaRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class JugadorService {
   @Autowired
    private BarcoService barcoService;
   @Autowired
    private CasillaRepository casillaRepository;

    @Transactional
	public void generarcasillas(Jugador jugador) {
		for (int i = 0; i < Jugador.getX(); i++) {
			for (int j = 0; j < Jugador.getY(); j++) {
                Casilla casilla=new Casilla(i, j);
                casillaRepository.save(casilla);
				jugador.getTablero().add( casilla);
			}
		}
	}
    @PersistenceContext
private EntityManager entityManager;
    @Transactional
    public void generarbarcos(Jugador jugador) {
        int[] longBarco = jugador.getLongBarco();
        for (int i = 0; i < jugador.getNbarcos(); i++) {
            Barco barco = new Barco(i, longBarco[i]);
            barco = entityManager.merge(barco); 
           //barco=barcoRepository.findById(barco.getId())
            jugador.getBarcos().add(barco);
           barcoService.generarbarco(jugador,barco);
          // barcoService.generarBarcoManual(jugador, barco);
        }
    }
}