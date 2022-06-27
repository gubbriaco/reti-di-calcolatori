package clinica_sanitaria;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Random;
import java.util.StringTokenizer;

import clinica_sanitaria.Medico.Prenotazione;

public class Server {
	
	private LinkedList<Esame> esami;
	private LinkedList<Medico> medici;
	
	private boolean server_attivo;
	
	public Server(LinkedList<Esame> esami, LinkedList<Medico> medici) {
		this.esami = esami;
		this.medici = medici;
		
		server_attivo = true;
		
		init();
	}
	
	
	private static int TCP_PORT_FUORI_ORARIO = 3000;
	private ServerSocket server_fuori_orario;
	private ObjectOutputStream pw_fuori_orario;
	private static String servizio_non_disponibile = "service not available";
	
	private void init() {
		
		while( server_attivo ) {
		
			init_prenotazione();
			
			init_annullamento_prenotazione();
			
//			init_direzione();
			
		}
		
		while( !server_attivo ) {
			try {
			server_fuori_orario = new ServerSocket( TCP_PORT_FUORI_ORARIO );
				System.out.println(server_fuori_orario.toString());
				paziente = server_fuori_orario.accept();
				System.out.println(paziente.toString());
				
				pw_fuori_orario = new ObjectOutputStream(paziente.getOutputStream());
				pw_fuori_orario.writeObject( servizio_non_disponibile );
				pw_fuori_orario.flush();
			
			pw_fuori_orario.close();
			paziente.close();
			server_fuori_orario.close();
		
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private static int TCP_PORT_PRENOTAZIONE = 3000;
	private ServerSocket server_prenotazione;
	private Socket paziente;
	private ObjectInputStream br_prenotazione;
	private String esame_richiesto;
	private int codice_esame_richiesto;
	private int matricola_paziente;
	private Medico prenotazione_effettuata;
	private int progressivo_esame;
	private ObjectOutputStream pw_prenotazione;
	private String prenotazione_da_inviare;
	private Prenotazione prenotazione;
	
	private void init_prenotazione() {
		try {
			
			server_prenotazione = new ServerSocket( TCP_PORT_PRENOTAZIONE );
			System.out.println(server_prenotazione.toString());
			paziente = server_prenotazione.accept();
			System.out.println(paziente.toString());
			
			br_prenotazione = new ObjectInputStream(paziente.getInputStream());
			esame_richiesto = (String)br_prenotazione.readObject();
			StringTokenizer st = new StringTokenizer(esame_richiesto);
			String codiceEsame = st.nextToken();
			@SuppressWarnings("unused")
			String vuota = st.nextToken();
			String matricolaPaziente = st.nextToken();
			codice_esame_richiesto = Integer.valueOf(codiceEsame);
			matricola_paziente = Integer.valueOf( matricolaPaziente );
			
			prenotazione_effettuata = prenota( codice_esame_richiesto, matricola_paziente );
			
			/** prenotazione avvenuta correttamente */
			if( !prenotazione_effettuata.pazienteInAttesa() ) {
				
				pw_prenotazione = new ObjectOutputStream(paziente.getOutputStream());
				prenotazione = new Prenotazione(codice_esame_richiesto, matricola_paziente);
				progressivo_esame = prenotazione_effettuata.getPazienti().indexOf(prenotazione) + 1;
				prenotazione_da_inviare = codiceEsame + " " + progressivo_esame + " " + prenotazione_effettuata.getMatricola();
				pw_prenotazione.writeObject(prenotazione_da_inviare);	
				pw_prenotazione.flush();
			}
			else {
				
				pw_prenotazione = new ObjectOutputStream(paziente.getOutputStream());
				prenotazione = new Prenotazione(codice_esame_richiesto, matricola_paziente);
				progressivo_esame = prenotazione_effettuata.getPazienti().size() + 
						prenotazione_effettuata.getPazientiInAttesa().indexOf(prenotazione) + 1;
				prenotazione_da_inviare = codiceEsame + " " + progressivo_esame + " " +  prenotazione_effettuata.getMatricola();
				pw_prenotazione.writeObject(prenotazione_da_inviare);
				pw_prenotazione.flush();
				
			}
			
		}catch(IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Restituisce -1 se la prenotazione non &egrave avvenuta altrimenti restituisce il
	 * progressivo della prenotazione.
	 * @param codice_esame_richiesto
	 * @param matricola_paziente
	 * @return Progressivo prenotazione
	 */
	private Medico prenota(int codice_esame_richiesto, int matricola_paziente) {
		
		for(int i=0;i<esami.size();++i) 
			if( (i+1) == codice_esame_richiesto ) 
				for( int j=0;j<medici.size();++j ) 
					if(medici.get(i).possoAccettareAltriPazienti()) {
						Medico medico = medici.get(i);
						Prenotazione prenotazione = new Prenotazione(codice_esame_richiesto, matricola_paziente);
						medico.aggiungiPaziente( prenotazione );
						return medico;
					}
		Random random = new Random();
		int matricolaMedico = random.nextInt(1, medici.size()+1);
		return new Medico(matricolaMedico);
	}
	
	
	private int PORT_TCP_ANNULLAMENTO_PRENOTAZIONE = 4000;
	private ServerSocket server_annullare_prenotazione;
	private Socket paziente_annullare_prenotazione;
	private ObjectInputStream br_annullamento_prenotazione;
	private String annulla_prenotazione;
	private ObjectOutputStream pw_annullamento_prenotazione;
	private String ack_annullamento_prenotazione;
	
	private void init_annullamento_prenotazione() {
		
		try {
			
			server_annullare_prenotazione = new ServerSocket( PORT_TCP_ANNULLAMENTO_PRENOTAZIONE );
			System.out.println(server_annullare_prenotazione.toString());
			
			paziente_annullare_prenotazione = server_annullare_prenotazione.accept();
			System.out.println(paziente_annullare_prenotazione.toString());
			
			br_annullamento_prenotazione = new ObjectInputStream(paziente_annullare_prenotazione.getInputStream());
			annulla_prenotazione = (String)br_annullamento_prenotazione.readObject();
		
			if( annulla_prenotazione.toUpperCase().contains( "ANNULLARE" ) ||  annulla_prenotazione.toUpperCase().contains( "ANNULLA" ) ) {
				
				boolean prenotazione_annullata = annullare_prenotazione_paziente( annulla_prenotazione );
				
				if( prenotazione_annullata ) {
					
					ack_annullamento_prenotazione = "------- PRENOTAZIONE ANNULLATA -------";
					pw_annullamento_prenotazione = new ObjectOutputStream(paziente_annullare_prenotazione.getOutputStream());
					pw_annullamento_prenotazione.writeObject(ack_annullamento_prenotazione);
					pw_annullamento_prenotazione.flush();
				}
				else {
					
					ack_annullamento_prenotazione = "*-*-*-*-* ERRORE *-*-*-* prenotazione NON annullata";
					pw_annullamento_prenotazione = new ObjectOutputStream(paziente_annullare_prenotazione.getOutputStream());
					pw_annullamento_prenotazione.writeObject(ack_annullamento_prenotazione);
					pw_annullamento_prenotazione.flush();
				}
				
			}
			else {
				
				ack_annullamento_prenotazione = "%%%%%%% ERRORE %%%%%%%";
				pw_annullamento_prenotazione = new ObjectOutputStream(paziente_annullare_prenotazione.getOutputStream());
				pw_annullamento_prenotazione.writeObject(ack_annullamento_prenotazione);
				pw_annullamento_prenotazione.flush();
			}
			
		}catch(IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	private boolean annullare_prenotazione_paziente(String annulla_prenotazione) {
		boolean prenotazione_annullata = false;
		
		StringTokenizer st = new StringTokenizer(annulla_prenotazione);
		String annullare = st.nextToken();
		String vuota = st.nextToken();
		String codice_esame = st.nextToken();
		int codiceEsame = Integer.valueOf(codice_esame);
		
		if(codiceEsame > (esami.size()+1) || codiceEsame < 0)
			return prenotazione_annullata;
		else
			prenotazione_annullata = true;
		
		return prenotazione_annullata;
	}
	
	
	
	
	public static void main(String...strings) {
		
		LinkedList<Esame> esami = new LinkedList<>();
		LinkedList<Medico> medici = new LinkedList<>();
		
		Esame esame1 = new Esame(1);
		Esame esame2 = new Esame(2);
		Esame esame3 = new Esame(3);
		Esame esame4 = new Esame(4);
		esami.add(esame1); esami.add(esame2); esami.add(esame3); esami.add(esame4);
		
		Medico medico1 = new Medico(1*100);
		Medico medico2 = new Medico(2*100);
		medici.add(medico1); medici.add(medico2);
	
		Server server = new Server(esami, medici);
		
	}

}
