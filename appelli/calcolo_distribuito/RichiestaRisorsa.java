package calcolo_distribuito;

import java.io.Serializable;
import java.util.Objects;

public class RichiestaRisorsa implements Serializable {

	private static final long serialVersionUID = 247505782302928728L;
	
	
	private String tipo, descrizione;
	
	public RichiestaRisorsa(String tipo, String descrizione) {
		this.tipo = tipo;
		this.descrizione = descrizione;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	@Override
	public int hashCode() {
		return Objects.hash(descrizione, tipo);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if(!(o instanceof RichiestaRisorsa))
			return false;
		if (this == o)
			return true;
		RichiestaRisorsa r = (RichiestaRisorsa)o;
		return this.descrizione.equals(r.descrizione) &&
			   this.tipo.equals(r.tipo);
	}

	@Override
	public String toString() {
		return tipo + "," + descrizione;
	}
	
	

}
