package clinica_sanitaria.esercizio4;

public class Magazzino {
	
	private int id;
	private ListaProdotti listaProdotti;
	
	
	public Magazzino(int id) {
		this.id = id;
		listaProdotti = new ListaProdotti();
	}
	
	public Magazzino(int id, ListaProdotti listaProdotti) {
		this.id = id;
		this.listaProdotti = listaProdotti;
	}
	
	public int getId() {
		return id;
	}
	
	public double getIncasso() {
		double incasso = 0;
		
		for(int i=0;i<listaProdotti.getProdotti().size();++i)
			incasso += listaProdotti.getProdotti().get(i).getPrezzo();
		
		return incasso;
	}
	
	public ListaProdotti getListaProdotti() {
		return listaProdotti;
	}
	
	@Override public int hashCode() {
		int hashCode = 0;
		
		for(int i=0;i<listaProdotti.getProdotti().size();++i)
			hashCode += listaProdotti.getProdotti().get(i).hashCode();
		
		hashCode += (this.toString()).hashCode();
		
		return hashCode;	
	}
	
	@Override public boolean equals(Object magazzino) {
		
		if(!(this instanceof Object))
			return false;
		if(this.id != ((Magazzino)magazzino).id)
			return false;
		if(this.getIncasso() != ((Magazzino)magazzino).getIncasso())
			return false;
		return this.listaProdotti.equals(((Magazzino)magazzino).listaProdotti);
	}
	
	@Override public String toString() {
		return "Magazzino " + id;
	}

}
