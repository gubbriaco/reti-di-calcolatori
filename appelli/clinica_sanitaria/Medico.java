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
	
	private LinkedList<Prenotazione> pazienti;
	private LinkedList<Prenotazione> pazientiInAttesa;
	
	public Medico(int matricola) {
		this.matricola = matricola;
		pazienti = new LinkedList<>();
		pazientiInAttesa = new LinkedList<>();
	}
	
	public int getMatricola() {
		return matricola;
	}
	
	public boolean pazienteInAttesa() {
		return pazienteInAttesa;
	}
	
	public void aggiungiPaziente(Prenotazione paziente) {
		if(possoAccettareAltriPazienti()) {
			pazienti.add(paziente);
			pazienteInAttesa = false;
		}
		else
			return;
	}
	
	public void rimuoviPaziente(Prenotazione paziente) {
		pazienti.remove(paziente);
		pazienteInAttesa = false;
	}
	
	public void aggiungiPazienteInAttesa(Prenotazione paziente) {
		pazientiInAttesa.add(paziente);
		pazienteInAttesa = true;
	}
	
	public void rimuoviPazienteInAttesa(Prenotazione paziente) {
		pazientiInAttesa.remove(paziente);
		pazienteInAttesa = true;
	}
	
	public LinkedList<Prenotazione> getPazienti() {
		return pazienti;
	}
	
	public LinkedList<Prenotazione> getPazientiInAttesa() {
		return pazientiInAttesa;
	}
	
	public boolean possoAccettareAltriPazienti() {
		return pazienti.size() < MAX_PAZIENTI;
	}

}
