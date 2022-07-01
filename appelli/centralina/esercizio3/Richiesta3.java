package centralina.esercizio3;

import java.io.Serializable;

public class Richiesta3 implements Serializable {

	private static final long serialVersionUID = 8666958903853000675L;

	
	private int id;
	
	public Richiesta3(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	@Override public String toString() {
		return id + "";
	}
	
}
