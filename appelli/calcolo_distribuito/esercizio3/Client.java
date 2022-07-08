package calcolo_distribuito.esercizio3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
	
	private String address;
	
	private final static int TCP_PORT_RICHIESTA = 2000;
	
	public Client(String address) {
		this.address = address;
	}
	
	
	@SuppressWarnings("resource")
	public static void main(String...strings) {

		String address = "localhost";
		Client client = new Client(address);
		
		try {
			
			Socket socket = new Socket(client.address, Client.TCP_PORT_RICHIESTA);
			System.out.println(socket.toString());
			
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			String tipo = "HARDWARE", descrizione = "descrizione";
			RichiestaRisorsa rr = new RichiestaRisorsa(tipo, descrizione);
			oos.writeObject(rr);
			oos.flush();
			
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			String address_elaboratore = (String)ois.readObject();
			
			System.out.println(address_elaboratore);
			
		}catch(IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}

}
