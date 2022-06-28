package porto;

import java.io.Serializable;

public class Richiesta implements Serializable {

	private static final long serialVersionUID = 8825577699524735538L;
	
	private int id_nave, lunghezza_nave, nr_container_da_scaricare;
	
	public Richiesta(int id_nave, int lunghezza_nave, int nr_container_da_scaricare) {
		this.id_nave = id_nave;
		this.lunghezza_nave = lunghezza_nave;
		this.nr_container_da_scaricare = nr_container_da_scaricare;
	}

	public int getId_nave() {
		return id_nave;
	}

	public void setId_nave(int id_nave) {
		this.id_nave = id_nave;
	}

	public int getLunghezza_nave() {
		return lunghezza_nave;
	}

	public void setLunghezza_nave(int lunghezza_nave) {
		this.lunghezza_nave = lunghezza_nave;
	}

	public int getNr_container_da_scaricare() {
		return nr_container_da_scaricare;
	}

	public void setNr_container_da_scaricare(int nr_container_da_scaricare) {
		this.nr_container_da_scaricare = nr_container_da_scaricare;
	}
	
	
	@Override public String toString() {
		return id_nave + "," + lunghezza_nave + "," + nr_container_da_scaricare;
	}

}
