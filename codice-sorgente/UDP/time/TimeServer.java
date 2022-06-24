package UDP.time;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;

public class TimeServer {
	
	
	public static void main(String...strings) {
		
		DatagramSocket socket = null;
		
		try {
			
			socket = new DatagramSocket(3575);
			System.out.println(socket.toString());
			
			int n=1;
			
			while(n<=10) {
				
				byte[] buf = new byte[256];
				
				/** riceve la richiesta */
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);
				
				/** produce la risposta */
				String dString = new Date().toString();
				buf = dString.getBytes();
				
				
				/** invia la risposta al client */
				InetAddress address = packet.getAddress();
				int port = packet.getPort();
				
				packet = new DatagramPacket(buf, buf.length, address, port);
				System.out.println(packet.toString());
				System.out.println("Sending packet..");
				socket.send(packet);
				n++;
				
			}
			
			socket.close();
			System.out.println("******* Socket closed: " + socket.isClosed() + " *******");
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	

}
