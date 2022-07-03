package asta.esercizio4;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Server {
	
	private final static int TCP_PORT_OFFERTE=3000, UDP_PORT_PIUALTA=4000;
	private final static String HOST_NAME="asta.unical.it";
	private final static int TIMEOUT = 10000;
	
	public static List<Prodotto> prodotti;
	private static List<Offerta> offerte;
	
	public Server(List<Prodotto> prodotti) {
		this.prodotti = prodotti;
		this.offerte = Collections.synchronizedList(new LinkedList<Offerta>());
	}

	
	public static void main(String...strings) {
		
		Server s = new Server(Collections.synchronizedList(new LinkedList<Prodotto>()));
		
		try {
			
			synchronized(prodotti) { 
				synchronized(offerte) {
					
					ServerSocket server = new ServerSocket(TCP_PORT_OFFERTE);
					System.out.println(server.toString());
					server.setSoTimeout(TIMEOUT);
					
					while(true) {
						
						Socket socket = server.accept();
						System.out.println(socket.toString());
						
						RichiestaHandler rh = new RichiestaHandler(socket, prodotti, offerte);
						rh.start();
									
					}
					
					Socket socket = server.accept();
					System.out.println(socket.toString());
					
					ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
					ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
					String server_offline = "SCADUTA";
					oos.writeObject(server_offline);
					oos.flush();
					
					DatagramSocket ds = new DatagramSocket(UDP_PORT_PIUALTA);
					offerte.sort(new OfferteComparator());
					Offerta piuAlta = ((LinkedList<Offerta>)offerte).getFirst();
					String offerta_vincente = piuAlta.getClient().getId() + "," + piuAlta.getProdotto().getCodice();
					byte[] buffer = offerta_vincente.getBytes();
					InetAddress address = InetAddress.getByName(HOST_NAME);
					DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, UDP_PORT_PIUALTA);
					
				}
			}
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	class OfferteComparator implements Comparator<Offerta> {
		
		@Override public int compare(Offerta o1, Offerta o2) {
			if(o1.getCifra() > o2.getCifra())
				return 1;
			else if(o1.getCifra() < o2.getCifra())
				return -1;
			else
				return 0;
		}
		
	}
	

}
