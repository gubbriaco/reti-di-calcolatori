package porto;

import java.io.Serializable;

public class Risposta implements Serializable {

	private static final long serialVersionUID = 4731985386092825212L;

	private int id_nave, lunghezza_nave, nr_container_da_scaricare, nr_banchina;
	
	public Risposta(int id_nave, int lunghezza_nave, int nr_container_da_scaricare, int nr_banchina) {
		this.id_nave = id_nave;
		this.lunghezza_nave = lunghezza_nave;
		this.nr_container_da_scaricare = nr_container_da_scaricare;
	}

	public int getIdNave() {
		return id_nave;
	}

	public void setIdNave(int id_nave) {
		this.id_nave = id_nave;
	}

	public int getLunghezzaNave() {
		return lunghezza_nave;
	}

	public void setLunghezzaNave(int lunghezza_nave) {
		this.lunghezza_nave = lunghezza_nave;
	}

	public int getNrContainerDaScaricare() {
		return nr_container_da_scaricare;
	}

	public void setNrContainerDaScaricare(int nr_container_da_scaricare) {
		this.nr_container_da_scaricare = nr_container_da_scaricare;
	}
	
	public int getNrBanchina() {
		return nr_banchina;
	}
	
	public void setNrBanchina(int nr_banchina) {
		this.nr_banchina = nr_banchina;
	}
	
	
}
