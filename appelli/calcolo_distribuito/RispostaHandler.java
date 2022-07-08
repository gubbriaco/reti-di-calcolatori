package calcolo_distribuito;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RispostaHandler extends Thread {
	
	private Socket socket;
	private String address_elaboratore;

	public RispostaHandler(Socket socket, String address_elaboratore) {
		this.socket = socket;
		this.address_elaboratore = address_elaboratore;
	}
	
	
	@Override public void run() {
		try {
			
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(address_elaboratore);
			oos.flush();
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
}
