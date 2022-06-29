package lotteria.esercizio4;

import java.io.Serializable;
import java.util.List;

public class Risposta implements Serializable {

	private static final long serialVersionUID = 3807702447818528485L;

	private List<Biglietto> biglietti_giocati;
	
	public Risposta(List<Biglietto> biglietti_giocati) {
		this.biglietti_giocati = biglietti_giocati;
	}
	
	public List<Biglietto> getBigliettiGiocati(){
		return biglietti_giocati;
	}
	
	
	@Override public String toString() {
		String risposta = "";
		
		for(int i=0;i<biglietti_giocati.size();++i)
			risposta += biglietti_giocati.get(i) + "\n";
		
		return risposta;
	}
	
}
