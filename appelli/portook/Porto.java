package portook;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class Porto {
	
	private static final int TCP_PORT = 3000;
	
	private List<Operatore> operatori;
	private List<Banchina> banchine;
	
	
	public Porto(List<Operatore> operatori, List<Banchina> banchine) {
		this.operatori = operatori;
		this.banchine = banchine;
	}
	
	public List<Operatore> getOperatori(){
		return operatori;
	}
	
	public List<Banchina> getBanchine() {
		return banchine;
	}
	
	
	
	public static void main(String...strings) {
		
		List<Operatore> operatori = new LinkedList<Operatore>();
		/** deve essere synchronized poiche' le banchine verranno modificate di volta in volta 
		 *  essendo che possono essere occupate da diverse navi nel tempo */
		List<Banchina> banchine = Collections.synchronizedList(new LinkedList<Banchina>());
		
		Porto porto = new Porto(operatori, banchine);
		
		ServerSocket server;
		Socket nave;
		RichiestaHandler rh;
		Calendar tempo_richiesta;
		try {
			
			server = new ServerSocket(TCP_PORT);
			System.out.println(server.toString());
			
			while(true) {
				
				nave = server.accept();
				System.out.println(nave.toString());
				tempo_richiesta = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"), Locale.ITALY);
				rh = new RichiestaHandler( tempo_richiesta, nave, porto.getOperatori(), porto.getBanchine() );
				rh.start();
			}
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}

}
