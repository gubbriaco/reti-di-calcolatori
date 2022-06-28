package portook;

import java.util.LinkedList;
import java.util.List;

public class Banchina {
	
	private int id;
	private boolean piu40;
	private boolean isBusy;
	private List<Nave> navi;
	
	public Banchina(int id, boolean piu40, boolean isBusy) {
		this.id = id;
		this.piu40 = piu40;
		this.isBusy = isBusy;
		
		navi = new LinkedList<>();
	}
	
	public int getId() {
		return id;
	}
	
	public boolean getPiu40() {
		return piu40;
	}

	public boolean getIsBusy() {
		return isBusy;
	}
	
	public void setIsBusy(boolean isBusy) {
		this.isBusy = isBusy;
	}
	
	public void aggiungiNave(Nave nave) {
		if(isBusy)
			return;
		else {
			navi.add(nave);
			isBusy = true;
		}
	}
	
	public void rimuoviNave(Nave nave) {
		if(!isBusy)
			return;
		else {
			navi.remove(nave);
			isBusy = false;
		}
	}
	
	
}
