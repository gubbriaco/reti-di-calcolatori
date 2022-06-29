package clinica_sanitaria.esercizio4;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class ListaProdotti implements Serializable {
	
	private List<Prodotto> prodotti;
	
	
	public ListaProdotti() {
		prodotti = new LinkedList<>();
	}
	
	public ListaProdotti(List<Prodotto> prodotti) {
		this.prodotti = prodotti;
	}
	
	
	public void aggiungiProdotto(Prodotto prodotto) {
		prodotti.add(prodotto);
	}
	
	public void rimuoviProdotto(Prodotto prodotto) {
		prodotti.remove(prodotto);
	}
	
	public List<Prodotto> getProdotti() {
		return prodotti;
	}
	
	@Override public int hashCode() {
		int hashCode = 0;
		
		for(int i=0;i<prodotti.size();++i)
			hashCode += prodotti.get(i).hashCode();
		
		return hashCode;
	}
	
	@Override public boolean equals(Object o) {
		if(!(o instanceof ListaProdotti))
			return false;
		ListaProdotti lp = (ListaProdotti)o;
		if(this.prodotti.size() != lp.prodotti.size())
			return false;
		for(int i=0;i<this.prodotti.size();++i)
			if(!this.prodotti.get(i).equals(lp.prodotti.get(i)))
				return false;
		return true;
	}
	
	@Override public String toString() {
		return prodotti.toString();
	}
	

}
