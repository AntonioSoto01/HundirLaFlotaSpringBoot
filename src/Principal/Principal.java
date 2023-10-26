package Principal;
import java.util.concurrent.Semaphore;

public class Principal {

    public static void main(String[] args) {
        Semaphore semaphoreJugador1 = new Semaphore(0); // Semáforo para el jugador 1
        Semaphore semaphoreJugador2 = new Semaphore(1); // Semáforo para el jugador 2
        

        
        Jugador jugador = new Jugador(semaphoreJugador1, null,semaphoreJugador2);
        Jugador1 maquina = new Jugador1(semaphoreJugador2, jugador,semaphoreJugador1); // Pasar el jugador1 como rival
        
        jugador.setRival(maquina); // Establecer jugador2 como rival del jugador1
        
        Thread jugador1Thread = new Thread(jugador);
        Thread jugador2Thread = new Thread(maquina);

        jugador1Thread.start();
        jugador2Thread.start();

    }
}
