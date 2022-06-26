package porto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Operatore extends Thread {
	
	private Socket socket;
    private final Porto  porto;
    
    public Operatore(Socket socket,Porto porto){
        this.socket = socket;
        this.porto  = porto;
    }
    
    @Override
    public void run(){
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String messageNave    = reader.readLine();
            String id             = messageNave.substring(0,4); //supponiamo i primi 4 caratteri contengano id
            int    lunghezza      = Integer.parseInt(messageNave.substring(4, 6)); //2 numeri per la lunghezza
            int    nContainer     = Integer.parseInt(messageNave.substring(6,messageNave.length())); //numero container
            int    nBanchina      = porto.requestBanchina(lunghezza);
            System.out.println("ID: "+id+" Lunghezza: "+lunghezza+" nContainer: "+nContainer);
            PrintWriter print     = new PrintWriter(socket.getOutputStream(),true);
            //comunica alla nave il numero della banchina
            print.println(nBanchina);
            //comunica all'operatore l'arrivod della nave
            InetAddress address   = porto.getOperatore(nBanchina);
            socket                = new Socket(address,4000);
            print                 = new PrintWriter(socket.getOutputStream(),true);
            String message        = id+","+nContainer;
            print.println(message);
            //attendi la fine dello scarico
            reader                = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            reader.readLine();
            //rilascia banchina
            porto.releaseBanchina(lunghezza,nBanchina);
            socket.close();
        }catch(IOException ex){
            System.out.println("Error 302 - I/O Exception NaveHandler --> run");
        }
    }
	
}
