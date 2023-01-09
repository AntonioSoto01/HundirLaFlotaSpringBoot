package Principal;

import java.util.Scanner;

public class Principal {

	public static void main(String[] args) {
		Jugador jugador1 = new Jugador1();
		Scanner s = new Scanner(System.in);
		jugador1.generarcasillas();
		jugador1.generarbarcos();
		Jugador maquina = new Jugador();
		maquina.generarcasillas();
		maquina.generarbarcos();

		String tocado = "";
		maquina.ver(false);
		while (!tocado.equals("Final")) {
			do {
				System.out.println("Tu turno" + '\n');
				tocado = maquina.disparado();
				maquina.ver(false);
			} while (tocado.equals("Tocado"));
			if (!tocado.equals("Final")) {
				do {
					System.out.println();
						System.out.println(jugador1.espacios() + "Pulsa enter para continuar");
					s.nextLine();
					System.out.println(jugador1.espacios() + "Turno del contrario" + '\n');
					tocado = jugador1.disparado();
					jugador1.ver(true);
				} while (tocado.equals("Tocado"));
			}
		}
	}

}
