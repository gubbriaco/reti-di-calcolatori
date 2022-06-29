package lotteria;

public class Lotteria {
	
	private String nome;
	
	public Lotteria(String nome) {
		this.nome = nome;
	}
	
	
	public String getNome() {
		return nome;
	}
	
	
	@Override public String toString() {
		return nome;
	}

}
