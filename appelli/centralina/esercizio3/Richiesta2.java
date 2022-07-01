package centralina.esercizio3;

import java.io.Serializable;

public class Richiesta2 implements Serializable {

	private static final long serialVersionUID = 170090812305456371L;
	
	
	private int id;
	private long from, to;
	
	public Richiesta2(int id, long from, long to) {
		this.id = id;
		this.from = from;
		this.to = to;
	}
	
	@Override public String toString() {
		return id + "," + from + "," + to;
	}

}
