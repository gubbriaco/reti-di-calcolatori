package traccia3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Ente {
    public static void main(String[] args) {
        String GIUDICE_ADDRESS = "127.0.0.1";
        int GIUDICE_PORT = 2000;
        try {
            // Invio richiesta gara di appalto
            Socket socket = new Socket(GIUDICE_ADDRESS, GIUDICE_PORT);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(new Richiesta("Costruzione Stadio Arcavacata",100000));

            // Mi metto in attesa dell'offerta migliore
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Offerta offertaMigliore = (Offerta) in.readObject();
            System.out.println("Ricevuta offerta migliore: " + offertaMigliore);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
