package com.antonio.hundirlaflota;

import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Service
public class BarcoService {

    @Autowired
    private BarcoRepository barcoRepository;


    @Transactional
    public void generarbarco(Jugador jugador, Barco barco) {
        boolean valido;
    
        int x, y;
        int horizontal;
    
        do {
            barco.getPosiciones().clear();
            valido = true;
            horizontal = (int) (Math.random() * 2);
    
            if (horizontal < 1) {
                x = (int) (Math.random() * (Jugador.getX() - barco.getLongitud() + 1));
                y = (int) (Math.random() * Jugador.getY());
            } else {
                x = (int) (Math.random() * Jugador.getX());
                y = (int) (Math.random() * (Jugador.getY() - barco.getLongitud() + 1));
            }
    
            int x1 = x;
            int y1 = y;
    
            while (x1 <= x + barco.getLongitud() - 1 && y1 <= y + barco.getLongitud() - 1 && valido) {
                Casilla casilla = jugador.getCasilla(x1, y1);
                if (casilla == null || !casilla.isPuedebarco()) {
                    valido = false;
                } else {
                    barco.getPosiciones().add(casilla);
                    x1++;
                }
            }
        } while (!valido);
    
        colocarBarco(barco);
        puedebarco(x, y, horizontal, jugador, barco);
    }
    

    @Transactional
    public void colocarBarco(Barco barco) {
        for (Casilla casilla : barco.getPosiciones()) {
            casilla.setBarco(barco);
        }
        barcoRepository.save(barco);
    }
    

    @Transactional
    public void puedebarco(int x, int y, int horizontal, Jugador jugador, Barco barco) {
        int aux1 = 0;
        int aux2 = 0;

        if (horizontal < 1) {
            aux1 = x + barco.getLongitud();
            aux2 = y + 1;
        } else {
            aux1 = x + 1;
            aux2 = y + barco.getLongitud();
        }
        for (int i = x - 1; i <= aux1; i++) {
            for (int j = y - 1; j <= aux2; j++) {
                try {
                    jugador.getCasilla(i, j).setPuedebarco(false);
                    barco.getAlrededor().add(jugador.getCasilla(i, j));
                } catch (Exception e) {
                }
            }
        }
    }

    @Transactional
    public void puededisparar(Barco barco) {
        for (Casilla casilla : barco.getAlrededor()) {
            casilla.setPuededisparar(false);
        }
    }
}
