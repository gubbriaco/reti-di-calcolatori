package asta.esercizio4;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RispostaHandler extends Thread {

	private String msg;
	private Socket socket;
	
	public RispostaHandler(String msg, Socket socket) {
		this.msg = msg;
		this.socket = socket;
	}
	
	@Override public void run() {
		try {
			
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(msg);
			oos.flush();
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
}
