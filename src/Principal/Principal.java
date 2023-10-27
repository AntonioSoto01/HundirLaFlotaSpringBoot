package Principal;

import java.util.concurrent.Semaphore;

public class Principal {

    public static void main(String[] args) {
        Semaphore semaphoreJ = new Semaphore(0); // Semáforo para el jugador 1
        Semaphore semaphoreM = new Semaphore(1); // Semáforo para el jugador 2
       Meta meta = new Meta();
        Jugador jugador = new Jugador(semaphoreJ, null, semaphoreM,meta);
        Jugador1 maquina = new Jugador1(semaphoreM, jugador, semaphoreJ,meta);

        jugador.setRival(maquina); // Establecer jugador2 como rival del jugador1

        jugador.start();
        maquina.start();
 meta.inicio();

        if (jugador.getTerminar()) {
            System.out.println("Ha ganado el jugador");

        } else if(maquina.getTerminar()){


            System.out.println("Ha ganado la maquina");
            semaphoreJ.release();
        }

        try {
            jugador.join(); // Esperar a que el hilo del jugador termine

            maquina.join(); // Esperar a que el hilo de la máquina termine
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
