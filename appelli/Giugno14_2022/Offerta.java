package Giugno14_2022;

import java.io.Serializable;
import java.util.Objects;

public class Offerta implements Serializable {
	
	private static final long serialVersionUID = 2986095284462431657L;

	private boolean attiva;
	
	private String address;
	
	private String settore, ruolo, tipo, ral;
	
	private int id_numerico;
	
	public Offerta(String settore, String ruolo, String tipo, String ral, boolean attiva, String address) {
		this.settore = settore;
		this.ruolo = ruolo;
		this.tipo = tipo;
		this.ral = ral;
		this.attiva = attiva;
		id_numerico = -1;
		this.address = address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setIdNumerico(int id_numerico) {
		this.id_numerico = id_numerico;
	}
	
	public int getIdNumerico() {
		return id_numerico;
	}
	
	public boolean isActive() {
		return attiva;
	}
	
	public void setActive(boolean attiva) {
		this.attiva = attiva;
	}

	public String getSettore() {
		return settore;
	}

	public void setSettore(String settore) {
		this.settore = settore;
	}

	public String getRuolo() {
		return ruolo;
	}

	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getRal() {
		return ral;
	}

	public void setRal(String ral) {
		this.ral = ral;
	}

	@Override
	public int hashCode() {
		return Objects.hash(ral, ruolo, settore, tipo);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Offerta) || o == null)
			return false;
		if (this == o)
			return true;
		if (getClass() != o.getClass())
			return false;
		Offerta off = (Offerta) o;
		return Objects.equals(ral, off.ral) && Objects.equals(ruolo, off.ruolo)
				&& Objects.equals(settore, off.settore) && Objects.equals(tipo, off.tipo);
	}

	@Override
	public String toString() {
		return id_numerico + "," + settore + "," + ruolo + "," + tipo + "," + ral;
	}
	
	

}
