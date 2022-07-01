package centralina.esercizio3;

import java.net.Socket;
import java.util.List;
import java.util.Random;

public class RichiestaHandler extends Thread {
	
	private Socket s1, s2, s3;
	private Richiesta1 r1;
	private Richiesta2 r2;
	private Richiesta3 r3;
	public enum TipologiaRichiesta {R1, R2, R3}
	

	private static TipologiaRichiesta tr;
	
	public RichiestaHandler(Socket s1, Richiesta1 r1) {
		this.r1 = r1;
		this.s1 = s1;
		tr = TipologiaRichiesta.R1;
	}

	public RichiestaHandler(Socket s2, Richiesta2 r2) {
		this.r2 = r2;
		this.s2 = s2;
		tr = TipologiaRichiesta.R2;
	}
	
	public RichiestaHandler(Socket s3, Richiesta3 r3) {
		this.s3 = s3;
		this.r3 = r3;
		tr = TipologiaRichiesta.R3;
	}
	
	@Override public void run() {
		
		if (RichiestaHandler.tr == TipologiaRichiesta.R1) {
			
			String r1Str = r1.toString();
			String[] r1split = r1Str.split(",");
			String idc1 = r1split[0];

			List<Centralina> centraline = Server.getCentraline();
			for(int i=0;i<centraline.size();++i)
				if(centraline.get(i).getId() == Integer.valueOf(idc1)) {
					centraline.get(i).set1(s1, r1);
					centraline.get(i).start();
				}

		} else if(RichiestaHandler.tr == TipologiaRichiesta.R2) {
			
			String r2Str = r2.toString();
			String[] r2split = r2Str.split(",");
			String idc2 = r2split[0];
			
			List<Centralina> centraline = Server.getCentraline();
			for(int i=0;i<centraline.size();++i)
				if(centraline.get(i).getId() == Integer.valueOf(idc2)) {
					centraline.get(i).set2(s2, r2);
					centraline.get(i).start();
				}
			
		} else {
			
			boolean giaPresente = false;
			List<Centralina> centraline = Server.getCentraline();
			for(int i=0;i<centraline.size();++i)
				if(centraline.get(i).getId() == r3.getId())
					giaPresente = true;
			
			if(!giaPresente) {
				
				Centralina c = new Centralina();
				c.set3();
				c.setTime(new Random().nextLong());
				centraline.add(c);
				
				c.start();
				
			}
			
		}
		
		
		
	}
	
}
