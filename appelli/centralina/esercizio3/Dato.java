package centralina.esercizio3;

import java.io.Serializable;

public class Dato implements Serializable {

	private static final long serialVersionUID = -2272206213759865561L;
	
	private String tipologia;
	private double misura, valore_medio;
	private long time_stamp;
	private static Tipologia d;
	
	public enum Tipologia {d1, d2}
	
	public Dato(String tipologia, double misura, long time_stamp) {
		this.tipologia = tipologia;
		this.misura = misura;
		this.time_stamp = time_stamp;
		d = Tipologia.d1;
	}
	
	public Dato(String tipologia, double valore_medio) {
		this.tipologia = tipologia;
		this.valore_medio = valore_medio;
		d = Tipologia.d2;
	}
	
	
	@Override public String toString() {
		return tipologia + (d==Tipologia.d1?misura:valore_medio) + (d==Tipologia.d1?time_stamp:"");
	}

}
