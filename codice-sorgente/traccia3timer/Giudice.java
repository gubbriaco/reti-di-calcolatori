package traccia3timer;

import traccia3.Offerta;
import traccia3.Richiesta;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

public class Giudice {

    private final static int rPort = 2000;
    private final static String gAddress = "230.0.0.1";
    private final static int gPort = 3000;
    private final static int oPort = 4000;
    private final static int n = 1;

    public static void main(String[] args) {
        try {
            // Creo il grouppo del multicast socket
            InetAddress group = InetAddress.getByName(gAddress);

            // Fase 1: ricevi richiesta
            ServerSocket server = new ServerSocket(rPort);
            Socket accettaRichiesta = server.accept();
            ObjectInputStream ois = new ObjectInputStream(accettaRichiesta.getInputStream());
            Richiesta richiesta = (Richiesta) ois.readObject();
            System.out.println("Ricevuta: " + richiesta);

            // Fase 2: invia richiesta ai partecipanti
            inviaRichiestaAiPartecipanti(richiesta, group);

            // Fase 3: ricevi offerte dai partecipanti
            Offerta oVincente = riceviOfferte();
            System.out.println("Offerta vincente: " + oVincente);

            // Fase 4: invia esito gara
            ObjectOutputStream oos = new ObjectOutputStream(accettaRichiesta.getOutputStream());
            oos.writeObject(oVincente);
            System.out.println("Inviata Offerta vincente all'ente");
            inviaEsitoGara(oVincente, group);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private static void inviaRichiestaAiPartecipanti(Richiesta richiesta, InetAddress group) {
        try {
            MulticastSocket msocket = new MulticastSocket(gPort);
            String r = richiesta.getDescrizione() + " - " + richiesta.getImportoMassimo();
            byte buf[] = r.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, group, gPort);
            msocket.send(packet);
            System.out.println("Inviata richiesta in multicast: " + richiesta);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static Offerta riceviOfferte() {
        Offerta oVincente = null;

        try {

            ServerSocket server = new ServerSocket(oPort);
            server.setSoTimeout(60000);
            while(true) {
                Socket partecipante = server.accept();
                ObjectInputStream ois = new ObjectInputStream(partecipante.getInputStream());
                Offerta offerta = (Offerta) ois.readObject();

                if (oVincente == null)
                    oVincente = offerta;
                else if (offerta.getImportoRichiesto() <= oVincente.getImportoRichiesto()
                        || (offerta.getImportoRichiesto() == oVincente.getImportoRichiesto() && offerta.getId() < oVincente.getId()))
                    oVincente = offerta;

                partecipante.close();
            }// while

        } catch (SocketTimeoutException ste) {
            System.out.println("Tempo offerte scaduto!");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return oVincente;

    }

    private static void inviaEsitoGara(Offerta oVincente, InetAddress group) {
        try {
            MulticastSocket mSocket = new MulticastSocket(gPort);
            String message = oVincente.getId() + "-" + oVincente.getImportoRichiesto();
            byte[] buf = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, group, gPort);
            mSocket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
