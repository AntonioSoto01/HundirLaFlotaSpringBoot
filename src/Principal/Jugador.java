package Principal;

import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Jugador extends Thread {
	private static final String FINAL = "Final";
	private static final String TOCADO = "Tocado";
	private static final int x = 10;
	private static final int y = 10;
	private static final int[] longBarco = { 1, 1, 1, 1, 2, 2, 2, 3, 3, 4 };
	private static final int nbarcos = longBarco.length;
	private Casilla[][] tablero = new Casilla[x][y];
	private Barco[] barcos = new Barco[nbarcos];
	private int barcoshundidos = 0;
	private Semaphore semaphore;
	private Semaphore semaphoreRival;
	private boolean terminar;
	private Jugador rival;
	private String nombre;
	private boolean ver;

	public Jugador(Semaphore semaphore, Jugador rival, Semaphore semaphoreRival) {
		this.semaphore = semaphore;
		this.terminar = false;
		this.rival = rival;
		this.semaphoreRival = semaphoreRival;
		ver = false;

		this.nombre = "jugador";
	}

	public int getNbarco() {
		return nbarcos;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getBarcoshundidos() {
		return barcoshundidos;
	}

	public void setBarcoshundidos(int barcoshundidos) {
		this.barcoshundidos = barcoshundidos;
	}

	public boolean getTerminar() {
		return terminar;
	}

	public boolean getVer() {
		return ver;
	}

	public void setVer(boolean ver) {
		this.ver = ver;
	}

	public void setTerminar(boolean terminar) {
		this.terminar = terminar;
	}

	public Jugador getRival() {
		return rival;
	}

	public void setRival(Jugador rival) {
		this.rival = rival;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Casilla getCasilla(int x, int y) {
		return tablero[x][y];
	}

	public Barco getBarco(int x) {
		return barcos[x];
	}

	public Semaphore getSemaphore() {
		return semaphore;
	}

	public Semaphore getSemaphoreRival() {
		return semaphoreRival;
	}

	public void run() {
		try {
			iniciarJuego();

			while (!terminar) {

				realizarTurno();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void iniciarJuego() throws InterruptedException {
		semaphore.acquire();
		generarcasillas();
		generarbarcos();
		semaphoreRival.release();
	}

	private void realizarTurno() throws InterruptedException {
		String tocado = "";
		semaphore.acquire();
		if (!terminar) {
			do {
				enter();
				mostrarMensajeTurno(tocado);
				tocado = disparado();
				ver(getVer());

				if (tocado.equals(FINAL)) {
					terminar = true;
					rival.setTerminar(true);
				}

			} while (tocado.equals(TOCADO));

			semaphoreRival.release();
		}
	}

	private void mostrarMensajeTurno(String tocado) {
		String mensaje = (tocado.equals(TOCADO)) ? " tiene otro turno" : "";
		System.out.println(espacios() + nombre + mensaje + '\n');
	}

	public void generarcasillas() {
		for (int i = 0; i < tablero.length; i++) {
			for (int j = 0; j < tablero[i].length; j++) {
				tablero[i][j] = new Casilla(i, j);
			}
		}
	}

	public void generarbarcos() {
		for (int i = 0; i < barcos.length; i++) {
			barcos[i] = new Barco(i, longBarco[i]);
			barcos[i].generarbarco(this);
		}

	}

	public String disparado() {

		Casilla casillaDisparada = casillaDisparada();
		casillaDisparada.setDisparado(true);
		if (casillaDisparada.getBarco() != null) {// Tocado
			IATocado(casillaDisparada);
			casillaDisparada.getBarco().setTocado(casillaDisparada.getBarco().getTocado() + 1);
			if (casillaDisparada.getBarco().getTocado() == casillaDisparada.getBarco().getLongitud()) {// Hundido
				System.out.println(espacios() + "HUNDIDO!!!");
				IAHundido();
				this.setBarcoshundidos(this.getBarcoshundidos() + 1);
				if (this.getBarcoshundidos() == this.getNbarco()) {// Final
					System.out.println(espacios() + "Todos los barcos hundidos");
					return FINAL;
				}
				// return "Hundido";
			} else {
				System.out.println(espacios() + "TOCADO!!!");
			}

			return TOCADO;
		} // Agua
		System.out.println(espacios() + "Agua");
		IAgua();

		return "Agua";
	}

	public Casilla casillaDisparada() {

		int x = 0;
		int y = 0;
		boolean valido = true;
		Scanner s = new Scanner(System.in);
		boolean veces = false;
		do {
			if (veces) {
				System.out.println("Ya has disparado esa casilla");
			}
			do {
				valido = true;
				System.out.println("Introduce una posicion y(de la A a la J) y x(de 1 a 10)");
				String cadena = s.next();

				try {

					y = (int) ((Character.toUpperCase(cadena.charAt(0))) - 'A');
					x = Integer.parseInt((cadena.substring(1))) - 1;
					if (x < 0 || x > this.getX() - 1 || y < 0 || y > this.getY() - 1) {
						throw new java.lang.ArrayIndexOutOfBoundsException();
					}

				} catch (Exception e) {
					valido = false;
					System.out.println(espacios() + "Valor no admitido");
				}
			} while (!valido);
			veces = true;
		} while (this.getCasilla(x, y).isDisparado());
		System.out.println("Dispara a " + this.getCasilla(x, y).toString());

		return this.getCasilla(x, y);
	}

	public void enter() {

	}

	public void IATocado(Casilla casillaDisparada) {

	}

	public void IAHundido() {

	}

	public void IAgua() {
	}

	public String espacios() {
		return "";
	};

	public void ver(boolean maquina) {
		char a = 'A';
		System.out.print(espacios() + String.format("%-6s", ""));
		for (int i = 0; i < this.getX(); i++) {
			System.out.print(String.format("%-6s", (char) (a + i)));// letra eje y
		}
		System.out.println();
		System.out.println();
		for (int i = 0; i < this.getX(); i++) {

			System.out.print(espacios() + String.format("%-6s", String.format("%2d", i + 1) + ")"));// numero eje x
			for (int j = 0; j < this.getY(); j++) {
				if (this.getCasilla(i, j).isDisparado()) {

					if (this.getCasilla(i, j).getBarco() != null) {
						System.out.print(String.format("%-6s", "T"));// tocado
					} else {

						System.out.print(String.format("%-6s", "A"));// agua
					}
				} else {
					if (maquina && this.getCasilla(i, j).getBarco() != null) {
						System.out.print(String.format("%-6s", "B"));// barco visible

					} else {
						// if(maquina&&!this.getCasilla(i, j).isPuededisparar()){
						// System.out.print(String.format("%-6s", "R"));
						// }else {
						System.out.print(String.format("%-6s", "-"));// sin disparar o barco invisible
						// }
					}

				}

			}
			System.out.println('\n');
			// System.out.println('\n');
		}
	}

}
