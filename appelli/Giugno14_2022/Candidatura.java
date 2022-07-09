package Giugno14_2022;

import java.io.Serializable;
import java.util.Objects;

public class Candidatura implements Serializable {

	private static final long serialVersionUID = 8903154757332401350L;

	
	private int id;
	private String cv;
	
	public Candidatura(int id, String cv) {
		this.id = id;
		this.cv = cv;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCv() {
		return cv;
	}

	public void setCv(String cv) {
		this.cv = cv;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cv, id);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Candidatura) || o == null)
			return false;
		Candidatura c = (Candidatura) o;
		return Objects.equals(cv, c.cv) && id == c.id;
	}

	@Override
	public String toString() {
		return id + "," + cv;
	}
	
	
	
}
