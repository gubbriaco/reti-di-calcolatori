package calcolo_distribuito;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class OffertaHandler extends Thread {


	private Socket socket;
	private Gestore gestore;
	
	public OffertaHandler(Socket socket, Gestore gestore) {
		this.socket = socket;
		this.gestore = gestore;
	}
	
	@Override public void run() {
		try {
			
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			OffertaRisorsa offerta_risorsa = (OffertaRisorsa)ois.readObject();
			String address_elaboratore = socket.getInetAddress().toString();
			gestore.aggiungiOfferta(offerta_risorsa, address_elaboratore);
			
		}catch(IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
}
