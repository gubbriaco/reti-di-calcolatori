package Giugno14_2022;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;

public class CandidaturaHandler extends Thread {
	
	private Socket socket_candidatura;
	private Server s;
	
	public CandidaturaHandler(Socket socket_candidatura, Server s) {
		this.socket_candidatura = socket_candidatura;
		this.s = s;
	}
	
	@Override public void run() {
		try {
			
			ObjectInputStream ois = new ObjectInputStream(socket_candidatura.getInputStream());
			Candidatura candidatura = (Candidatura)ois.readObject();
			
			List<Offerta> offerte = s.getOfferte();
			Offerta offerta = null;
			for(int i=0;i<offerte.size();++i)
				if(offerte.get(i).getIdNumerico() == candidatura.getId())
					offerta = offerte.get(i);
			
			if(offerta.isActive()) {
				
				InvioCandidaturaHandler ich = new InvioCandidaturaHandler(offerta, candidatura, s);
				ich.start();
				
			}else {
				
			}
			
		}catch(IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
