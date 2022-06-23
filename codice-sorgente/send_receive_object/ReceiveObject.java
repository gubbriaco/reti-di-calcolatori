package send_receive_object;

import java.io.ObjectInputStream;
import java.net.Socket;

public class ReceiveObject {
	
	public static void main(String...strings) {
	
		try {
			
			Socket server = new Socket("localhost", 3575);
			
			ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
			
			String beginMsg = (String)ois.readObject();
			System.out.println(beginMsg);
			
			Studente studente1 = (Studente)ois.readObject();
			System.out.println(studente1.toString());
			Studente studente2 = (Studente)ois.readObject();
			System.out.println(studente2.toString());
			Studente studente3 = (Studente)ois.readObject();
			System.out.println(studente3.toString());
			
			String endMsg = (String)ois.readObject();
			System.out.println(endMsg);
			
			server.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}
