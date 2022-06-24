package MULTICAST.time;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Date;

public class TimeServer {
	
	public static void main(String...strings) {
		
		MulticastSocket socket = null;
		int port = 3575;
		
		try {
			
			socket = new MulticastSocket(3575);
			
			while(true) {
				
				byte[] buf = new byte[256];
				
				String msg = new Date().toString();
				buf = msg.getBytes();
				
				/** invia il messaggio in broadcast */
				String address = "230.0.0.1";
				InetAddress groupAddress = InetAddress.getByName(address);
				DatagramPacket packet = new DatagramPacket(buf, buf.length, groupAddress, port);
				socket.send(packet);
				
				System.out.println("Broadcasting: " + msg);
				
				Thread.sleep(1000);
			}
			
		}catch(Exception e) {
			socket.close();
			e.printStackTrace();
		}
		
		socket.close();
		
	}

}
