package asta.esercizio3;

import java.util.LinkedList;

public class OspedaliService {
	
	private LinkedList<Ospedale> ospedali;
	
	public OspedaliService() {
		ospedali = new LinkedList<>();
	}
	
	public OspedaliService(LinkedList<Ospedale> ospedali) {
		this.ospedali = ospedali;
	}
	
	/**
	 * Dato il nome della citta, restituisce l'ospedale con il
	 * maggior numero di posti letto liberi.
	 * @param citta
	 * @return
	 */
	public Ospedale maxOspedale(String citta) {
		
		Ospedale ospedaleMax = ospedali.getFirst();
		int max = ospedali.getFirst().getNrPostiLetto();
		
		for(int i=0;i<ospedali.size();++i) {
			Ospedale current = ospedali.get(i);
			if(current.getNrPostiLetto() > max) {
				ospedaleMax = current;
				max = current.getNrPostiLetto();
			}
			
		}
		
		return ospedaleMax;
	}
	
	
	/**
	 * Restituisce il nome della citta che, considerando tutti
	 * gli ospedali, presenta il maggior tasso di occupazione
	 * dei posti letto (calcolato come il rapporto tra pazienti
	 * ricoverati e posti letto disponibili).
	 * @return
	 */
	public String cittaRatio() {
		
		Ospedale ospedaleTassoMax = ospedali.getFirst();
		int tassoMax = ospedali.getFirst().getNrPazientiRicoverati() / ospedali.getFirst().getNrPostiLetto();
		
		for(int i=0;i<ospedali.size();++i) {
			Ospedale current = ospedali.get(i);
			int tasso = current.getNrPazientiRicoverati() / current.getNrPostiLetto();
			if(tasso > tassoMax) {
				ospedaleTassoMax = current;
				tassoMax = tasso;
			}
		}
		
		return ospedaleTassoMax.getCitta();
		
	}
	

}
