package UDP.time;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class TimeClient {
	
	public static void main(String...strings) throws IOException {
		
		String host = "localhost";
		int port = 3575;
		
		DatagramSocket socket = new DatagramSocket();
		
		/** invia la richiesta */
		byte[] buf = new byte[256];
		InetAddress address = InetAddress.getByName(host);
		
		DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
		socket.send(packet);
		
		
		/** riceve la risposta */
		packet = new DatagramPacket(buf, buf.length);
		socket.receive(packet);
		
		/** visualizza risposta */
		String msg = new String(packet.getData());
		System.out.println("RESPONSE: " + msg);
		
		socket.close();
		
	}
	

}
