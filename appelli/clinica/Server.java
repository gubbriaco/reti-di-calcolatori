package clinica;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import java.util.Iterator;

public class Server {

	/** i medici sono indicizzati nel seguente modo: -1 nessun medico assegnato == esame ancora
	 *  non assegnato, 0 medico avente 10 esami, 1 medico avente i rimanenti 10 esami */
	
	private static class Medico {
		
		private int matricola, disponibilita;
		
		public Medico(int matricola) {
			this.matricola = matricola;
			this.disponibilita = -1;
		}
		
		public int getMatricola() {
			return matricola;
		}
		
		public int getDisponibilita() {
			return matricola;
		}
		
		public void setDisponibilita(int disponibilita) {
			this.disponibilita = disponibilita;
		}
		
	}
	
	private static class Esame {
		
		private int numeroProgressivo;
		private int codice;
		private int postiDisponibili;
		
		public Esame(int codice) {
			numeroProgressivo = 1;
			this.codice = codice;
			postiDisponibili = 20;
		}
		
		public int getCodice() {
			return codice;
		}
		
		public void aggiungiNuovoPaziente() {
			numeroProgressivo++;
			postiDisponibili--;
		}
		
		public void rimuoviPrenotazione() {
			numeroProgressivo--;
			postiDisponibili++;
		}
		
		public int getPostiDisponibili() {
			return postiDisponibili;
		}
		
		public int getNumeroProgressivo() {
			return numeroProgressivo;
		}
		
	}
	
	/** gli esami sono indicizzati da 0 a 19  */
	
	
	private Map<Esame, Medico> esami; /** MAX 20 ESAMI TOTALI - 10 per ogni medico */
	
	private int oreServizio; /** dalle 8 alle 12 */
	
	private int portaTCPPrenotazione = 3000;
	
	public Server(int oreServizio) {
		
		esami = new HashMap<>();
		for(int i=0;i<20;++i)
			esami.put(new Esame(i), new Medico((i+25)*125));
		
		this.oreServizio = oreServizio;
		
		INIT_SERVER();
	}
	
	private boolean attivo = true;
	private ServerSocket server;
	private BufferedReader br;
	
	private void INIT_SERVER() {
		try {
			server = new ServerSocket(portaTCPPrenotazione);
			System.out.println(server.toString());
			
			while(attivo) {
				
				Socket paziente = server.accept();
				System.out.println(paziente.toString() + " accepted");
				
				/** legge l'esame richiesto dal paziente */
				br = new BufferedReader(new InputStreamReader(paziente.getInputStream()));
				String esameRichiestoStr = br.readLine();
				int esameRichiesto = Integer.valueOf(esameRichiestoStr);
				boolean okEsame = verificaDisponibilita(esameRichiesto);
				
				if(okEsame) {
					
					PrintWriter pw = new PrintWriter(paziente.getOutputStream());
					Set<Esame> keyEsami = esami.keySet();
					Iterator<Esame> it = keyEsami.iterator();
					int pos = 0;
					int progressivoEsame = -1;
					while(it.hasNext()) {
						if(pos == esameRichiesto) {
							Esame esame = it.next();
							progressivoEsame = esame.getNumeroProgressivo();
							break;
						}
						Esame esame = it.next();
						pos++;
					}
					/** stringa di prenotazione al paziente */
					String msg = esameRichiesto + " " + progressivoEsame + esami.get(esameRichiesto).getMatricola();
					pw.println(msg);
					
				}
				
				
				
				
			}
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private int mediciZero = 0, mediciUno = 0;
	
	private boolean verificaDisponibilita(int esameRichiesto) {
		boolean esameDisponibile = false;
		
		if(esami.get(esameRichiesto).getDisponibilita() == -1) {
			if(mediciZero < mediciUno) {
				esami.get(esameRichiesto).setDisponibilita(0);
				mediciZero++;
				esameDisponibile = true;
			}
			else {
				esami.get(esameRichiesto).setDisponibilita(1);
				mediciUno++;
				esameDisponibile = true;
			}
		}
		
		
		return esameDisponibile;
	}
	
	public static void main(String...strings) throws IOException {
		
		int oreServizio = 4;
		Server clinica = new Server(4);
		
		int portaTCPAnnullarePrenotazione = 4000;
		ServerSocket server = new ServerSocket(portaTCPAnnullarePrenotazione);
		System.out.println(server.toString());

		while(clinica.attivo) {
			Socket paziente = server.accept();
			BufferedReader br = new BufferedReader(new InputStreamReader(paziente.getInputStream()));
			
			int codiceEsameDaAnnullare = Integer.valueOf(br.readLine());
			
			Set<Esame> keyEsami = clinica.esami.keySet();
			Iterator<Esame> it = keyEsami.iterator();
			int pos = 0;
			while(it.hasNext()) {
				if(pos == codiceEsameDaAnnullare) {
					Esame esame = it.next();
					int nrProgressivo = esame.getNumeroProgressivo();
					esame.rimuoviPrenotazione();
					break;
				}
				Esame esame = it.next();
				pos++;
			}
		}
		
		
		
	}

}
