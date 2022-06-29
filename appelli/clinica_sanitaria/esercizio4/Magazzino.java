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
	
	@Override public boolean equals(Object o) {
		
		if(!(o instanceof Magazzino))
			return false;
		Magazzino m = (Magazzino)o;
		if(this.id != m.id)
			return false;
		if(this.getIncasso() != m.getIncasso())
			return false;
		return this.listaProdotti.equals(m.listaProdotti);
	}
	
	@Override public String toString() {
		return "Magazzino " + id;
	}

}
