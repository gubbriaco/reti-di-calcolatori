package Giugno14_2022;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Azienda extends Thread {
	
	private final String host_name_server = "job.unical.it";
	private final int TCP_PORT_INVIO = 3000, TCP_PORT_CANDIDATURA = 5000;
	
	private int piva;
	private String address;
	
	public Azienda(int piva, String address) {
		this.piva = piva;
		this.address = address;
	}
	
	
	@Override public void run() {

		try {
			
			Socket socket_offerta = new Socket(host_name_server, TCP_PORT_INVIO);
			
			ObjectOutputStream oos = new ObjectOutputStream(socket_offerta.getOutputStream());
			Offerta offerta = new Offerta("settore", "ruolo", "tipo", "ral", false, address);
			oos.writeObject(offerta);
			oos.flush();
			
			ObjectInputStream ois = new ObjectInputStream(socket_offerta.getInputStream());
			int id = (int)ois.readObject();

			
			ServerSocket server_candidatura = new ServerSocket(TCP_PORT_CANDIDATURA);
			Socket socket = server_candidatura.accept();
			ois = new ObjectInputStream(socket.getInputStream());
			Candidatura candidatura = (Candidatura)ois.readObject();
			
		}catch(IOException |ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String...strings) {
		
		final int N = 4;
		
		for(int i=0;i<N;++i) {
			
			Random random = new Random();
			int piva = random.nextInt();
			String address = "azienda" + piva + ".unical.it";
			new Azienda(piva, address).start();
			
		}
		
	}

}
