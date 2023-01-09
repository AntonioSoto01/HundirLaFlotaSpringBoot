package Principal;

public class Jugador1 extends Jugador {
	private Casilla ultTocado = null;
	private int estado = 1;

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public Casilla getUllTocado() {
		return ultTocado;
	}

	public void setUllTocado(Casilla ultidisparo) {
		this.ultTocado = ultidisparo;
	}

	public Casilla casillaDisparada() {
		int x = 0;
		int y = 0;
		Casilla casillaDisparada = null;
		if (getUllTocado() == null) {

			do {
				x = (int) (Math.random() * (this.getX()));
				y = (int) (Math.random() * this.getY());
			} while (this.getCasilla(x, y).isDisparado() || !Comprobaralrededor(x, y));

		} else {

			boolean valido = true;

			do {
				valido = true;
				x = getUllTocado().getX();
				y = getUllTocado().getY();

				switch (getEstado()) {
				case 1:
					x++;
					break;
				case 2:
					x--;
					break;
				case 3:
					y++;
					break;
				case 4:
					y--;
					break;
				}
				if (y >= this.getY() || y < 0 || x < 0 || x >= this.getX() || this.getCasilla(x, y).isDisparado() || !Comprobaralrededor(x, y)) {
					actuUllTocado(getEstado());
					setEstado(getEstado() + 1);
					valido = false;
				}
			} while (!valido);
		}
		casillaDisparada = getCasilla(x, y);
//		try {
//			System.out.println(espacios() + "UltDisparo" + getCasilla(getUllTocado().getX(), getUllTocado().getY()).toString());
//		} catch (java.lang.NullPointerException e) {
//		}
		System.out.println(espacios() + "Dispara a " + getCasilla(x, y).toString());
		return casillaDisparada;
	}

	public boolean Comprobaralrededor(int x, int y) {
		int i = x - 1;
		int j = y - 1;
		boolean valido = true;
		while (i <= x + 1 && valido) {
			j = y - 1;
			while (j <= y + 1 && valido) {

				try {

					if (((this.getCasilla(i, j).getBarco() != null && this.getCasilla(i, j) != this.getUllTocado()) && this.getCasilla(i, j).isDisparado())) {
						valido = false;
						// System.out.print(espacios()+getCasilla(x,y).toString());
						// System.out.println(espacios()+getCasilla(i,j).toString());
					}

				} catch (java.lang.ArrayIndexOutOfBoundsException e) {
				}

				j++;
			}
			i++;
		}
		return valido;

	}

	public void actuUllTocado(int estado) {
		int tocado = getUllTocado().getBarco().getTocado();
		switch (estado) {
		case 1:
			setUllTocado(getCasilla(getUllTocado().getX() - tocado + 1, getUllTocado().getY()));
			break;
//		case 2:
//			setUllTocado(getCasilla(getUllTocado().getX() + tocado - 1, getUllTocado().getY()));
//			break;
		case 3:
			setUllTocado(getCasilla(getUllTocado().getX(), getUllTocado().getY() - tocado + 1));
			break;
//		case 4:
//			setUllTocado(getCasilla(getUllTocado().getX(), getUllTocado().getY() + tocado - 1));
//			break;
		default:
			break;
		}

	}

	public void IATocado(Casilla casillaDisparada) {
		setUllTocado(casillaDisparada);
	}

	public void IAHundido() {
		setUllTocado(null);
		setEstado(1);
	}

	public void IAgua() {
		if (getUllTocado() != null) {
			actuUllTocado(getEstado());
			setEstado(getEstado() + 1);
		}
	}

	public String espacios() {
		return String.format("%-130s", "");
	};
}
