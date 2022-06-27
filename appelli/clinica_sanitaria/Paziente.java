package clinica_sanitaria;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class Paziente extends Thread {
	
	private int matricola;
	
	public Paziente(int matricola) {
		this.matricola = matricola;
	}
	
	@Override public String toString() {
		return "Paziente " + matricola;
	}
	
	
	private int TCP_PORT_PRENOTAZIONE = 3000;
	private Socket server;
	private ObjectOutputStream oos_prenotazione;
	private String prenotazione;
	
	private int TCP_PORT_ANNULLAMENTO_PRENOTAZIONE = 4000;
	private Socket server_annullare_prenotazione;
	private ObjectOutputStream oos_annullamento_prenotazione;
	private String annullamento_prenotazione;
	private ObjectInputStream ois_annullamento_prenotazione;
	private String ack_annullamento_prenotazione;

	
	@Override public void run() {
		
		try {
			
			server =  new Socket("localhost", TCP_PORT_PRENOTAZIONE);
			System.out.println(server.toString());
			
			oos_prenotazione = new ObjectOutputStream(server.getOutputStream());
			Random random = new Random();
			int codice_esame = random.nextInt(1, 5);
			prenotazione = codice_esame + " " + this.matricola;
			oos_prenotazione.writeObject(prenotazione);
			oos_prenotazione.flush();
			
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(System.in);
			String line = scanner.nextLine();
			
			server_annullare_prenotazione = new Socket("localhost", TCP_PORT_ANNULLAMENTO_PRENOTAZIONE);
			System.out.println(server_annullare_prenotazione.toString());
			
			oos_annullamento_prenotazione = new ObjectOutputStream(server_annullare_prenotazione.getOutputStream());
			annullamento_prenotazione = line;
			oos_annullamento_prenotazione.writeObject(annullamento_prenotazione);
			oos_annullamento_prenotazione.flush();
			
			ois_annullamento_prenotazione = new ObjectInputStream(server_annullare_prenotazione.getInputStream());
			ack_annullamento_prenotazione = (String) ois_annullamento_prenotazione.readObject();
			System.out.println(ack_annullamento_prenotazione);
			
		}catch(IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	
	public static void main(String...strings) {
		Random random = new Random();
		int matricolaPaziente = random.nextInt(100, 8501);
		Paziente paziente = new Paziente(matricolaPaziente);
		paziente.start();
	}

	
}
