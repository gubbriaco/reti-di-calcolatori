package clinica;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;

public class Paziente {
	
	private int matricola;
	
	public Paziente(int matricola) {
		this.matricola = matricola;
		
		INIT_CLIENT();
	}
	
	private Socket server;
	private int portaTCPPrenotazione = 3000;
	private Random random;
	private PrintWriter pw;
	private int codiceEsame;
	
	private void INIT_CLIENT() {
		
		try {
			
			server = new Socket("localhost", portaTCPPrenotazione);
			random = new Random();
			codiceEsame = random.nextInt(0, 5);
			String esameRichiesto = String.valueOf(codiceEsame);
			
			pw = new PrintWriter(server.getOutputStream());
			pw.println(esameRichiesto);
			System.out.println(this.toString() + " ha richiesto l'esame " + esameRichiesto);
			
			
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}

	
	public static void main(String...strings) throws UnknownHostException, IOException {
		
		Random random = new Random();
		int matricola = random.nextInt();
		Paziente paziente = new Paziente(matricola);
		
		Scanner sc = new Scanner(System.in);
		String str = sc.nextLine();
		
		Socket server;
		int portaTCPAnnullarePrenotazione = 4000;
		PrintWriter pw;
		if(str.toUpperCase().equals("ANNULLA")) {
			server = new Socket("localhost", portaTCPAnnullarePrenotazione);
			System.out.println(server.toString());
			pw = new PrintWriter(server.getOutputStream());
			pw.println("" + paziente.codiceEsame);
			System.out.println(paziente.toString() + " ha annullato l'esame con codice" + paziente.codiceEsame);
			
		}
	}
	
}
