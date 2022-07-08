package calcolo_distribuito.esercizio4;

import java.io.Serializable;
import java.util.Objects;

public class Data implements Serializable {

	private int giorno, mese, anno;
	
	public Data(int giorno, int mese, int anno) {
		this.giorno = giorno;
		this.mese = mese;
		this.anno = anno;
	}

	public int getGiorno() {
		return giorno;
	}

	public void setGiorno(int giorno) {
		this.giorno = giorno;
	}

	public int getMese() {
		return mese;
	}

	public void setMese(int mese) {
		this.mese = mese;
	}

	public int getAnno() {
		return anno;
	}

	public void setAnno(int anno) {
		this.anno = anno;
	}

	@Override
	public int hashCode() {
		return Objects.hash(anno, giorno, mese);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (this == o)
			return true;
		if (!(o instanceof Data))
			return false;
		Data d = (Data)o;
		return anno == d.anno && giorno == d.giorno && mese == d.mese;
	}

	@Override
	public String toString() {
		return "Data [giorno=" + giorno + ", mese=" + mese + ", anno=" + anno + "]";
	}
	
	
	
}
