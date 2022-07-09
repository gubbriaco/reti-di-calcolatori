package Giugno14_2022;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class Utente extends Thread {
	
	private final static int TCP_PORT_INVIO = 4000 , MC_PORT = 6000;
	private final static String ADDRESS_SERVER = "job.unica.it", ADDRESS = "230.0.0.1";
	
	private int id;
	private List<Offerta> offerte_ricevute;
	
	public Utente(int id) {
		this.id = id;
		offerte_ricevute = new LinkedList<>();
	}
	
	public void aggiungiOfferta(Offerta o) {
		offerte_ricevute.add(o);
	}
	
	@Override public void run() {
		
		try {
			
			InetAddress address_group = InetAddress.getByName(ADDRESS);
			MulticastSocket ms = new MulticastSocket(MC_PORT);
			ms.joinGroup(address_group);
			
			byte[] buffer = new byte[256];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			ms.receive(packet);
			
			String offerta = new String(packet.getData());
			String[] offerta_splitted = offerta.split(",");
			int id_numerico = Integer.valueOf(offerta_splitted[0]);
			
			Socket socket = new Socket(ADDRESS_SERVER, TCP_PORT_INVIO);
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			String curriculum = "";
			Candidatura candidatura = new Candidatura(id_numerico, curriculum);
			oos.writeObject(candidatura);
			oos.flush();
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String...strings) {
		
		final int M = 10;
		
		for(int i=0;i<M;++i)
			new Utente(i).start();
		
	}

}
