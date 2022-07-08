package calcolo_distribuito;

import java.io.Serializable;
import java.util.Objects;

public class OffertaRisorsa implements Serializable {
	
	private static final long serialVersionUID = 4671264648640483990L;


	private String nome, tipo, descrizione;
	
	public OffertaRisorsa(String nome, String tipo, String descrizione) {
		this.nome = nome;
		this.tipo = tipo;
		this.descrizione = descrizione;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
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

	@Override public int hashCode() {
		return Objects.hash(descrizione, nome, tipo);
	}

	@Override public boolean equals(Object o) {
		if (o == null)
			return false;
		if(!(o instanceof OffertaRisorsa))
			return false;
		if (this == o)
			return true;
		OffertaRisorsa r = (OffertaRisorsa)o;
		return this.descrizione.equals(r.descrizione) && 
			   this.nome.equals(r.nome) && 
			   this.tipo.equals(r.tipo);
	}

	@Override public String toString() {
		return "nome" + "," + tipo + "," + descrizione;
	}
	
	
}
