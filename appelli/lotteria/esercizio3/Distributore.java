package lotteria.esercizio3;

import java.io.Serializable;

public class Distributore implements Serializable {
	
	private String partitaIva, regioneSociale, regione;
	private double prezzoDiesel, prezzoBenzina;
	
	public Distributore(String partitaIva, String regioneSociale, String regione, double prezzoDiesel, double prezzoBenzina) {
		this.partitaIva = partitaIva;
		this.regioneSociale = regioneSociale;
		this.regione = regione;
		this.prezzoDiesel = prezzoDiesel;
		this.prezzoBenzina = prezzoBenzina;
	}

	public String getPartitaIva() {
		return partitaIva;
	}

	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}

	public String getRegioneSociale() {
		return regioneSociale;
	}

	public void setRegioneSociale(String regioneSociale) {
		this.regioneSociale = regioneSociale;
	}

	public String getRegione() {
		return regione;
	}

	public void setRegione(String regione) {
		this.regione = regione;
	}

	public double getPrezzoDiesel() {
		return prezzoDiesel;
	}

	public void setPrezzoDiesel(double prezzoDiesel) {
		this.prezzoDiesel = prezzoDiesel;
	}

	public double getPrezzoBenzina() {
		return prezzoBenzina;
	}

	public void setPrezzoBenzina(double prezzoBenzina) {
		this.prezzoBenzina = prezzoBenzina;
	}
	
	@Override public int hashCode() {
		return (partitaIva + "-" + regioneSociale + "-" + regione + "-" + prezzoDiesel + "-" + prezzoBenzina).hashCode();
	}
	
	@Override public boolean equals(Object o) {
		if(!(o instanceof Distributore))
			return false;
		Distributore d = (Distributore)o;
		return this.partitaIva.equals(d.partitaIva) && this.regioneSociale.equals(d.regioneSociale) &&
			   this.regione.equals(d.regione) && this.prezzoDiesel==d.prezzoDiesel && 
			   this.prezzoBenzina== d.prezzoBenzina;
	}
	
	@Override public String toString() {
		return partitaIva + "-" + regioneSociale + "-" + regione + "-" + prezzoDiesel + "-" + prezzoBenzina;
	}

}
