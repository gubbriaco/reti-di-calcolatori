package clinica_sanitaria.esercizio4;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class ProdottiService {
	
	private List<Magazzino> magazzini;
	
	public ProdottiService() {
		magazzini = new LinkedList<>();
	}
	
	/**
	 * Dato il nome del produttore restituisce il Prodotto piu' venduto 
	 * di quel produttore tra i diversi magazzini.
	 * @param nomeProduttore
	 * @return
	 */
	public Prodotto prodottoPiuVenduto(String nomeProduttore) {
		
		Prodotto piuVenduto = null;
		
		Map<Prodotto, Integer> prodottiVendutiProduttore = new HashMap<>();
		
		for(int i=0;i<magazzini.size();++i) {
			ListaProdotti listaProdotti = magazzini.get(i).getListaProdotti();
			List<Prodotto> prodotti = listaProdotti.getProdotti();
			for(int j=0;j<prodotti.size();++j) {
				Prodotto prodotto = prodotti.get(j);
			
				if(prodotto.getProduttore().equals(nomeProduttore)) {

					boolean trovato = false;
					for(Entry<Prodotto, Integer> entry: prodottiVendutiProduttore.entrySet())
						if(entry.getKey().equals(prodotto)) {
							int nr_vendite = entry.getValue();
							entry.setValue(nr_vendite+1);
							trovato = true;
						}
					if(!trovato)
						prodottiVendutiProduttore.put(prodotto, 1);	
				}
			}
		}
		
		class PiuVendutiComparator implements Comparator<Prodotto> {
			private Map<Prodotto, Integer> venduti;
			public PiuVendutiComparator(Map<Prodotto, Integer> venduti) {
				this.venduti = venduti;
			}
			@Override public int compare(Prodotto nrv1, Prodotto nrv2) {
				if(venduti.get(nrv1) > venduti.get(nrv2))
					return 1;
				if(venduti.get(nrv1) < venduti.get(nrv2))
					return -1;
				return 0;
			}
		}
		
		PiuVendutiComparator pvc = new PiuVendutiComparator(prodottiVendutiProduttore);
		Map<Prodotto, Integer> ordinatiDecrescenti = new TreeMap<>(pvc);
	
		piuVenduto = ordinatiDecrescenti.entrySet().iterator().next().getKey();
		
		return piuVenduto;
	}
	
	
	/**
	 * Dato un id magazzino restituisce una lista contenente i 3 prodotti che hanno
	 * prodotto piu' incassi per quel magazzino.
	 * @param id_magazzino
	 * @return
	 */
	public List<Prodotto> prodottiMaxIncasso(String id_magazzino) {
		LinkedList<Prodotto> treProdottiPiuIncassi = new LinkedList<>();
		List<Prodotto> prodottiMagazzino = new LinkedList<>();
		
		for(int i=0;i<magazzini.size();++i) 
			if(magazzini.get(i).getId() == Integer.valueOf(id_magazzino))
				prodottiMagazzino = magazzini.get(i).getListaProdotti().getProdotti();
		
		class ProdottoComparator implements Comparator<Prodotto> {
			@Override public int compare(Prodotto p1, Prodotto p2) {
				if(p1.getPrezzo() > p2.getPrezzo())
					return 1;
				if(p1.getPrezzo() < p2.getPrezzo())
					return -1;
				return 0;
			}
		}
		
		prodottiMagazzino.sort(new ProdottoComparator());
		
		if(prodottiMagazzino.size()<3)
			return prodottiMagazzino;
		
		for(int i=0;i<3;++i)
			treProdottiPiuIncassi.addLast(prodottiMagazzino.get(i));
		
		return treProdottiPiuIncassi;
	}

}
