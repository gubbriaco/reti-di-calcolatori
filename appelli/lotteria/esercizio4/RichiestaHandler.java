package lotteria.esercizio4;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class RichiestaHandler extends Thread {
	
	private Calendar tempo_richiesta;
	private Socket socket;
	private List<Lotteria> lotterie;
	private List<Biglietto> biglietti;

	private List<Risposta> risposte;
	
	public RichiestaHandler(Calendar tempo_richiesta, Socket socket, List<Lotteria> lotterie, List<Biglietto> biglietti) {
		this.tempo_richiesta = tempo_richiesta;
		this.socket = socket;
		this.lotterie = lotterie;
		this.biglietti = biglietti;
		
		risposte = Collections.synchronizedList(new LinkedList<Risposta>());
	}
	
	public Calendar getTempoRichiesta() {
		return tempo_richiesta;
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public List<Lotteria> getLotterie() {
		return lotterie;
	}
	
	public List<Biglietto> getBiglietti() {
		return biglietti;
	}
	
	public void storeRisposta(Risposta risposta) {
		risposte.add(risposta);
	}
	
	private ObjectInputStream ois;
	private Richiesta richiesta;
	private String richiestaString;
	private String[] richiesta_splitted;
	private String nomeLotteria_richiesto;
	private int nr_biglietti_richiesto;
	private Biglietto biglietto_richiesto;
	private List<Biglietto> biglietti_richiesti;
	private RispostaHandler rh;
	private ObjectOutputStream oos;
	private Risposta risposta;
	
	@Override public void run() {
		
		try {
			
			synchronized(risposte) {
				
				/** accetto le richieste di nuovi biglietti da giocare */
				ois = new ObjectInputStream(socket.getInputStream());
				richiesta = (Richiesta)ois.readObject();
				richiestaString = richiesta.toString();
				richiesta_splitted = richiestaString.split(",");
				nomeLotteria_richiesto = richiesta_splitted[0];
				nr_biglietti_richiesto = Integer.valueOf( richiesta_splitted[1] );
				
				biglietto_richiesto = new Biglietto(nomeLotteria_richiesto);
				
				biglietti_richiesti = Collections.synchronizedList(new LinkedList<>());
				for(int i=0;i<nr_biglietti_richiesto;++i)
					biglietti_richiesti.add(biglietto_richiesto);
				
				
				rh = new RispostaHandler(biglietti_richiesti, this);
				rh.start();
				
				while(rh.isAlive())
					;
			
				
				/** invio i biglietti giocati */
				oos = new ObjectOutputStream(socket.getOutputStream());
				risposta = ((LinkedList<Risposta>)risposte).getFirst();
				oos.writeObject(risposta);
				oos.flush();
				
				for(int i=0;i<biglietti_richiesti.size();++i) {
					Biglietto biglietto_giocato = biglietti_richiesti.get(i);
					Banco.storeBigliettiGiocati(biglietto_giocato);
				}
				
				
			}
			
		}catch(IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	

}
