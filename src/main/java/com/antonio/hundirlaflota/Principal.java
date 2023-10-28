package com.antonio.hundirlaflota;

import java.util.concurrent.Semaphore;

public class Principal {

    public static void main(String[] args) {
        Semaphore semaphoreJ = new Semaphore(1); // Semáforo para el jugador 1
        Semaphore semaphoreM = new Semaphore(0); // Semáforo para el jugador 2

        Jugador jugador = new Jugador(semaphoreJ, null, semaphoreM);
        Jugador1 maquina = new Jugador1(semaphoreM, jugador, semaphoreJ);

        jugador.setRival(maquina); // Establecer jugador2 como rival del jugador1

        jugador.start();
        maquina.start();


        try {
            jugador.join(); // Esperar a que el hilo del jugador termine

            maquina.join(); // Esperar a que el hilo de la máquina termine
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
