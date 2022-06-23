package send_receive_object;

import java.io.Serializable;

public class Studente implements Serializable {

	private static final long serialVersionUID = -5612262958614247973L;
	
	private int matricola;
	private String nome, cognome, corsoDiLaurea;
	
	public Studente(int matricola, String nome, String cognome, String corsoDiLaurea) {
		this.matricola = matricola;
		this.nome = nome;
		this.cognome = cognome;
		this.corsoDiLaurea = corsoDiLaurea;
	}
	
	public int getMatricola() {
		return matricola;
	}
	
	public String getNome() {
		return nome;
	}
	
	public String getCognome() {
		return cognome;
	}
	
	public String getCorsoDiLaurea() {
		return corsoDiLaurea;
	}
	
	@Override public String toString() {
		return nome + " " + cognome + " MATRICOLA: " + matricola + " FACOLTA': " + corsoDiLaurea;
	}
	
}
