package lotteria.esercizio4;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class RispostaHandler extends Thread {
	
	private List<Biglietto> biglietti_richiesti;
	private RichiestaHandler rh;
	
	public RispostaHandler(List<Biglietto> biglietti_richiesti, RichiestaHandler rh) {
		this.biglietti_richiesti = biglietti_richiesti;
		this.rh = rh;
	}
	
	public List<Biglietto> getBiglietti() {
		return biglietti_richiesti;
	}
	
	public RichiestaHandler getRh() {
		return rh;
	}
	
	private Risposta risposta;
	private List<Biglietto> biglietti_non_trovati;
	
	@Override public void run() {
		
		synchronized (biglietti_richiesti) {

			boolean biglietti_disponibili = verificaDisponibilitaBiglietti(biglietti_richiesti);

			if (biglietti_disponibili) {
				risposta = new Risposta(biglietti_richiesti);
				rh.storeRisposta(risposta);
			} 
			else {
				biglietti_non_trovati = Collections.synchronizedList(new LinkedList<>());

				for (int i = 0; i < biglietti_richiesti.size(); ++i) {
					String nome_lotteria = biglietti_richiesti.get(i).getNomeLotteria();
					Biglietto biglietto_non_trovato = new Biglietto(nome_lotteria);
					int codice_biglietto_non_trovato = 0;
					biglietto_non_trovato.setCodice(codice_biglietto_non_trovato);
					biglietti_non_trovati.add(biglietto_non_trovato);
				}

				risposta = new Risposta(biglietti_non_trovati);
				rh.storeRisposta(risposta);
			}

		}
		
	}
	
	private boolean verificaDisponibilitaBiglietti(List<Biglietto> biglietti) {
		boolean disponibili = false;
		int disponibilita = 0;
		
		List<Biglietto> bigliettiDisponibili = rh.getBiglietti();
		
		for(int i=0;i<biglietti.size();++i) {
			Biglietto richiesto = biglietti.get(i);
			for(int j=0;j<bigliettiDisponibili.size();++j) {
				Biglietto disponibile = bigliettiDisponibili.get(i);
				if(richiesto.equals(disponibile)) {
					disponibilita++;
				}
			}
		}
		
		if(disponibilita==biglietti.size())
			disponibili = true;
		
		return disponibili;
	}

}
