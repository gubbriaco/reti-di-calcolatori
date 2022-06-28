package clinica_sanitaria.esercizio4;

import java.io.Serializable;

public class Prodotto implements Serializable {
	
	private String codice, nome, produttore;
	private double prezzo;
	
	public Prodotto(String codice, String nome, String produttore, double prezzo) {
		this.codice = codice;
		this.nome = nome;
		this.produttore = produttore;
		this.prezzo = prezzo;
	}
	
	public String getCodice() {
		return codice;
	}
	
	public String getNome() {
		return nome;
	}
	
	public String getProduttore() {
		return produttore;
	}
	
	public double getPrezzo() {
		return prezzo;
	}
	
	@Override public int hashCode() {
		return (codice + nome + produttore).hashCode() + (prezzo + "").hashCode();
	}
	
	@Override public boolean equals(Object prodotto) {
		if(!(this instanceof Object))
			return false;
		if(!this.codice.equals(((Prodotto)prodotto).codice))
			return false;
		if(!this.nome.equals(((Prodotto)prodotto).nome))
			return false;
		if(this.prezzo != ((Prodotto)prodotto).prezzo)
			return false;
		return true;
	}
	
	@Override public String toString() {
		return "Prodotto [codice=" + codice + ", nome=" + nome + ", produttore=" + produttore +", prezzo=" + prezzo + "]";
	}

}
