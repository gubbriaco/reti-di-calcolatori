package send_receive_object.adv;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

import send_receive_object.Studente;

public class SendObjectAdv {
	
	public static void main(String...strings) {
		
		LinkedList<Studente> studenti = new LinkedList<>();
		
		Studente studente1 = new Studente(140928, "Fabio", "Rossi", "Ingegneria Informatica");
		Studente studente2 = new Studente(111637, "Paolo", "Neri", "Ingegneria Elettronica");
		Studente studente3 = new Studente(152316, "Francesco", "Bianchi", "Ingegneria Gestionale");
		Studente studente4 = new Studente(125452, "Alessando", "Tiano", "Ingegneria Alimentare");
		Studente studente5 = new Studente(111637, "Matteo", "Positano", "Ingegneria Elettronica");
		Studente studente6 = new Studente(135432, "Roberto", "Russo", "Robotics and Automation Engineering");
		Studente studente7 = new Studente(146344, "Diego", "Ercolano", "Ingegneria Informatica");
		Studente studente8 = new Studente(124535, "Paolo", "Gersitano", "Ingegneria Meccanica");
		Studente studente9 = new Studente(173424, "Luca", "Rigola", "Ingegneria Gestionale");
		
		studenti.add(studente1); studenti.add(studente2); studenti.add(studente3);
		studenti.add(studente4); studenti.add(studente5); studenti.add(studente6);
		studenti.add(studente7); studenti.add(studente8); studenti.add(studente9);
		
		try {
			
			ServerSocket server = new ServerSocket(3575);
			System.out.println(server.toString());
			Socket client = server.accept();
			System.out.println(client.toString());
			System.out.println("Client connected: " + client.isConnected());
			
			ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
			int matricola = (int)ois.readObject();
			
			ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
			for(int i=0;i<studenti.size();++i)
				if(studenti.get(i).getMatricola()==matricola)
					oos.writeObject(studenti.get(i));
			
			
			oos.close();
			client.close();
			System.out.println("Client connected: " + client.isConnected());
			server.close();
			System.out.println("Server closed: " + server.isClosed());
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	

}
