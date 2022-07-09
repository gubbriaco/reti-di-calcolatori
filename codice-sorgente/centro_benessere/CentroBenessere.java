package centro_benessere;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Random;

public class CentroBenessere {
    private String hostName;
    private String gropuIP="224.3.2.1";
    private final String hostNameGestore="gestore.dimes.unical.it";
    private final int portGestore=3333;

    public CentroBenessere(String hostName){
        this.hostName=hostName;
    }

    public void avvia(){
        try{
            MulticastSocket socketMul = new MulticastSocket(2222);
            InetAddress group=InetAddress.getByName(gropuIP);
            socketMul.joinGroup(group);
            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf,buf.length);
            socketMul.receive(packet);

            String[] richiesta = new String(packet.getData()).split("-");
            int numPersone=Integer.parseInt(richiesta[1]);

            System.out.println(richiesta);
            Random r = new Random();
            if(r.nextDouble(1)>0.3){
                Offerta offerta = new Offerta(hostName, r.nextInt(50,151)*numPersone);
                Socket socket = new Socket(hostNameGestore,portGestore);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(offerta);
                socket.close();
            }

        }catch(IOException exc){
        }
    }
}
