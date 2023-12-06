package com.antonio.hundirlaflota.Servicios;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.antonio.hundirlaflota.Modelos.Barco;
import com.antonio.hundirlaflota.Modelos.Casilla;
import com.antonio.hundirlaflota.Modelos.Jugador;
import com.antonio.hundirlaflota.Modelos.Jugador1;
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
	@Autowired
	private Jugador1Service jugador1Service;

	@Transactional
	public void generarcasillas(Jugador jugador) {
		for (int i = 0; i < Jugador.getX(); i++) {
			for (int j = 0; j < Jugador.getY(); j++) {
				Casilla casilla = new Casilla(i, j);
				casillaRepository.save(casilla);
				jugador.getTablero().add(casilla);
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
			// barco=barcoRepository.findById(barco.getId())
			jugador.getBarcos().add(barco);
			barcoService.generarbarco(jugador, barco);
			// barcoService.generarBarcoManual(jugador, barco);
		}
	}

	@Transactional
	public String disparado(Jugador jugador, Casilla casillaDisparada) {
		casillaDisparada.setDisparado(true);
		Barco barcoDisparado = casillaDisparada.getBarco();

		if (barcoDisparado != null) {
			if (jugador instanceof Jugador1) {
				jugador1Service.IATocado(casillaDisparada,(Jugador1)jugador);
			}
			barcoDisparado.setTocado(barcoDisparado.getTocado() + 1);
			if (barcoDisparado.getTocado() == barcoDisparado.getLongitud()) {
				barcoService.hundir(barcoDisparado);
				if (jugador instanceof Jugador1) {
					jugador1Service.IAHundido((Jugador1)jugador);
				}
				jugador.setBarcoshundidos(jugador.getBarcoshundidos() + 1);
				if (jugador.getBarcoshundidos() == jugador.getNbarcos()) { // Final
					return Jugador.getFinal();
				}
				return Jugador.getHundido();
			} else {

				return Jugador.getTocado();
			}
		} else { // Agua
			if (jugador instanceof Jugador1) {
				jugador1Service.IAgua((Jugador1)jugador);
			}
			return "Agua";
		}
	}

	@Transactional
	public Casilla casillaDisparada(Jugador jugador, String cadena) {
		int x, y;

		try {
			y = (int) (Character.toUpperCase(cadena.charAt(0))) - 'A';
			x = Integer.parseInt(cadena.substring(1)) - 1;

			if (x >= 0 && x < jugador.getX() && y >= 0 && y < jugador.getY()
					&& !jugador.getCasilla(x, y).isDisparado()) {
			} else {
				System.out.println("Coordenada inválida o casilla ya disparada. Inténtalo de nuevo.");
				return null;
			}
		} catch (Exception e) {
			System.out.println("Entrada inválida. Inténtalo de nuevo.");
			return null;
		}


		Casilla casillaDisparada = jugador.getCasilla(x, y);
		casillaDisparada.setDisparado(true);
		casillaRepository.save(casillaDisparada);
		return casillaDisparada;
	}

}