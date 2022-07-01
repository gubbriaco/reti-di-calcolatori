package centralina.esercizio3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

public class Client {
	
	public Client() {
		
	}
	
	
	public static void main(String...strings) {
		
		Client c = new Client();
		Socket s1, s2;
		ObjectOutputStream oos;
		ObjectInputStream ois;
		Dato dato;
		
		try {
			
			s1 = new Socket("localhost", 3000);
			System.out.println(s1.toString());
			oos = new ObjectOutputStream(s1.getOutputStream());
			Richiesta1 r1 = new Richiesta1(new Random().nextInt(1, 10), "temperatura");
			oos.writeObject(r1.toString());
			oos.flush();
			
			s2 = new Socket("localhost", 4000);
			System.out.println(s2.toString());
			oos = new ObjectOutputStream(s2.getOutputStream());
			Richiesta2 r2 = new Richiesta2(new Random().nextInt(1, 10), 1345, 2352);
			oos.writeObject(r2.toString());
			oos.flush();
			
			
			ois = new ObjectInputStream(s1.getInputStream());
			dato = (Dato)ois.readObject();
			
			
			ois = new ObjectInputStream(s2.getInputStream());
			dato = (Dato)ois.readObject();
			
		}catch(IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
	}

}
