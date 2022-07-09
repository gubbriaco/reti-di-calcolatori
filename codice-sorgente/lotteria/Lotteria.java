package lotteria;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Lotteria {
	
	private String nomeLotteria;
	private LinkedList<Biglietto> biglietti;
	private boolean statoLotteria;
	
	public Lotteria(String nomeLotteria,LinkedList<Biglietto> biglietti) {
		this.nomeLotteria=nomeLotteria;
		this.biglietti=new LinkedList<Biglietto>(biglietti);
	}
	
	public LinkedList<Biglietto> getBiglietti(){
		return new LinkedList<>(biglietti);
	}
	public String getNome() {
		return nomeLotteria;
	}
	public void setStato(boolean state) {
		statoLotteria=state;
	}
	public boolean isOpen() {
		return statoLotteria;
	}
	
	
}
