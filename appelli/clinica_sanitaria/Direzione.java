package clinica_sanitaria;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Direzione {
	
	
	public static void main(String...strings) {
		
		try {

			int portaStatistiche = 5000;
			DatagramSocket server = new DatagramSocket(portaStatistiche);
			System.out.println(server.toString());
			InetAddress local = InetAddress.getByName("localhost");
			
			while(true) {
				
				byte[] buf = new byte[256];
				DatagramPacket statisticaPacket = new DatagramPacket(buf, buf.length);
				server.receive(statisticaPacket);
				
				System.out.println(statisticaPacket.toString());
				
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	

}
