package portook;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class RichiestaHandler extends Thread {
	
	private Socket socket;
	private List<Operatore> operatori;
	private List<Banchina> banchine;
	
	private List<Risposta> risposte;
	
	private List<Nave> coda;
	
	public RichiestaHandler(Socket socket, List<Operatore> operatori, List<Banchina> banchine) {
		this.socket = socket;
		this.operatori = operatori;
		this.banchine = banchine;
		
		risposte = Collections.synchronizedList(new LinkedList<Risposta>());
		
		coda = Collections.synchronizedList(new LinkedList<Nave>());
	}
	
	
	public List<Operatore> getOperatori() {
		return operatori;
	}
	
	public synchronized List<Banchina> getBanchine() {
		return banchine;
	}
	
	public void storeRisposta(Risposta risposta) {
		risposte.add(risposta);
	}
	
	
	private ObjectInputStream ois;
	private Richiesta richiesta;
	private String richiesta_string;
	private String[] richiesta_split;
	private int id_nave, lunghezza_nave, nr_container_da_scaricare;
	private RispostaHandler bh;
	private Nave nave;
	private ObjectOutputStream oos;
	private Risposta risposta;
	
	@Override public void run() {
		
		try {
			
			ois = new ObjectInputStream( socket.getInputStream() );
			richiesta = (Richiesta)ois.readObject();
			richiesta_string = richiesta.toString();
			richiesta_split = richiesta_string.split(",");
			id_nave = Integer.parseInt(richiesta_split[0]);
			lunghezza_nave = Integer.parseInt(richiesta_split[1]);
			nr_container_da_scaricare = Integer.parseInt(richiesta_split[2]);
			
			nave = new Nave(id_nave, lunghezza_nave, nr_container_da_scaricare);
			
			bh = new RispostaHandler( nave, this);
			bh.start();
			
			/** deve terminare la sua esecuzione cosi' da prelevare la risposta 
			 *  dalla struttura dati {@link RichiestaHandler@risposte} */
			while( bh.isAlive() )
				;
			
			/** controllo se la richiesta e' stata gestita oppure e' stata messa in 
			 *  attesa a causa di banchine non libere */
			if( !coda.isEmpty() ) {
				@SuppressWarnings("unused")
				Banchina banchina_idonea;
				boolean trovata_banchina_idonea = false;
				while(!trovata_banchina_idonea) {
					for( int i=0;i<banchine.size();++i ) {
						Banchina banchina = banchine.get(i);
						if(nave.getLunghezza() <= 40) {
							if( !banchina.getPiu40() && !banchina.getIsBusy() ) {
								banchina.aggiungiNave(nave);
								banchina_idonea = banchina;
								trovata_banchina_idonea = true;
								break;
							}
						}else {
							if( banchina.getPiu40() && !banchina.getIsBusy() ) {
								banchina.aggiungiNave(nave);
								banchina_idonea = banchina;
								trovata_banchina_idonea = true;
								break;
							}
						}
					}
					
					if( trovata_banchina_idonea )
						break;
				}
			}
			
			oos = new ObjectOutputStream( socket.getOutputStream() );
			risposta = ((LinkedList<Risposta>)risposte).getFirst();
			oos.writeObject(risposta);
			oos.flush();
			
		}catch(IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void aggiungiNaveInCoda(Nave nave) {
		coda.add(nave);
	}
	
	public void rimuoviNaveInCoda(Nave nave) {
		coda.remove(nave);
	}

}
