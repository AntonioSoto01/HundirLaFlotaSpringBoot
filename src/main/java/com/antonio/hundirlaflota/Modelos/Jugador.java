package com.antonio.hundirlaflota.Modelos;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.transaction.Transactional;
import lombok.Data;

@Data
@Entity
public class Jugador {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private static final String FINAL = "Final";
	private static final String TOCADO = "Tocado";
	private static final int x = 10;
	private static final int y = 10;
	private static final int[] longBarco = { 1, 1, 1, 1, 2, 2, 2, 3, 3, 4 };
	private static final int nbarcos = longBarco.length;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Casilla> tablero = new ArrayList<Casilla>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Barco> barcos = new ArrayList<Barco>();
	private int barcoshundidos = 0;
	private boolean terminar;
	private String nombre;
	private boolean ver;

	public Jugador() {
		this.terminar = false;
		ver = false;
		this.nombre = "jugador";
	}

	public int getNbarcos() {
		return nbarcos;
	}

	public boolean getVer() {
		return ver;
	}

	public static int getX() {
		return x;
	}

	public static int getY() {
		return y;
	}

	public int[] getLongBarco() {
		return longBarco;
	}

	public Casilla getCasilla(int x, int y) {
		return tablero.get(x * this.x + y);
	}

	public void mostrarMensajeTurno(String tocado) {
		String mensaje = (tocado.equals(TOCADO)) ? " tiene otro turno" : "";
		System.out.println(espacios() + nombre + mensaje + '\n');
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
				if (this.getBarcoshundidos() == this.getNbarcos()) {// Final
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
 
					} 
					else if (maquina && !this.getCasilla(i, j).isPuedebarco()) {
						System.out.print(String.format("%-6s", "/"));
					}
					else {
						// if(maquina&&!this.getCasilla(i, j).isPuededisparar()){F
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
