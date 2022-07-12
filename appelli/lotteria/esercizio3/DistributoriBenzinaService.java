package lotteria.esercizio3;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class DistributoriBenzinaService {

	private List<Distributore> distributori;

	
	public DistributoriBenzinaService(List<Distributore> distributori) {
		this.distributori = distributori;
	}
	
	
	/**
	 * Dato il nome di una regione restituisce il distributore con il
	 * miglior prezzo della benzina.
	 * @param nome_regione
	 * @return
	 */
	public Distributore minPrezzoBenzina(String nome_regione) {
		
		List<Distributore> distributori_regione = new LinkedList<>();
		
		for(int i=0;i<distributori.size();++i) {
			Distributore distributore = distributori.get(i);
			if(distributore.getRegione().equals(nome_regione))
				distributori_regione.add(distributore);
		}
		
		class ComparatorDistributoriBenzina implements Comparator<Distributore> {
			@Override public int compare(Distributore d1, Distributore d2) {
				if(d1.getPrezzoBenzina() > d2.getPrezzoBenzina())
					return 1;
				if(d1.getPrezzoBenzina() < d2.getPrezzoBenzina())
					return -1;
				return 0;
			}
		}
		
		distributori_regione.sort(new ComparatorDistributoriBenzina());
		
		return ((LinkedList<Distributore>)distributori_regione).getFirst();
		
	}
	
	/**
	 * Restituisce il nome della regione che, considerando tutti i distributori presenti 
	 * sul suo territorio, ha il min prezzo medio del diesel.
	 * @return
	 */
	public String regioneMinMediaDiesel() {
		
		class ComparatorDistributoriDiesel implements Comparator<Distributore> {
			@Override public int compare(Distributore d1, Distributore d2) {
				if(d1.getPrezzoDiesel() > d2.getPrezzoDiesel())
					return 1;
				if(d1.getPrezzoDiesel() < d2.getPrezzoDiesel())
					return -1;
				return 0;
			}
		}
		
		distributori.sort(new ComparatorDistributoriDiesel());
		
		return ((LinkedList<Distributore>)distributori).getFirst().getRegione();
		
	}
	
	
}
