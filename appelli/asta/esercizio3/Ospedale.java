package asta.esercizio3;

import java.io.Serializable;

public class Ospedale implements Serializable {

	private int nrPazientiRicoverati, nrPostiLetto;
	private String codice, citta;
	
	public Ospedale(String codice, String citta, int nrPazientiRicoverati, int nrPostiLetto) {
		this.codice = codice;
		this.citta = citta;
		this.nrPazientiRicoverati = nrPazientiRicoverati;
		this.nrPostiLetto = nrPostiLetto;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public int getNrPazientiRicoverati() {
		return nrPazientiRicoverati;
	}

	public void setNrPazientiRicoverati(int nrPazientiRicoverati) {
		this.nrPazientiRicoverati = nrPazientiRicoverati;
	}

	public int getNrPostiLetto() {
		return nrPostiLetto;
	}

	public void setNrPostiLetto(int nrPostiLetto) {
		this.nrPostiLetto = nrPostiLetto;
	}

	public String getCitta() {
		return citta;
	}

	public void setCitta(String citta) {
		this.citta = citta;
	}
	
	@Override public String toString() {
		return codice + "-" + citta + "-" + nrPazientiRicoverati + "-" + nrPostiLetto;
	}
	
	@Override public int hashCode() {
		return this.toString().hashCode();
	}
	
	@Override public boolean equals(Object o) {
		if(!(o instanceof Ospedale))
			return false;
		Ospedale os = (Ospedale)o;
		return this.codice.equals(os.codice) && this.citta.equals(os.citta) && 
			   this.nrPazientiRicoverati == os.nrPazientiRicoverati &&
			   this.nrPostiLetto == os.nrPostiLetto;
	}
	
}
