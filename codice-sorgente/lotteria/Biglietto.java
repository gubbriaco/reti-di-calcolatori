package lotteria;
import java.io.*;
import java.net.*;
import java.util.*;
public class Biglietto implements Serializable{
	private static final long serialVersionUID = 7403949238101107806L;
	private String nomeLotteria;
	private int numBiglietto;
	
	public Biglietto(String nomeLotteria, int numBiglietto) {
		this.nomeLotteria=nomeLotteria;
		this.numBiglietto=numBiglietto;
	}
	public String getNomeLotteria() {
		return nomeLotteria;
	}
	public int getNumBiglietto() {
		return numBiglietto;
	}
}
