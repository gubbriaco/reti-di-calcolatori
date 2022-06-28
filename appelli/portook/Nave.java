package portook;

import java.io.Serializable;

public class Nave implements Serializable {

	private static final long serialVersionUID = 931940802335392007L;
	
	private int id, lunghezza, nr_container_da_scaricare;
	
	public Nave(int id, int lunghezza, int nr_container_da_scaricare) {
		this.id = id;
		this.lunghezza = lunghezza;
		this.nr_container_da_scaricare = nr_container_da_scaricare;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLunghezza() {
		return lunghezza;
	}

	public void setLunghezza(int lunghezza) {
		this.lunghezza = lunghezza;
	}

	public int getNr_container_da_scaricare() {
		return nr_container_da_scaricare;
	}

	public void setNr_container_da_scaricare(int nr_container_da_scaricare) {
		this.nr_container_da_scaricare = nr_container_da_scaricare;
	}
	

}
