package centralina.esercizio3;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

public class Centralina extends Thread {
	
	private Socket s1, s2;
	private Richiesta1 r1;
	private Richiesta2 r2;
	public enum TipologiaRichiesta {R1, R2, R3}
	
	private long time;
	
	private static TipologiaRichiesta tr;
	
	public void setTime(long time) {
		this.time = time;
	}
	
	public void set1(Socket s1, Richiesta1 r1) {
		this.s1 = s1;
		this.r1 = r1;
		tr = TipologiaRichiesta.R1;
		time = new Random().nextLong();
	}
	
	public void set2(Socket s2, Richiesta2 r2) {
		this.s2 = s2;
		this.r2 = r2;
		tr = TipologiaRichiesta.R2;
	}
	
	public void set3() {
		tr = TipologiaRichiesta.R3;
	}

	
	
	class Misure extends Thread {
		
		private Socket s;
		private String tipogr;
		private long t1, t2, time;
		
		public enum TipologiaRichiesta {R1, R2, R3}
		private static TipologiaRichiesta tr;
		
		public Misure(Socket s, String tipogr) {
			this.s = s;
			this.tipogr = tipogr;
			tr = TipologiaRichiesta.R1;
		}
		
		public Misure(Socket s, long t1, long t2) {
			this.s = s;
			this.t1 = t1;
			this.t2 = t2;
			tr = TipologiaRichiesta.R2;
		}
		
		public Misure(long time) {
			this.time = time;
			tr = TipologiaRichiesta.R3;
		}
		
		@SuppressWarnings("resource")
		@Override public void run() {
			
			
			try {

				Dato dato;
				ObjectOutputStream oos;
				
				if (Misure.tr == TipologiaRichiesta.R1) {
					
					long time_stamp = System.currentTimeMillis();
					
					Random random = new Random();
					double misura = random.nextDouble();
					dato = new Dato(tipogr, misura, time_stamp);
					
					oos = new ObjectOutputStream(s1.getOutputStream());
					oos.writeObject(dato);
					oos.flush();
					
				}else if(Misure.tr == TipologiaRichiesta.R2) {
					
					Random random = new Random();
					double valore_medio = random.nextDouble(t1, t2);
					dato = new Dato(tipogr, valore_medio);
					
					oos = new ObjectOutputStream(s2.getOutputStream());
					oos.writeObject(dato);
					oos.flush();
					
				} else {
					
					long l = 0;
					Socket socket;
					Random random;
					double m = 0;
					
					while(l < time) {
						random = new Random();
						m += random.nextDouble();
						l++;
					}
					
					socket = new Socket("localhost", 5000);
					oos = new ObjectOutputStream(socket.getOutputStream());
					String misura = m + "";
					oos.writeObject( misura );
					oos.flush();
					
				}
				
				
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	@Override public void run() {
		
		if (Centralina.tr == TipologiaRichiesta.R1) {

			String r1Str = r1.toString();
			String[] r1split = r1Str.split(",");
			String idc1 = r1split[0];
			String tipogr = r1split[1];
			
			Misure m1 = new Misure(s1, tipogr);
			m1.start();

		} else if(Centralina.tr == TipologiaRichiesta.R2) {

			String r2Str = r2.toString();
			String[] r2split = r2Str.split(",");
			String idc2 = r2split[0];
			String from = r2split[1];
			String to = r2split[2];
			
			Misure m2 = new Misure(s2, Long.valueOf(from), Long.valueOf(to));
			m2.start();
			
		} else {
			
			Misure m3 = new Misure(time);
			m3.start();
			
		}
		
		
	}

}
