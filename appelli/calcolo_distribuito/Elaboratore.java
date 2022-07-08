package calcolo_distribuito;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Elaboratore {
	
	private final static int TCP_PORT_OFFERTA = 3000;
	
	@SuppressWarnings("unused")
	private String address;
	
	public Elaboratore(String address) {
		this.address = address;
	}
	
	
	@SuppressWarnings("resource")
	public static void main(String...strings) {
		
		String address = "230.0.0.1";
		Elaboratore elaboratore = new Elaboratore(address);
		
		try {
		
			Socket socket = new Socket(elaboratore.address, Elaboratore.TCP_PORT_OFFERTA);
			
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			String nome = "RISORSA 1", tipo = "HARDWARE", descrizione = "descrizione";
			OffertaRisorsa or = new OffertaRisorsa(nome, tipo, descrizione);
			oos.writeObject(or);
			oos.flush();
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

}
