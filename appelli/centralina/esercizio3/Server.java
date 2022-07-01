package centralina.esercizio3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Server {
	
	private final static int TCP_PORT_richieste1 = 3000, 
			                 TCP_PORT_richieste2 = 4000,
			                 TCP_PORT_CENTRALINE = 5000,
			                 UDP_PORT_CENTRALINE = 6000;
	
	public int getTCP_PORT_richieste1() {
		return TCP_PORT_richieste1;
	}
	
	public int getTCP_PORT_richieste2() {
		return TCP_PORT_richieste2;
	}
	
	public int getTCP_PORT_CENTRALINE() {
		return TCP_PORT_CENTRALINE;
	}
	
	public int getUDP_PORT_CENTRALINE() {
		return UDP_PORT_CENTRALINE;
	}
	
	
	private static List<String> misure; 
	
	private static List<Centralina> centraline;
	
	public Server(List<Centralina> centraline) {
		this.centraline = centraline;
		misure = Collections.synchronizedList(new LinkedList<String>());
	}
	
	public static void aggiungiMisure(String misure_effettuate) {
		misure.add(misure_effettuate);
	}
	
	public static List<Centralina> getCentraline() {
		return centraline;
	}
	
	
	
	public static void main(String...strings) {
		
		List<Centralina> centraline = Collections.synchronizedList(new LinkedList<Centralina>());

		synchronized(centraline) { synchronized(misure) {
			
			Server server;
			ServerSocket server1, server2, server3;
			Socket socket1, socket2, socket3;
			ObjectInputStream ois1, ois2, ois3;
			Richiesta1 r1;
			Richiesta2 r2;
			Richiesta3 r3;
			DatagramSocket ds;
			DatagramPacket p;
			byte[] buffer;
			
			try {
				
				server  = new Server(centraline);
				
				server1 = new ServerSocket(server.getTCP_PORT_richieste1());
				System.out.println(server1.toString());
				
				server2 = new ServerSocket(server.getTCP_PORT_richieste2());
				System.out.println(server2.toString());
				
				server3 = new ServerSocket(server.getTCP_PORT_CENTRALINE());
				System.out.println(server3.toString());
		
				while(true) {
				
					socket1 = server1.accept();
					System.out.println(socket1.toString());
					ois1 = new ObjectInputStream(socket1.getInputStream());
					String r1Str = (String)ois1.readObject();
					String[] r1split = r1Str.split(",");
					int id1 = Integer.valueOf(r1split[0]);
					String gr1 = r1split[1];
					r1 = new Richiesta1(id1, gr1);
					
					RichiestaHandler rh1 = new RichiestaHandler(socket1, r1);
					rh1.start();
					
					
					socket2 = server2.accept();
					System.out.println(socket2.toString());
					ois2 = new ObjectInputStream(socket2.getInputStream());
					String r2Str = (String)ois2.readObject();
					String[] r2split = r2Str.split(",");
					int id2 = Integer.valueOf(r2split[0]);
					long t1 = Long.valueOf(r2split[1]), t2 = Long.valueOf(r2split[2]);
					r2 = new Richiesta2(id2, t1, t2);
					
					RichiestaHandler rh2 = new RichiestaHandler(socket2, r2);
					rh2.start();
					
					socket3 = server3.accept();
					System.out.println(socket3.toString());
					ois3 = new ObjectInputStream(socket3.getInputStream());
					r3 = (Richiesta3)ois2.readObject();
					
					RichiestaHandler rh3 = new RichiestaHandler(socket3, r3);
					rh2.start();
					
					ds = new DatagramSocket(server.getUDP_PORT_CENTRALINE());
					buffer = new byte[256];
					p = new DatagramPacket(buffer, buffer.length);
					ds.receive(p);
					
					String misure = new String(p.getData());
					Server.aggiungiMisure(misure);
					
				}
			}catch(IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
			
		}
		}
		
	}

}
