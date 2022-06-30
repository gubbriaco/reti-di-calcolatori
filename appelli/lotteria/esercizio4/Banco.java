package lotteria.esercizio4;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

public class Banco {
	
	private final static int TCP_PORT = 3000;
	private final static int MULTICAST_PORT = 4000;
	private final static String address = "230.0.0.1";
	
	@SuppressWarnings("unused")
	private List<Lotteria> lotterie;
	@SuppressWarnings("unused")
	private List<Biglietto> biglietti;
	
	private static List<Biglietto> biglietti_giocati;
	
	public Banco(List<Lotteria> lotterie, List<Biglietto> biglietti) {
		this.lotterie = lotterie;
		this.biglietti = biglietti;
		
		biglietti_giocati = Collections.synchronizedList(new LinkedList<Biglietto>());
	}
	
	
	public int getTCP_PORT() {
		return TCP_PORT;
	}
	
	public int getMULTICAST_PORT() {
		return MULTICAST_PORT;
	}
	
	
	public static void storeBigliettiGiocati(Biglietto biglietto) {
		biglietti_giocati.add(biglietto);
	}
	
	
	public static void main(String...strings) {
		List<Lotteria> lotterie = Collections.synchronizedList(new LinkedList<Lotteria>());
		List<Biglietto> biglietti = Collections.synchronizedList(new LinkedList<Biglietto>());
		Banco banco = new Banco(lotterie, biglietti);
		
		ServerSocket server;
		Socket socket;
		Calendar tempo_init_server, tempo_close_server, tempo_attuale, tempo_richiesta;
		RichiestaHandler rh;
		MulticastSocket multicast_socket;
		String vincitoreString;
		byte[] buffer;
		InetAddress group_address;
		DatagramPacket packet;
		
		try {
			synchronized(biglietti_giocati) {
				/** FASE VENDITA BIGLIETTI - 60 MINUTI */
				System.out.println("******* FASE VENDITA BIGLIETTI *******");
				server = new ServerSocket(banco.getTCP_PORT());
				System.out.println(server.toString());
				
				tempo_init_server = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"), Locale.ITALY);
				System.out.println("init_server: " + tempo_init_server.toString());
				tempo_close_server = tempo_init_server;
				tempo_close_server.add(Calendar.MINUTE, 60);
				tempo_attuale = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"), Locale.ITALY);
				System.out.println("current: " + tempo_attuale.toString());
				
				while( tempo_close_server.get(Calendar.MINUTE) != tempo_attuale.get(Calendar.MINUTE)) {
					
					socket = server.accept();
					System.out.println(socket.toString());
					
					tempo_richiesta = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"), Locale.ITALY);
					
					rh = new RichiestaHandler( tempo_richiesta, socket, lotterie, biglietti );
					rh.start();
					
					while(rh.isAlive())
						;
					
					tempo_attuale = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"), Locale.ITALY);
				}
				server.close();
				System.out.println(server.toString());
				System.out.println("server_closed: "  + tempo_close_server.toString());
				
				
				Biglietto vincitore = calcolaVincitore();
				
				System.out.println("******* FASE VINCITORE LOTTERIA *******");
				multicast_socket = new MulticastSocket(banco.getMULTICAST_PORT());
				System.out.println(multicast_socket.toString());
				
				vincitoreString = vincitore.toString();
				buffer = new byte[256];
				buffer = vincitoreString.getBytes();
				
				group_address = InetAddress.getByName(address);
				packet = new DatagramPacket(buffer, buffer.length, group_address, banco.getMULTICAST_PORT());
				multicast_socket.send(packet);
				System.out.println("Broadcasting..");
				System.out.println("Winner is: " + vincitore.toString());
			}
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private static Biglietto calcolaVincitore() {
		List<Integer> codici_biglietti = new LinkedList<>();
		for(int i=0;i<biglietti_giocati.size();++i)
			codici_biglietti.add(biglietti_giocati.get(i).getCodice());
		
		Random random = new Random();
		int MIN_POS = 0, MAX_POS = codici_biglietti.size();
		int pos_biglietto_vincitore = random.nextInt( MIN_POS, MAX_POS );
		
		int codice_biglietto_vincitore = codici_biglietti.get(pos_biglietto_vincitore);
		
		Biglietto biglietto_vincitore = null;
		for(int i=0;i<biglietti_giocati.size();++i)
			if(biglietti_giocati.get(i).getCodice() == codice_biglietto_vincitore)
				biglietto_vincitore = biglietti_giocati.get(i);
		
		return biglietto_vincitore;	
	}
	

}
