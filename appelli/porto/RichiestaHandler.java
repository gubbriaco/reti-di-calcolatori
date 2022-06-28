package porto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

public class RichiestaHandler extends Thread {
	
	private Socket socketTCP;
	private List<Operatore> operatori;
	private List<Banchina> banchine;
	
	private List<Risposta> risposte;
	
	private List<Nave> coda;
	
	private Calendar tempo_richiesta;
	
	public RichiestaHandler(Calendar tempo_richiesta, Socket socketTCP, List<Operatore> operatori, List<Banchina> banchine) {
		this.tempo_richiesta = tempo_richiesta;
		this.socketTCP = socketTCP;
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
	private Random random;
	private final static int MIN_TEMPO_SCARICO = 20, MAX_TEMPO_SCARICO = 50;
	private int tempo_scarico;
	private String ok;
	private Calendar fra5minuti;
	private final static int UDP_PORT = 4000;
	private DatagramSocket socketUDP;
	private byte[] buffer;
	private DatagramPacket packet;
	private final static String HOST_NAME = "localhost";
	private InetAddress address;
	private int tempo_medio_attesa;
	
	@Override public void run() {
		
		synchronized(risposte ) { synchronized(coda) {
			try {
				
				ois = new ObjectInputStream( socketTCP.getInputStream() );
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
					
					fra5minuti = tempo_richiesta;
					fra5minuti.add(Calendar.SECOND, +5);
					while(!trovata_banchina_idonea) {
						
						
						if(tempo_richiesta.equals(fra5minuti)) {
							
							socketUDP = new DatagramSocket(UDP_PORT);
							System.out.println(socketUDP.toString());
							
							buffer = new byte[256];
							tempo_medio_attesa = 120;
							buffer = ("TEMPO MEDIO DI ATTESA: " + String.valueOf(tempo_medio_attesa)).getBytes();
							
							address = InetAddress.getByName(HOST_NAME);
							packet = new DatagramPacket(buffer, buffer.length, address, UDP_PORT);
							socketUDP.send(packet);
					
							/** calcolo nuovamente il tempo */
							tempo_richiesta =  Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"), Locale.ITALY);
							fra5minuti = tempo_richiesta;
							fra5minuti.add(Calendar.SECOND, +5);
						}
						
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
				
				
				oos = new ObjectOutputStream( socketTCP.getOutputStream() );
				risposta = ((LinkedList<Risposta>)risposte).getFirst();
				oos.writeObject(risposta);
				oos.flush();
				
				
				for(int i=0;i<nave.getNr_container_da_scaricare();++i) {
					random = new Random();
					tempo_scarico = random.nextInt(MIN_TEMPO_SCARICO, MAX_TEMPO_SCARICO+1);  
					Thread.sleep(tempo_scarico);
				}
					
				oos = new ObjectOutputStream( socketTCP.getOutputStream() );
				ok = "OK";
				oos.writeObject(ok);
				oos.flush();
				
				/** la banchina sara' di nuovo libera */
				for(int i=0;i<banchine.size();++i)
					if(banchine.get(i).getId() == risposta.getNrBanchina())
						banchine.get(i).rimuoviNave(nave);
					
				
			}catch(IOException | ClassNotFoundException | InterruptedException e) {
				e.printStackTrace();
			}
		}}
	}
	
	
	public void aggiungiNaveInCoda(Nave nave) {
		coda.add(nave);
	}
	
	public void rimuoviNaveInCoda(Nave nave) {
		coda.remove(nave);
	}

}
