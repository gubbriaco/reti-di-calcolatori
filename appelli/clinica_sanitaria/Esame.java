package clinica_sanitaria;

public class Esame {
	
	private int codice;
	
	public Esame(int codice) {
		this.codice = codice;
	}
	
	public int getCodice() {
		return codice;
	}
	
	@Override public String toString() {
		return "Esame " + codice;
	}

}
