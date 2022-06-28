package porto;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Random;

public class Nave implements Serializable {

	private static final long serialVersionUID = 931940802335392007L;
	
	private int id, lunghezza, nr_container_da_scaricare;
	
	public Nave(int id, int lunghezza, int nr_container_da_scaricare) {
		this.id = id;
		this.lunghezza = lunghezza;
		this.nr_container_da_scaricare = nr_container_da_scaricare;
		
		init();
	}
	
	
	private Socket socket;
	private final static int TCP_PORT = 3000;
	private Richiesta richiesta;
	private ObjectOutputStream oos;
	
	private void init() {
		try {
			
			socket = new Socket("localhost", TCP_PORT);
			System.out.println(socket.toString());
			richiesta = new Richiesta(this.id, this.lunghezza, this.nr_container_da_scaricare);
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(richiesta);
			oos.flush();
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLunghezza() {
		return lunghezza;
	}

	public void setLunghezza(int lunghezza) {
		this.lunghezza = lunghezza;
	}

	public int getNr_container_da_scaricare() {
		return nr_container_da_scaricare;
	}

	public void setNr_container_da_scaricare(int nr_container_da_scaricare) {
		this.nr_container_da_scaricare = nr_container_da_scaricare;
	}
	
	
	public static void main(String...strings) {
		Random random = new Random();
		int id = random.nextInt();
		int lunghezza_nave = random.nextInt(10, 80);
		int nr_container_da_scaricare = random.nextInt(1, 100);
		Nave nave = new Nave(id, lunghezza_nave, nr_container_da_scaricare);
	}

	
}
