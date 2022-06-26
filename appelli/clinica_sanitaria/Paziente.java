package clinica_sanitaria;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Paziente {
	

	private static int portaTCP_PRENOTAZIONI = 3000, portaTCP_ANNULLARE_PRENOTAZIONI = 4000;
	private ServerSocket pazienteCheVuolePrenotare, pazienteCheVuoleAnnullarePrenotazione;
	private Socket serverPrenotazioni, serverAnnullarePrenotazioni;
	private BufferedReader brPrenotazioni, brAnnullarePrenotazioni;
	private PrintWriter pwPrenotazioni, pwAnnullarePrenotazioni;
	
	public Paziente() {
		
		
	}
	
	public void init() {
		try {
			
			pazienteCheVuolePrenotare = new ServerSocket(portaTCP_PRENOTAZIONI);
			System.out.println(serverPrenotazioni.toString());
			
			
			
			Scanner sc = new Scanner(System.in);
			String line = sc.nextLine();
			
			if(line.toUpperCase().equals("ANNULLA") || line.toUpperCase().equals("ANNULLARE") ) {
				
				pazienteCheVuoleAnnullarePrenotazione = new ServerSocket(portaTCP_ANNULLARE_PRENOTAZIONI);
				System.out.println(serverAnnullarePrenotazioni.toString());
				
				serverAnnullarePrenotazioni = pazienteCheVuoleAnnullarePrenotazione.accept();
				pwAnnullarePrenotazioni = new PrintWriter(serverAnnullarePrenotazioni.getOutputStream());
				pwAnnullarePrenotazioni.println("ANNULLARE");
				
				
			}
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String...strings) {
		
		Paziente paziente = new Paziente();
		paziente.init();
		
	}
	
}
