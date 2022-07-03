package asta;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.Random;

public class Client extends Thread {
	
	private final static int TCP_PORT_OFFERTE=3000, UDP_PORT_PIUALTA=4000;
	private final static String HOST_NAME="asta.unical.it";
	
	private String client_id;
	
	public Client(String client_id) {
		this.client_id = client_id;
	}
	
	public String getClient_Id() {
		return client_id;
	}
	
	
	
	@SuppressWarnings("resource")
	@Override public void run() {
		try {
			
			Socket socket = new Socket(HOST_NAME, TCP_PORT_OFFERTE);
			System.out.println(socket.toString());
			
			int codice = 81371;
			long time = 500; 
			double prezzo_minimo = 200;
			Prodotto prodotto = new Prodotto(codice, time, prezzo_minimo);
			int cifra = 250;
			Offerta offerta = new Offerta(prodotto, this, cifra);
			
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(offerta);
			oos.flush();
			
			
			DatagramSocket ds = new DatagramSocket(UDP_PORT_PIUALTA);

			byte[] buffer = new byte[256];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			ds.receive(packet);
			
			String msg = new String(packet.getData());
			String[] msg_splitted = msg.split(",");
			String client_id = msg_splitted[0];
			String codice_prodotto = msg_splitted[1];
			
			if(client_id.equals(this.client_id))
				System.out.println("VINCITORE_" + "[" + codice_prodotto + "]");
			else
				System.out.println("NON VINCITORE_" + "[" + codice_prodotto + "]");
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String...strings) {
		Random random = new Random();
		int id = random.nextInt();
		Client client = new Client(String.valueOf(id));
		client.start();
	}

}
