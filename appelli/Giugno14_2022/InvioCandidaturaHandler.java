package Giugno14_2022;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class InvioCandidaturaHandler extends Thread {
	
	private final static int TCP_PORT_INVIO_CANDIDATURA = 5000;
	
	private Offerta offerta;
	private Candidatura candidatura;
	private Server s;
	
	public InvioCandidaturaHandler(Offerta offerta, Candidatura candidatura, Server s) {
		this.offerta = offerta;
		this.candidatura = candidatura;
		this.s = s;
	}
	
	
	@Override public void run() {
		try {
		
			Socket socket = new Socket(offerta.getAddress(), TCP_PORT_INVIO_CANDIDATURA);
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(candidatura);
			oos.flush();
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

}
