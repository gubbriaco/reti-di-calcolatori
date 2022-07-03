package asta;

import java.io.Serializable;

public class Offerta implements Serializable {

	private static final long serialVersionUID = -1253275645410898077L;
	
	private Prodotto p;
	private Client c;
	private int cifra;
	
	public Offerta(Prodotto p, Client c, int cifra) {
		this.p = p;
		this.c = c;
		this.cifra = cifra;
	}

	public Prodotto getProdotto() {
		return p;
	}

	public void setProdotto(Prodotto p) {
		this.p = p;
	}

	public Client getClient() {
		return c;
	}

	public void setClient(Client c) {
		this.c = c;
	}

	public int getCifra() {
		return cifra;
	}

	public void setCifra(int cifra) {
		this.cifra = cifra;
	}
	
	@Override public String toString() {
		return p.toString() + "," + c.toString() + "," + cifra;
	}
	
	

}
