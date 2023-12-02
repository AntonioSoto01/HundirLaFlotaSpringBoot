package com.antonio.hundirlaflota.Servicios;

import java.util.Scanner;

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
		
		if (barcoDisparado != null) { // Tocado
			jugador.IATocado(casillaDisparada);
			barcoDisparado.setTocado(barcoDisparado.getTocado() + 1);
			
			if (barcoDisparado.getTocado() == barcoDisparado.getLongitud()) {
				barcoDisparado.setHundido(true);
				System.out.println(jugador.espacios() + "HUNDIDO!!!");
				jugador.IAHundido();
				jugador.setBarcoshundidos(jugador.getBarcoshundidos() + 1);
				
				if (jugador.getBarcoshundidos() == jugador.getNbarcos()) { // Final
					System.out.println(jugador.espacios() + "Todos los barcos hundidos");
					return Jugador.getFinal();
				}
				
				return Jugador.getHundido();
			} else {
				System.out.println(jugador.espacios() + "TOCADO!!!");
				return Jugador.getTocado();
			}
		} else { // Agua
			System.out.println(jugador.espacios() + "Agua");
			jugador.IAgua();
			return "Agua";
		}
	}
	
	@Transactional
	public Casilla casillaDisparada(Jugador jugador, String cadena) {
		int x, y;

	
			try {
				y = (int) (Character.toUpperCase(cadena.charAt(0))) - 'A';
				x = Integer.parseInt(cadena.substring(1)) - 1;
	
				if (x >= 0 && x < jugador.getX() && y >= 0 && y < jugador.getY() && !jugador.getCasilla(x, y).isDisparado()) {
				} else {
					System.out.println("Coordenada inválida o casilla ya disparada. Inténtalo de nuevo.");
					return null; 
				}
			} catch (Exception e) {
				System.out.println("Entrada inválida. Inténtalo de nuevo.");
				return null; 
			}
		
	
		System.out.println("Disparaste a " + jugador.getCasilla(x, y).toString());
		Casilla casillaDisparada = jugador.getCasilla(x, y);
		casillaDisparada.setDisparado(true);
		casillaRepository.save(casillaDisparada);
		return casillaDisparada;
	}
	
}