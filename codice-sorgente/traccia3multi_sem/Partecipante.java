package traccia3multi_sem;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.Random;

public class Partecipante extends Thread {

    private int id;
    private final String GIUDICE_ADDRESS = "127.0.0.1";
    private final int gPort = 3000;

    public Partecipante(int id) {
        this.id = id;
    }

    private void gestisciRichiesta(String[] richiestaParts) throws IOException, InterruptedException {
        int idGara = Integer.parseInt(richiestaParts[1]);
        int importoMax = Integer.parseInt(richiestaParts[3].trim());

        sleep(new Random().nextInt(60000));
        //Rispondo al Giudice
        Socket socket = new Socket(GIUDICE_ADDRESS, 4000);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

        Offerta offerta = new Offerta(this.id, idGara, new Random().nextInt(importoMax));
        out.writeObject(offerta);
        System.out.println("Inviata offerta: " + offerta);
        socket.close();
    }


    @Override
    public void run() {
        System.out.println("Avviato partecipante con ID " + this.id);
        MulticastSocket mSocket = null;
        try {
            // Ricevo richiesta offerta
            mSocket = new MulticastSocket(gPort);
            InetAddress group = InetAddress.getByName("230.0.0.1");
            mSocket.joinGroup(group);
            while (true) {
                byte[] buf = new byte[512];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                mSocket.receive(packet);

                String richiesta = new String(packet.getData());
                String[] richiestaParts = richiesta.split("-");
                System.out.println("Ricevuto pacchetto multicast: " + richiesta);
                if (richiestaParts[0].equals("RICHIESTA")) {
                    gestisciRichiesta(richiestaParts);
                } else System.out.println(richiesta);  // Esito offerte precedenti
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int n = 10;
        for (int i = 0; i < n; i++) {
            new Partecipante(i).start();
        }
    }
}
