package centralina.esercizio3;

import java.io.Serializable;

public class Richiesta1 implements Serializable {

	private static final long serialVersionUID = -1305750276005969033L;

	private int id;
	private String tipo_grandezza;
	
	public Richiesta1(int id, String tipo_grandezza) {
		this.id = id;
		this.tipo_grandezza = tipo_grandezza;
	}
	
	@Override public String toString() {
		return id + "," + tipo_grandezza;
	}
	
	
}
