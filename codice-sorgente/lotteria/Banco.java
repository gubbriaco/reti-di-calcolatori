package lotteria;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Banco{
	
	private Map<String,LinkedList<Biglietto>> lotteriaEBiglietti;
	private LinkedList<Lotteria> lotterie;
	
	public Banco(LinkedList<Lotteria> lotterie) {
		lotteriaEBiglietti=Collections.synchronizedMap(new HashMap<String,LinkedList<Biglietto>>());
		for(Lotteria l : lotterie) {
			lotteriaEBiglietti.put(l.getNome(), l.getBiglietti());
		}
		this.lotterie=new LinkedList<>(lotterie);
	}//Banco
	
	public void avviaLotteria(Lotteria l) {
		new HandlerTimer(l).start();
	}
	
	public void avvia() {
		new HandlerRichieste().start();
	}//avvia
	
	

	class HandlerTimer extends Thread{
		private Lotteria l;
		public HandlerTimer(Lotteria l) {
			this.l=l;
		}
		public void run() {
			try {
				l.setStato(true);
				TimeUnit.MINUTES.sleep(60);
				l.setStato(false);
			}catch(InterruptedException exc) {
			}
		}
	}//HandlerTimer
	
	
	class HandlerRichieste extends Thread{
		@Override
		public void run() {
			try {
				ServerSocket server = new ServerSocket(3000);
				while(true) {
					Socket socket = server.accept();
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String richiesta=in.readLine();
					in.close();
					
					String[] infoRichiesta=richiesta.split("-");
					Lotteria lotteria=ottieniLotteria(infoRichiesta[0]);
					int numBigliettiRichiesti=Integer.parseInt(infoRichiesta[1]);
					LinkedList<Biglietto> bigliettiDaInviare= new LinkedList<>();
					LinkedList<Biglietto> bigliettiDisponibili=lotteriaEBiglietti.get(infoRichiesta[0]);
					if(lotteria.isOpen()) {
						//Lotteria aperta ed è possibile acquistare biglietti
						if(bigliettiDisponibili.size()>=numBigliettiRichiesti) {
							//Ci sono abbastanza biglietti disponibili
							while(numBigliettiRichiesti>0) {
								bigliettiDaInviare.add(bigliettiDisponibili.removeFirst());
								numBigliettiRichiesti--;
							}
							ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
							out.writeObject(bigliettiDaInviare);
							out.close();
						}else {
							//Non ci sono abbastanza biglietti
							bigliettiDaInviare.add(new Biglietto(infoRichiesta[0],0));
						}
					}else {//Lotteria chiusa
						bigliettiDaInviare.add(new Biglietto(infoRichiesta[0],0));
						int numeroVincente=new Random().nextInt(1,10_001);
						MulticastSocket socketMul=new MulticastSocket();
						byte[] buf=(infoRichiesta[0]+"-"+numeroVincente).getBytes();
						InetAddress group=InetAddress.getByName("230.0.0.1");
						DatagramPacket packet = new DatagramPacket(buf,buf.length,group,4000);
						socketMul.send(packet);
						socketMul.close();
					}
				}
			}catch(IOException exc) {
			}
		}
		
		private Lotteria ottieniLotteria(String nomeLotteria) {
			for(Lotteria l : lotterie) {
				if(l.getNome().equals(nomeLotteria))
					return l;
			}
			return null;
		}
		
	}//HandlerRichiesta
	
	
	
}
