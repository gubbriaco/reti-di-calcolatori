package calcolo_distribuito.esercizio4;

import java.io.Serializable;
import java.util.LinkedList;

public class ExamsService {
	
	
	private LinkedList<Esame> esami;
	
	public ExamsService(LinkedList<Esame> esami) {
		this.esami = esami;
	}
	
	public ExamsService() {
		esami = new LinkedList<>();
	}
	
	
	public int votoStudente(String nome_esame, int matricola) {
		
		int voto = -1;
		
		for(Esame esame : esami)
			if(esame.getNome().equals(nome_esame) && esame.getMatricolaStudente()==matricola)
				voto = esame.getVoto();
		
		return voto;
	}
	
	public String EsameGiorno(Data data) {
		String nome_esame = "";
		
		for(Esame esame : esami)
			if(esame.getData().equals(data))
				nome_esame = esame.getNome();
		
		return nome_esame;
	}

	
	private class Esame implements Serializable {
		private String nome;
		private int matricola_studente, voto;
		private Data data;
		
		public Esame(String nome, int matricola_studente, int voto, Data data) {
			this.nome = nome;
			this.matricola_studente = matricola_studente;
			this.voto = voto;
			this.data = data;
		}
		
		public String getNome() {
			return nome;
		}
		
		public int getMatricolaStudente() {
			return matricola_studente;
		}
		
		public int getVoto() {
			return voto;
		}
		
		public Data getData() {
			return data;
		}
		
		@Override public String toString() {
			return nome + "," + matricola_studente + "," + voto + "," + data;
		}
	}
	
}
