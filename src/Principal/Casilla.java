package Principal;

public class Casilla {
	private boolean disparado;
	private Barco barco;
	private int x;
	private int y;


	@Override
	public String toString() {
		return ((char) ('A' + y) + "" + (x + 1));
	}
	public Casilla(int x, int y) {
		super();
		this.x = x;
		this.y = y;
		this.disparado = false;
		this.barco = null;
	}
	public int getX() {
		return x;      
	}

	public int getY() {
		return y;
	}
	
	public boolean isDisparado() {
		return disparado;
	}

	public void setDisparado(boolean disparado) {
		this.disparado = disparado;
	}

	public Barco getBarco() {
		return barco;
	}

	public void setBarco(Barco barco) {
		this.barco = barco;
	}
}
