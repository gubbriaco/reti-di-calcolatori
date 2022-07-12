package Giugno14_2022;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Server {
	
	private final int TCP_PORT_OFFERTE = 3000,
					  TCP_PORT_CANDIDATURE = 5000;
	
	private List<Offerta> offerte;
	private List<String> addresses;
	
	public Server() {
		offerte = Collections.synchronizedList(new LinkedList<Offerta>());
		addresses = Collections.synchronizedList(new LinkedList<String>());
	}
	
	public synchronized void aggiungiOfferta(Offerta o) {
		offerte.add(o);
	}
	
	public synchronized void rimuoviOfferta(Offerta o) {
		offerte.remove(o);
	}
	
	public synchronized List<Offerta> getOfferte(){
		return offerte;
	}
	
	public synchronized void aggiungiAddressAzienda(String address) {
		addresses.add(address);
	}
	
	public synchronized void rimuoviAddressAzienda(String address) {
		addresses.remove(address);
	}
	
	public synchronized List<String> getAddresses() {
		return addresses;
	}
	
	
	@SuppressWarnings("resource")
	public static void main(String...strings) {
		
		Server s = new Server();
		
		try {
			
			/** server offerte di lavoro */
			ServerSocket offerte_server = new ServerSocket(s.TCP_PORT_OFFERTE);
			/** server candidature di lavoro */
			ServerSocket candidature_server = new ServerSocket(s.TCP_PORT_CANDIDATURE);
			
			while(true) {
				
				/** accetta offerte di lavoro dalle aziende */
				Socket socket_offerta = offerte_server.accept();
				
				ObjectInputStream ois = new ObjectInputStream(socket_offerta.getInputStream());
				Offerta offerta = (Offerta)ois.readObject();
				
				/** genera l'id numerico dell'offerta */
				Random random = new Random();
				int id_numerico = random.nextInt();
				
				offerta.setIdNumerico(id_numerico);
				s.aggiungiOfferta(offerta);
				s.aggiungiAddressAzienda(socket_offerta.getInetAddress().getHostName());
				
				/** offerta valida per 30 giorni */
				new ThreadTimer(s, socket_offerta, offerta).start();
				
				
				/** invio l'id numerico generato dell'offerta all'azienda che l'ha inviata*/
				ObjectOutputStream oos = new ObjectOutputStream(socket_offerta.getOutputStream());
				oos.writeObject(id_numerico);
				oos.flush();
				
				
				/** invio in broadcast agli utenti dell'offerta di lavoro ricevuta dall'azienda */
				new ThreadInvioOfferta(offerta).start();
				
				
				/** accetto candidature dagli utenti */
				Socket socket_candidatura = candidature_server.accept();
				
				/** gestisco le connessioni multiple per ogni candidatura richiesta */
				CandidaturaHandler ch = new CandidaturaHandler(socket_candidatura, s);
				ch.start();
				
			}
			
		}catch(IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	private static class ThreadInvioOfferta extends Thread {

		private Offerta offerta;
		
		public ThreadInvioOfferta(Offerta offerta) {
			this.offerta = offerta;
		}
		
		@Override public void run() {
			try {
				
				InetAddress address_group = InetAddress.getByName("230.0.0.1");
				final int MC_PORT = 6000;
				MulticastSocket mc = new MulticastSocket(MC_PORT);
				byte[] buffer = offerta.toString().getBytes();
				DatagramPacket packet = new DatagramPacket( 
						buffer, buffer.length, address_group, MC_PORT);
				mc.send(packet);
				
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private static class ThreadTimer extends Thread {
		
		private Offerta offerta;
		private Socket socket_offerta;
		private Server s;
		
		public ThreadTimer(Server s, Socket socket_offerta, Offerta offerta) {
			this.offerta = offerta;
			this.socket_offerta = socket_offerta;
			this.s = s;
		}
		
		@Override public void run() {
			try {
				
				offerta.setActive(true);
				TimeUnit.DAYS.sleep(30);
				offerta.setActive(false);
				s.rimuoviOfferta(offerta);
				s.rimuoviAddressAzienda(socket_offerta.getInetAddress().getHostName());
				
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	

}
