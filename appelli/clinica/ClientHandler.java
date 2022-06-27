package clinica;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler extends Thread{
    
    private final Socket socket;
    private final Server server;
    
    public ClientHandler(Socket socket,Server server){
        this.socket = socket;
        this.server = server;
        start();
    }//costruttore
    
    @Override
    public void run(){
        try{
            PrintWriter    writer      = new PrintWriter(socket.getOutputStream(),true);
            BufferedReader reader      = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String esame               = reader.readLine();
            this.server.getMutex().acquire();
            Map<String,Integer> medici = this.server.getMediciEsami().get(esame);
            String medicoDisponibile   = null;
            for(String medico : medici.keySet()){
                if(medici.get(medico) > 0){
                    medicoDisponibile = medico;
                    break;
                }
            }//for
            if(medicoDisponibile != null){
                //posto disponibile
                this.server.getMediciEsami().get(esame).put(medicoDisponibile, server.getMediciEsami().get(esame).get(medicoDisponibile) - 1);
                int progressivo = server.getProgressivi().get(esame).get(medicoDisponibile);
                server.getProgressivi().get(esame).put(medicoDisponibile, progressivo + 1);
                String message  = esame+"/"+progressivo+"/"+medicoDisponibile;
                writer.println(message);
            }else{
                this.server.getListeAttesa().get(esame).addLast(socket);
            }
            this.server.getMutex().release();
        }catch(IOException ex){
            System.out.println("Error 302 - Errore nella Ricezione Dei Dati");
        } catch (InterruptedException ex) {
            System.out.println("Error 303 - Mutex Acquire");
        }
    }//run
    
    
//    public static void main(String...strings) throws IOException {
//    	Map<String, Integer> medici = new HashMap<>();
//    	medici.put("Medico1", 0);
//    	medici.put("Medico2", 0);
//    	Map<String,Map<String,Integer>> mediciEsami = new HashMap<>();
//    	mediciEsami.put("Esame1", medici);
//    	mediciEsami.put("Esame2", medici);
//    	Server server = new Server(mediciEsami);
//    	ServerSocket paziente = new ServerSocket(3000);
//    	Socket clinica = paziente.accept();
//    	ClientHandler client = new ClientHandler(clinica, server);
//    }
    
}//ClientHandler
