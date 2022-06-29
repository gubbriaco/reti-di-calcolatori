package lotteria.esercizio4;

import java.io.Serializable;

public class Richiesta implements Serializable {

	private static final long serialVersionUID = -6759200447302126755L;
	
	private String nomeLotteria;
	private int nr_biglietti;
	
	public Richiesta(String nomeLotteria, int nr_biglietti) {
		this.nomeLotteria = nomeLotteria;
		this.nr_biglietti = nr_biglietti;
	}
	
	public String getNomeLotteria() {
		return nomeLotteria;
	}
	
	public int getNr_biglietti() {
		return nr_biglietti;
	}
	
	public void setNomeLotteria(String nomeLotteria) {
		this.nomeLotteria = nomeLotteria;
	}
	
	public void setNrBiglietti(int nr_biglietti) {
		this.nr_biglietti = nr_biglietti;
	}
	
	@Override public String toString() {
		return nomeLotteria + "," + nr_biglietti;
	}

}
