package clinica;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

/**
 *
 * @author Nicola Corea
 */
public class ServerRemove extends Thread{
    
    private final int TCP_PORT = 4000;
    private ServerSocket serverSocket;
    private final Server       server;
    
    private boolean attivo;
    
    public ServerRemove(Server server){
        this.server = server;
        this.attivo = true;
        start();
        initServer();
    }//costruttore
    
    private void initServer(){
        try{
            this.serverSocket         = new ServerSocket(TCP_PORT);
            while(attivo){
                Socket client         =  serverSocket.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String prenotazione[] = reader.readLine().split("/");
                this.server.getMutex().acquire();
                client                = this.server.getListeAttesa().get(prenotazione[0]).removeFirst();
                PrintWriter writer    = new PrintWriter(client.getOutputStream(),true);
                writer.println(Arrays.toString(prenotazione));
                this.server.getMutex().release();
                client.close();
            }//while
        }catch(IOException ex){
            System.out.println("Error 304 - ServerRemove");
        }catch(InterruptedException ex1){
            System.out.println("Error 305 - IE ServerRemove");
        }
    }//initServer
    
    public boolean isAttivo()  { return attivo; }//isAttivo
    public void setAttivo(boolean attivo) { this.attivo = attivo; }//setAttivo
    
}//ServerRemove