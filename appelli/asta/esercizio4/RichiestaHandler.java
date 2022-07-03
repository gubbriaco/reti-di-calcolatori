package asta.esercizio4;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;

public class RichiestaHandler extends Thread {
	
	private Socket socket;
	private List<Prodotto> prodotti;
	private List<Offerta> offerte;
	
	public RichiestaHandler(Socket socket, List<Prodotto> prodotti, List<Offerta> offerte) {
		this.socket = socket;
		this.prodotti = prodotti;
		this.offerte = offerte;
	}
	
	@Override public void run() {
		try {
			
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			Offerta offerta = (Offerta)ois.readObject();
			
			boolean offertaOK = verificaOfferta(offerta, prodotti);
			if(offertaOK) {

				offerte.add(offerta);
				String offerta_accettata = "OK";
				RispostaHandler rh = new RispostaHandler(offerta_accettata, socket);
				rh.start();
				
			}else {
				
				String offerta_rifiutata = "TROPPO BASSA";
				RispostaHandler rh = new RispostaHandler(offerta_rifiutata, socket);
				rh.start();
				
			}
			
		}catch(IOException | ClassNotFoundException e) {
			
		}
	}
	
	private static boolean verificaOfferta(Offerta offerta, List<Prodotto> prodotti) {
		
		for(int i=0;i<prodotti.size();++i) {
			if(prodotti.get(i).equals(offerta.getProdotto()))
				if(offerta.getCifra() >= prodotti.get(i).getPrezzo_minimo())
					return true;
		}
		
		return false;
		
	}

}
