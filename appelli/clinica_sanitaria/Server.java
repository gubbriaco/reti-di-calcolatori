package clinica_sanitaria;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class Server {

	private LinkedList<Statistica> statistiche;
	
	private boolean attivo = false;
	
	private LinkedList<Medico> medici;
	private LinkedList<Esame> esami;
	private int numeroEsamiTotali;
	
	private Semaphore mutex = new Semaphore(1);
	private Semaphore[] esamiSem;
	
	private Medico medico1, medico2;
	
	public Server(int numeroEsamiTotali) {
		attivo = true;
		
		this.numeroEsamiTotali = numeroEsamiTotali;
		
		medici = new LinkedList<>();
		esami = new LinkedList<>();
		
		for(int i=0;i<numeroEsamiTotali;++i)
			esami.set(i, new Esame(i+1));
		
		esamiSem = new Semaphore[numeroEsamiTotali];
		for(int i=0;i<esamiSem.length;++i)
			esamiSem[i] = new Semaphore(0);
		
		statistiche = new LinkedList<>();
		
		medici.add(new Medico(0)); 
		medici.add(new Medico(1));
		medico1 = medici.get(0); 
		medico2 = medici.get(1);
	}
	
	private static class Statistica {
		
		private Esame esame;
		private Medico medico1, medico2;
		
		public Statistica(Esame esame, Medico medico1, Medico medico2) {
			this.esame = esame;
			this.medico1 = medico1;
			this.medico2 = medico2;
		}
		
		@Override public String toString() {
			return esame.getCodice() + "\n" + esame.pazienti.size() + "\n" +
					medico1.pazienti.toString() + "\n" + medico2.pazienti.toString();
		}
		
		
	}
	
	private static class Esame {
		
		private int codice;
		private int postiDisponibili;
		private LinkedList<Paziente> pazienti;
		
		private static int MAX_PAZIENTI = 20;
		
		public Esame(int codice) {
			this.codice = codice;
			pazienti = new LinkedList<>();
		}
		
		public int getCodice() {
			return codice;
		}
		
		public int getPostiDisponibili() {
			return postiDisponibili;
		}
		
		public synchronized void aggiungiPazienteInCoda(Paziente paziente) {
			pazienti.addLast(paziente);
			postiDisponibili--;
		}
		
		public synchronized void rimuoviPazienteInCoda(Paziente paziente) {
			pazienti.remove(paziente);
			postiDisponibili++;
		}
		
	}
	
	
	private static class Medico {
		
		private int matricola;
		private LinkedList<Paziente> pazienti;
		
		private static int MAX_PAZIENTI = 10;
		
		public Medico(int matricola) {
			this.matricola = matricola;
			pazienti = new LinkedList<>();
		}
		
		public int getMatricola() {
			return matricola;
		}
		
		public synchronized void curaPaziente(Paziente paziente) {
			pazienti.add(paziente);
		}
		
		public synchronized void nonCurarePiuPaziente(Paziente paziente) {
			pazienti.remove(paziente);
		}
		
	}
	
	private static int portaTCP_PRENOTAZIONI = 3000, portaTCP_ANNULLARE_PRENOTAZIONI = 4000;
	private ServerSocket serverPrenotazioni, serverAnnullarePrenotazioni;
	private Socket pazienteCheVuolePrenotare, pazienteCheVuoleAnnullarePrenotazione;
	private BufferedReader brPrenotazioni, brAnnullarePrenotazioni;
	private PrintWriter pwPrenotazioni, pwAnnullarePrenotazioni;
	
	public void init() {
		try {

			serverPrenotazioni = new ServerSocket(portaTCP_PRENOTAZIONI);
			System.out.println(serverPrenotazioni.toString());
			
			serverAnnullarePrenotazioni = new ServerSocket(portaTCP_ANNULLARE_PRENOTAZIONI);
			System.out.println(serverAnnullarePrenotazioni.toString());
			
			
			while(attivo) {

				pazienteCheVuolePrenotare = serverPrenotazioni.accept();
				System.out.println(pazienteCheVuolePrenotare.toString());
				brPrenotazioni = new BufferedReader(new InputStreamReader(pazienteCheVuolePrenotare.getInputStream()));
				String esameRichiestoStr = brPrenotazioni.readLine();
				int esameRichiesto = Integer.valueOf(esameRichiestoStr);
				
				LinkedList<Object> prenotazione = verificaDisponibilita(esameRichiesto);
				
				if(!((boolean)prenotazione.get(0))) {
					esamiSem[esameRichiesto].acquire();
					esami.get(esameRichiesto).aggiungiPazienteInCoda(  );
					
					boolean postoLiberato = false, chiusuraSocket = false;
					int minuti = 0, UN_ORA = 60;
					while(!postoLiberato || !chiusuraSocket) {
						if(minuti==UN_ORA) {
							chiusuraSocket = true;
							pazienteCheVuolePrenotare.close();
						}
						for(int i=0;i<esami.size();++i) {
							if(esami.get(i).pazienti.getFirst().finished) {
								System.out.println("Il " + esami.get(i).pazienti.getFirst() + " ha terminato l'esame medico.");
								esami.get(i).pazienti.removeFirst();
								esamiSem[i].release();
								postoLiberato = true;
								pwPrenotazioni = new PrintWriter(pazienteCheVuolePrenotare.getOutputStream());
								Esame esame = (Esame)prenotazione.get(1);
								Medico medico = (Medico)prenotazione.get(2);
								String stringaPrenotazione = esameRichiesto + " " + 
										(esame.MAX_PAZIENTI-esame.postiDisponibili) + " " + 
										 medico.getMatricola();
								pwPrenotazioni.println(stringaPrenotazione);
							}
						}
						minuti++;
					}
				}
				else {
					System.out.println("Richiesta esame accettata.");
					pwPrenotazioni = new PrintWriter(pazienteCheVuolePrenotare.getOutputStream());
					Esame esame = (Esame)prenotazione.get(1);
					Medico medico = (Medico)prenotazione.get(2);
					String stringaPrenotazione = esameRichiesto + " " + 
							(esame.MAX_PAZIENTI-esame.postiDisponibili) + " " + 
							 medico.getMatricola();
					pwPrenotazioni.println(stringaPrenotazione);
				}
				
				pwPrenotazioni = new PrintWriter(pazienteCheVuolePrenotare.getOutputStream());
				pwPrenotazioni.println("****** service not available ******");
				
				
				pazienteCheVuoleAnnullarePrenotazione = serverAnnullarePrenotazioni.accept();
				System.out.println(pazienteCheVuoleAnnullarePrenotazione.toString());
				brAnnullarePrenotazioni = new BufferedReader(new InputStreamReader(pazienteCheVuoleAnnullarePrenotazione.getInputStream()));
				String esameDaAnnullare = brAnnullarePrenotazioni.readLine();
				int codiceEsameDaAnnullare = Integer.valueOf(esameDaAnnullare);
				esami.remove(codiceEsameDaAnnullare).rimuoviPazienteInCoda(  );
				esami.remove(codiceEsameDaAnnullare);
				
				pwAnnullarePrenotazioni = new PrintWriter(pazienteCheVuoleAnnullarePrenotazione.getOutputStream());
				pwAnnullarePrenotazioni.println("------- ESAME ANNULLATO COME RICHIESTO -------");
				
			}

			brPrenotazioni.close(); brAnnullarePrenotazioni.close();
			pwPrenotazioni.close(); pwAnnullarePrenotazioni.close();
			pazienteCheVuolePrenotare.close(); pazienteCheVuoleAnnullarePrenotazione.close();
			
			
			Statistica statistica;
			for(int i=0;i<esami.size();++i) {
				statistica = new Statistica( esami.get(i), medico1, medico2 );
				statistiche.add(statistica);
			}
			
			int portaDirezione = 5000;
			DatagramSocket direzione = new DatagramSocket(5000);
			InetAddress local = InetAddress.getByName("localhost");
			DatagramPacket statisticaPacket;
			
			for(int i=0;i<statistiche.size();++i) {
				byte[] buf = new byte[256];
				buf = statistiche.get(i).toString().getBytes();
				statisticaPacket = new DatagramPacket(buf, buf.length, local, portaDirezione);
				direzione.send(statisticaPacket);
				
			}
			
			
		}catch(IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private LinkedList<Object> verificaDisponibilita(int esameRichiesto) {
		LinkedList<Object> prenotazione = new LinkedList<>();
		
		Esame esame = esami.get(esameRichiesto);
		if(esame.getPostiDisponibili()==esame.MAX_PAZIENTI) {
			prenotazione.add(false);
			prenotazione.add(esame);
			prenotazione.add(medico1);
		}

		if(medico1.pazienti.size()<medico2.MAX_PAZIENTI) {

			esamiSem[esameRichiesto].acquire();
			medico1.curaPaziente(  );
			prenotazione.add(true);
			prenotazione.add(esame);
			prenotazione.add(medico1);
		}
		else {

			esamiSem[esameRichiesto].acquire();
			medico2.curaPaziente(  );
			prenotazione.add(true);
			prenotazione.add(esame);
			prenotazione.add(medico2);
		}
		
		return prenotazione;
	}
	
	
	public static void main(String...strings) {
		
		Server server = new Server( 6 );
		server.init();
		
	}
	
}
