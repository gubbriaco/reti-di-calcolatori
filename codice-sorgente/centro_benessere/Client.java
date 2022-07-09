package centro_benessere;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

public class Client {
    private final int portGestore=1111;
    private final String hostGestore="gestore.dimes.unical.it";

    public void avvia(Richiesta r){
        try{
            //Invia una richiesta
            Socket socket = new Socket(hostGestore,portGestore);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(r);
            out.close();

            //Attende le offerte alla richiesta precedentemente inviata
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            LinkedList<Offerta> offerte = (LinkedList<Offerta>) in.readObject();
            System.out.println(offerte);
            in.close();
            socket.close();
        }catch(IOException exc){
        }catch(ClassNotFoundException exc){
        }
    }
}
