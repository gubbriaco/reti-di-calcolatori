package calcolo_distribuito;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Gestore {
	
	private final static int TCP_PORT_RISORSA = 3000,
							 TCP_PORT_RICHIESTA = 2000;
	
	private Map<OffertaRisorsa, String> risorse;
	
	public Gestore(Map<OffertaRisorsa, String> risorse) {
		this.risorse = risorse;
	}
	
	public void aggiungiOfferta(OffertaRisorsa or, String address) {
		risorse.put(or, address);
	}
	
	public Map<OffertaRisorsa, String> getRisorseOfferte() {
		return risorse;
	}
	
	
	public static void main(String...strings) {
		Map<OffertaRisorsa, String> risorse = Collections.synchronizedMap(
											  new HashMap<OffertaRisorsa,String>());
		Gestore gestore = new Gestore(risorse);
		
		try {
			
			synchronized(gestore.risorse) {
			
				ServerSocket server1 = new ServerSocket(Gestore.TCP_PORT_RISORSA);
				System.out.println(server1.toString());
				server1.setSoTimeout(10000);
				while(true) {
					try {
						
						Socket socket1 = server1.accept();
						System.out.println(socket1.toString());
						
						OffertaHandler rh1 = new OffertaHandler(socket1, gestore);
						rh1.start();
						
					}catch(SocketTimeoutException e) {
						break;
					}
				}
				
				ServerSocket server2 = new ServerSocket(Gestore.TCP_PORT_RICHIESTA);
				System.out.println(server2.toString());
				server2.setSoTimeout(250000);
				while(true) {
					try {
						
						Socket socket2 = server2.accept();
						System.out.println(socket2.toString());

						RichiestaHandler rh2 = new RichiestaHandler(socket2, gestore);
						rh2.start();
						
					}catch(SocketTimeoutException e) {
						break;
					}
				}
				
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
