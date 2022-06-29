package lotteria;

import java.util.Random;

public class Biglietto {
	
	private String nomeLotteria;
	
	private Random random;
	public final static int MIN_CODICE = 1, MAX_CODICE = 10000;
	
	private int codice;
	
	public Biglietto(String nomeLotteria) {
		this.nomeLotteria = nomeLotteria;
		
		random = new Random();
		this.codice = random.nextInt(MIN_CODICE, MAX_CODICE+1);
	}
	
	public String getNomeLotteria() {
		return nomeLotteria;
	}
	
	public int getCodice() {
		return codice;
	}
	
	public void setCodice(int codice) {
		this.codice = codice;
	}
	
	@Override public boolean equals(Object o) {
		if(!(o instanceof Biglietto))
			return false;
		Biglietto b = (Biglietto)o;
		
		return this.codice==b.codice && this.nomeLotteria==b.nomeLotteria;
	}
	
	@Override public String toString() {
		return codice + "," + nomeLotteria; 
	}

}
