package clinica_sanitaria;

import java.util.LinkedList;

public class Medico {
	
	
	static class Prenotazione {
		
		private int codice_esame, matricola_paziente;
		
		public Prenotazione(int codice_esame, int matricola_paziente) {
			this.codice_esame = codice_esame;
			this.matricola_paziente = matricola_paziente;
		}
		
		public int getCodiceEsame() {
			return codice_esame;
		}
		
		public int getMatricolaPaziente() {
			return matricola_paziente;
		}
		
	}
	
	private int matricola;
	
	private boolean pazienteInAttesa = false;
	
	private static int MAX_PAZIENTI = 10;
	
	private LinkedList<Prenotazione> prenotazioni;
	private LinkedList<Prenotazione> prenotazioniInAttesa;
	
	public Medico(int matricola) {
		this.matricola = matricola;
		prenotazioni = new LinkedList<>();
		prenotazioniInAttesa = new LinkedList<>();
	}
	
	public int getMatricola() {
		return matricola;
	}
	
	public boolean pazienteInAttesa() {
		return pazienteInAttesa;
	}
	
	public void aggiungiPaziente(Prenotazione prenotazione) {
		if(possoAccettareAltriPazienti()) {
			prenotazioni.add(prenotazione);
			pazienteInAttesa = false;
		}
		else
			return;
	}
	
	public void rimuoviPaziente(Prenotazione prenotazione) {
		prenotazioni.remove(prenotazione);
		pazienteInAttesa = false;
	}
	
	public void aggiungiPazienteInAttesa(Prenotazione prenotazione) {
		prenotazioniInAttesa.add(prenotazione);
		pazienteInAttesa = true;
	}
	
	public void rimuoviPazienteInAttesa(Prenotazione prenotazione) {
		prenotazioniInAttesa.remove(prenotazione);
		pazienteInAttesa = true;
	}
	
	public LinkedList<Prenotazione> getPazienti() {
		return prenotazioni;
	}
	
	public LinkedList<Prenotazione> getPazientiInAttesa() {
		return prenotazioniInAttesa;
	}
	
	public boolean possoAccettareAltriPazienti() {
		return prenotazioni.size() < MAX_PAZIENTI;
	}

}
