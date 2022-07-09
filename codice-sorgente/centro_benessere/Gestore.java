package centro_benessere;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.concurrent.TimeoutException;

public class Gestore {
	
    private final String myHostName;
    private final int portRichieste=1111;
    private final int portMul=2222;
    private final String groupIP="224.3.2.1";
    private final int portOfferte=3333;
    
    public Gestore(String myHostName){
        this.myHostName=myHostName;
    }
    public void avvia(){
        try{
            ServerSocket server = new ServerSocket(portRichieste);
            while(true){
                Socket socket = server.accept();
                new HandlerRichieste(socket).start();
            }
        }catch(IOException exc){
        }
    }

    class HandlerRichieste extends Thread{
        private Socket socket;
        private LinkedList<Offerta> offerte = new LinkedList<>();

        public HandlerRichieste(Socket socket){
            this.socket=socket;
        }

        @Override
        public void run(){
            try{
                //Attende una richiesta da un cliente
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                Richiesta r = (Richiesta) in.readObject();

                //Invia in broadcasr la richiesta ai centribenessere
                MulticastSocket socketMul = new MulticastSocket(portMul);
                byte[] buf = r.toString().getBytes();
                InetAddress group= InetAddress.getByName(groupIP);
                DatagramPacket packet = new DatagramPacket(buf,buf.length,group,2222);
                socketMul.send(packet);

                //Attiva un serverSocket per accettare le richieste che gli pervengono
                ServerSocket server = new ServerSocket(portOfferte);
                server.setSoTimeout(60_000);
                while(true) {
                    try {
                        Socket socketOfferte = server.accept();
                        in = new ObjectInputStream(socketOfferte.getInputStream());
                        Offerta offerta = (Offerta) in.readObject();
                        offerte.add(offerta);
                    } catch (InterruptedIOException exc) {
                        //Tempo scaduto
                        break;
                    }
                }

                //Manda le richieste
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(offerte);
                out.close();
                socket.close();
            }catch(IOException exc){
            }catch(ClassNotFoundException exc){}
        }
    }
}
