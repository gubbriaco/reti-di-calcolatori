package send_receive_object;

import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SendObject {
	
	
	public static void main(String...strings) {
		
		try {
			
			ServerSocket server = new ServerSocket(3575);
			
			Socket client = server.accept();
			
			System.out.println(server.toString());
			System.out.println(client.toString());
			
			System.out.println("Client connected: " + client.isConnected());
			
			ObjectOutputStream oos = new ObjectOutputStream( client.getOutputStream() );
			oos.writeObject("*** WELCOME ***");
			
			Studente studente1 = new Studente(140928, "Fabio", "Rossi", "Ingegneria Informatica");
			Studente studente2 = new Studente(111637, "Paolo", "Neri", "Ingegneria Elettronica");
			Studente studente3 = new Studente(152316, "Francesco", "Bianchi", "Ingegneria Gestionale");
			
			oos.writeObject(studente1);
			oos.writeObject(studente2);
			oos.writeObject(studente3);
			
			oos.writeObject("*** GOODBYE ***");
			
			
			System.out.println("Sending objects..");
			
			oos.close();
			client.close();

			System.out.println("Client closed: " + client.isClosed());
			server.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	

}
