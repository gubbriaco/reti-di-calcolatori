package traccia3multi_sem;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

public class Ente extends Thread {

    private static String GIUDICE_ADDRESS = "127.0.0.1";
    private static int GIUDICE_PORT = 2000, countOpere = 0;
    private int idEnte;

    public Ente(int idEnte) {
        this.idEnte = idEnte;
    }

    public void run() {
        try {
            sleep(new Random().nextInt(60000));
            // Invio richiesta gara di appalto
            Socket socket = new Socket(GIUDICE_ADDRESS, GIUDICE_PORT);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            Richiesta richiesta = new Richiesta(idEnte, "Opera " + (countOpere++), 100000);
            out.writeObject(richiesta);
            System.out.println("Inviata " + richiesta);

            // Mi metto in attesa dell'offerta migliore
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Offerta offertaMigliore = (Offerta) in.readObject();
            System.out.println("Ricevuta offerta migliore: " + offertaMigliore);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++)
            new Ente(i).start();
    }
}
