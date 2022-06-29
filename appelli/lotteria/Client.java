package lotteria;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.List;
import java.util.LinkedList;

public class Client {
	
	private final static int TCP_PORT = 3000;
	private final static int MULTICAST_PORT = 4000;
	private final static String address = "localhost";
	
	private Biglietto biglietto_da_giocare;
	
	public Client(Biglietto biglietto_da_giocare) {
		this.biglietto_da_giocare = biglietto_da_giocare;
	}
	
	public int getTCP_PORT() {
		return TCP_PORT;
	}
	
	public int getMULTICAST_PORT() {
		return MULTICAST_PORT;
	}
	
	public String getAddress() {
		return address;
	}
	
	public Biglietto getBigliettoDaGiocare() {
		return biglietto_da_giocare;
	}
	
	@SuppressWarnings("deprecation")
	public static void main(String...strings) {
		Biglietto biglietto_da_giocare = new Biglietto("Superenalotto");
		Client client = new Client(biglietto_da_giocare);
		
		Socket socket;
		ObjectOutputStream oos;
		ObjectInputStream ois;
		Risposta risposta;
		List<Biglietto> biglietti_giocati;
		Biglietto biglietto_giocato;
		MulticastSocket msocket;
		InetAddress group_address;
		byte[] buffer;
		DatagramPacket packet;
		String biglietto_vincente;
		String[] biglietto_vincente_splitted;
		int codice_biglietto_vincente;
		String lotteria_biglietto_vincente;
		
		try {
			
			/** FASE BIGLIETTO GIOCATO */
			socket = new Socket(client.getAddress(), client.getTCP_PORT());
			System.out.println(socket.toString());
			
			/** richiesta dei biglietti da giocare */
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(client.getBigliettoDaGiocare());
			oos.flush();
			
			/** risposta dei biglietti giocati */
			ois = new ObjectInputStream(socket.getInputStream());
			risposta = (Risposta)ois.readObject();
			biglietti_giocati = risposta.getBigliettiGiocati();
			biglietto_giocato = ((LinkedList<Biglietto>)biglietti_giocati).getFirst();
			
			
			/** FASE RICEZIONE VINCITORE */
			msocket = new MulticastSocket(client.getMULTICAST_PORT());
			group_address = InetAddress.getByName(client.getAddress());
			msocket.joinGroup(group_address);
			
			buffer = new byte[256];
			packet = new DatagramPacket(buffer, buffer.length);
			msocket.receive(packet);
			
			biglietto_vincente = new String(packet.getData());
			biglietto_vincente_splitted = biglietto_vincente.split(",");
			codice_biglietto_vincente = Integer.valueOf(biglietto_vincente_splitted[0]);
			lotteria_biglietto_vincente = biglietto_vincente_splitted[1];
			
			if(codice_biglietto_vincente==biglietto_giocato.getCodice() &&
			   lotteria_biglietto_vincente.equals(biglietto_giocato.getNomeLotteria())) {
				System.out.println(biglietto_vincente);
				System.out.println("Ho vinto!!!");
			}
			else
				System.out.println(biglietto_vincente);
			
		}catch(IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
		
	}

}
