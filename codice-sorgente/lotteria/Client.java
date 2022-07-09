package lotteria;
import java.io.*;
import java.net.*;
import java.util.*;
public class Client {
	
	private final String hostName="lotterie.unical.it";
	private final int lotteriaPort=3000;
	
	@SuppressWarnings("deprecation")
	public void avvia(Lotteria l) {
		
		try {
			Socket socket = new Socket(hostName,lotteriaPort);
			int numBiglietti=new Random().nextInt(10_001);
			String msg=l.getNome()+"-"+numBiglietti;
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			out.write(msg);
			
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			LinkedList<Biglietto> biglietti=(LinkedList<Biglietto>) in.readObject();
			if(biglietti.getFirst().getNumBiglietto()!=0) {
				MulticastSocket socketM=new MulticastSocket();
				socketM.joinGroup(InetAddress.getByName("230.0.0.1"));
				byte[] buf=new byte[256];
				DatagramPacket packet = new DatagramPacket(buf,buf.length);
				socketM.receive(packet);
				System.out.println(new String(packet.getData()));
				socketM.close();
			}
			
			socket.close();
		}catch(IOException exc) {
		}catch(ClassNotFoundException exc) {
		}
		
	}
}
