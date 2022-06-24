package betserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Date;
import java.util.LinkedList;
import java.util.Random;
import java.util.StringTokenizer;

public class Server {

	private static class Puntata {
		
		private String client;
		private int numeroCavallo;
		private int puntata;
		
		public Puntata(String client, int numeroCavallo, int puntata) {
			this.client = client;
			this.numeroCavallo = numeroCavallo;
			this.puntata = puntata;
		}
		
		public String getClient() {
			return client;
		}
		
		public int getNumeroCavallo() {
			return numeroCavallo;
		}
		
		public int getPuntata() {
			return puntata;
		}
		
		@Override public String toString() {
			return "<" + numeroCavallo + "> <" + puntata + ">";
		}
		
	}
	
	private LinkedList<Puntata> puntate;
	private LinkedList<String> clients;
	
	private static int MIN_NR_CAVALLO = 1, MAX_NR_CAVALLO = 12+1;
	private Random random;
	
	private int ora_limite;
//	private int actual;
//	private int limit;
//	private Date newDate;
	
	public Server(int ora_limite) {
		puntate = new LinkedList<>();
		clients = new LinkedList<>();
		this.ora_limite = ora_limite;
//		actual = new Date().getSeconds();
//		limit = actual + ora_limite;
//		newDate = new Date();
//		newDate.setSeconds(limit);
	}
	
	private int port = 8002;
	private String address = "230.0.0.1";
	private InetAddress group;
	
	private MulticastSocket socket;
	private byte[] buffer;
	private DatagramPacket packet;
	private String puntata;
	
	public void openScommesse() {
		
		try {
			
			socket = new MulticastSocket( port );
			
			String toTokenize, nrCavallo = "", soldi = "";
			StringTokenizer st;
			String client = "";
			
			int ora = 0;
			while( ora < ora_limite ) {
				
				buffer = new byte[256];
				packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);
				
				puntata = new String(packet.getData());
				client = socket.getInetAddress().toString();
				toTokenize = puntata.replace("<", "");
				toTokenize = puntata.replace(">", "");
				
				st = new StringTokenizer(toTokenize);
				boolean more = true;
				String nextToken = "";
				
				while(more) {
					
					nextToken = st.nextToken();
					if(nextToken.equals(",")) {
						while(st.hasMoreTokens()) {
							nextToken = st.nextToken();
							soldi += nextToken;
						}
					}
					else {
						nrCavallo += nextToken;
					}
					
				}
				
				int numeroCavallo = Integer.parseInt(nrCavallo);
				int puntataSoldi = Integer.parseInt(soldi);
				
				Puntata puntata = new Puntata(client, numeroCavallo, puntataSoldi);
				puntate.addLast(puntata);
				clients.addLast(client);
				
				ora++;
				
			}
			
			
			int cavalloVincente = calcolaCavalloVincente();
			
			
			
		}catch(IOException e) {
			socket.close();
			System.err.println(e);
		}
		
	}
	
	private int calcolaCavalloVincente() {
		
		return 0;
		
	}
	
	
	
	public static void main(String...strings) {
		
		Server server = new Server(90);
		server.openScommesse();
		
	}
	
	
	
	
	
	
	

}
