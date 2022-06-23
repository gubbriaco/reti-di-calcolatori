package send_receive_object.adv;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import send_receive_object.Studente;

public class ReceiveObjectAdv {
	
	public static void main(String...strings) {
		
		try {
			
			int matricola = 125452;
			
			Socket server = new Socket("localhost", 3575);
			System.out.println(server.toString());
			System.out.println("Server connected: " + server.isConnected());
			
			System.out.println("Sending input objects..");
			ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
			oos.writeObject(matricola);
			
			System.out.println("Reading output objects..");
			ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
			Studente studente = (Studente)ois.readObject();
			System.out.println(studente.toString());
			
			oos.close();
			ois.close();
			server.close();
			System.out.println("Server closed: " + server.isClosed());
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}
