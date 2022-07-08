package calcolo_distribuito.esercizio3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Map.Entry;

public class RichiestaHandler extends Thread {
	
	private Socket socket;
	private Gestore gestore;
	
	public RichiestaHandler(Socket socket, Gestore gestore) {
		this.socket = socket;
		this.gestore = gestore;
	}
	
	@Override public void run() {
		try {
			
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			RichiestaRisorsa rr = (RichiestaRisorsa)ois.readObject();
			
			String address_elaboratore = "";
			for(Entry<OffertaRisorsa, String> risorsa_offerta : gestore.getRisorseOfferte().entrySet())
				if(rr.getTipo().equals(rr.getTipo()) && rr.getDescrizione().equals(rr.getDescrizione())) {
					address_elaboratore = risorsa_offerta.getValue();
					break;
				}
			
			RispostaHandler rh = new RispostaHandler(socket, address_elaboratore);
			rh.start();
			
		}catch(IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	

}
