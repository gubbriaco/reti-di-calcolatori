package asta.esercizio4;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.Random;

public class Client{
	
	private final static int TCP_PORT_OFFERTE=3000, UDP_PORT_PIUALTA=4000;
	private final static String HOST_NAME="asta.unical.it";
	
	private String id;
	
	public Client(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	
	@SuppressWarnings("resource")
	public static void main(String...strings) {
		
		Random random = new Random();
		int id = random.nextInt();
		Client client = new Client(String.valueOf(id));
		
		try {
		
			Socket socket = new Socket(HOST_NAME, TCP_PORT_OFFERTE);
			System.out.println(socket.toString());
			
			int codice = 81371;
			long time = 500; 
			double prezzo_minimo = 200;
			Prodotto prodotto = new Prodotto(codice, time, prezzo_minimo);
			int cifra = 250;
			Offerta offerta = new Offerta(prodotto, client, cifra);
			
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
			
			if(client_id.equals(client.getId()))
				System.out.println("VINCITORE_" + "[" + codice_prodotto + "]");
			else
				System.out.println("NON VINCITORE_" + "[" + codice_prodotto + "]");
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}


}
