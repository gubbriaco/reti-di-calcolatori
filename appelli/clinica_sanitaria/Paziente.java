package clinica_sanitaria;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
	private ServerSocket paziente;
	private Socket server;
	private PrintWriter pw_prenotazione;
	private String prenotazione;
	
	private int TCP_PORT_ANNULLAMENTO_PRENOTAZIONE = 4000;
	private ServerSocket annullare_prenotazione;
	private Socket server_annullare_prenotazione;
	private PrintWriter pw_annullamento_prenotazione;
	private String annullamento_prenotazione;
	private BufferedReader br_annullamento_prenotazione;
	private String ack_annullamento_prenotazione;

	
	@Override public void run() {
		
		try {
			
			paziente = new ServerSocket(TCP_PORT_PRENOTAZIONE);
			System.out.println(paziente.toString());
			server = paziente.accept();
			System.out.println(server.toString());
			
			pw_prenotazione = new PrintWriter(server.getOutputStream());
			Random random = new Random();
			int codice_esame = random.nextInt(1, 5);
			prenotazione = codice_esame + " " + this.matricola;
			pw_prenotazione.println(prenotazione);
			
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(System.in);
			String line = scanner.nextLine();
			
			annullare_prenotazione = new ServerSocket(TCP_PORT_ANNULLAMENTO_PRENOTAZIONE);
			System.out.println(annullare_prenotazione.toString());
			server_annullare_prenotazione = annullare_prenotazione.accept();
			System.out.println(server_annullare_prenotazione.toString());
			
			pw_annullamento_prenotazione = new PrintWriter(server_annullare_prenotazione.getOutputStream());
			annullamento_prenotazione = line;
			pw_annullamento_prenotazione.println(annullamento_prenotazione);
			
			br_annullamento_prenotazione = new BufferedReader(new InputStreamReader(server_annullare_prenotazione.getInputStream()));
			ack_annullamento_prenotazione = br_annullamento_prenotazione.readLine();
			System.out.println(ack_annullamento_prenotazione);
			
		}catch(IOException e) {
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
