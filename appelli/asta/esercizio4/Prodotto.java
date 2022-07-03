package asta.esercizio4;

import java.io.Serializable;

public class Prodotto implements Serializable {

	private static final long serialVersionUID = 1717545571368335473L;
	
	private int codice;
	private long time;
	private double prezzo_minimo;
	
	public Prodotto(int codice, long time, double prezzo_minimo) {
		this.codice = codice;
		this.time = time;
		this.prezzo_minimo = prezzo_minimo;
	}

	public int getCodice() {
		return codice;
	}

	public void setCodice(int codice) {
		this.codice = codice;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public double getPrezzo_minimo() {
		return prezzo_minimo;
	}

	public void setPrezzo_minimo(double prezzo_minimo) {
		this.prezzo_minimo = prezzo_minimo;
	}

	@Override public String toString() {
		return codice + "," + time + "," + prezzo_minimo;
	}
	
}
