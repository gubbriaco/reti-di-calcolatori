package MULTICAST.time;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastTimeClient {
	
	
	@SuppressWarnings("deprecation")
	public static void main(String...strings) throws IOException {
		
		int port = 3575;
		MulticastSocket socket = new MulticastSocket(port);
		
		String host = "230.0.0.1";
		InetAddress groupAddress = InetAddress.getByName(host);
		
		socket.joinGroup(groupAddress);
		
		DatagramPacket packet;
		
		for(int i=0;i<100;++i) {
			
			byte[] buf = new byte[256];
			packet = new DatagramPacket(buf, buf.length);
			socket.receive(packet);
			String msg = new String(packet.getData());
			System.out.println("TIME: " + msg);
			
		}
		
		socket.leaveGroup(groupAddress);
		socket.close();
	}
	

}
