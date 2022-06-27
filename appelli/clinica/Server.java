package clinica;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class Server {
	  private final Map<String , Map<String,Integer>> mediciEsami;
	    private final Map<String , Map<String,Integer>> progressivi;
	    private final Map<String , LinkedList<Socket>> listeAttesa;
	    private ServerSocket server;
	    private boolean attivo;
	    @SuppressWarnings("unused")
		private boolean inviato;
	    
	    private final Semaphore mutex;
	    private final ServerRemove serverRemove;
	    
	    public Server(Map<String,Map<String,Integer>> mediciEsami){
	        this.mediciEsami = mediciEsami;
	        this.mutex       = new Semaphore(1,true);
	        this.progressivi = new HashMap<>();
	        this.listeAttesa = new HashMap<>();
	        attivo = true;
	        for(String esame : mediciEsami.keySet()){
	            this.listeAttesa.put(esame,new LinkedList<>());
	        }//for
	        initProgressivi();
	        initServer();
	        this.serverRemove = new ServerRemove(this);
	    }//costruttore
	    
	    private void initProgressivi(){
	        for(String esame : mediciEsami.keySet()){
	            Map<String,Integer> map = new HashMap<>();
	            for(String medico : mediciEsami.get(esame).keySet()){
	                map.put(medico,0);
	            }
	            progressivi.put(esame, map);
	        }//for
	    }//initProgressivi
	    
	    private void initServer(){
	        try{
	            this.server            = new ServerSocket(3000);
	            System.out.println("Server on!");
	            Date date              = new Date();
	            Calendar calendar      = GregorianCalendar.getInstance();
	            calendar.setTime(date);
	            while(attivo){
	                Socket client      = this.server.accept();
	                System.out.println("New Client: "+client.toString());
	                int hour           = calendar.get(Calendar.HOUR_OF_DAY);
	                //controllo orario richiesta
	                if(hour >= 8 && hour <= 12){
	                    client.setSoTimeout(60*1000*60);
	                    @SuppressWarnings("unused")
						ClientHandler c= new ClientHandler(client,this); 
	                }else{
	                    inviaReport();
	                    PrintWriter pw = new PrintWriter(client.getOutputStream(),true);
	                    pw.println("Service Not Available");
	                    client.close();
	                }
	            }//while
	            serverRemove.setAttivo(false);
	        }catch(IOException ex){
	            System.out.println("Error 301 - Errore Apertura Server");
	        }
	    }//initServer
	    
	    public void inviaReport() throws SocketException, IOException{
	        class Statistica {
	            @SuppressWarnings("unused")
				String esame;
	            @SuppressWarnings("unused")
				int    prenotazioni;
	            @SuppressWarnings("unused")
				int    attesa;
	            public Statistica(String esame,int prenotazioni,int attesa){
	                this.esame = esame;
	                this.prenotazioni = prenotazioni;
	                this.attesa = attesa;
	            }
	        }//Statistica
	        for(String esame : mediciEsami.keySet()){
	            int prenotazioni = 0;
	            for(String medico : mediciEsami.get(esame).keySet()){
	                prenotazioni += (10 - mediciEsami.get(esame).get(medico));
	            }
	            int attesa = listeAttesa.get(esame).size();
	            Statistica s = new Statistica(esame,prenotazioni,attesa);
	            DatagramSocket socket = new DatagramSocket(5000);
	            ByteArrayOutputStream output = new ByteArrayOutputStream();
	            ObjectOutputStream out       = new ObjectOutputStream(output);
	            out.writeObject(s);
	            out.flush();
	            byte[] message               = output.toByteArray();
	            DatagramPacket packet        = new DatagramPacket(message,message.length,InetAddress.getByName("direzione.unical.it"),5000);
	            socket.send(packet);
	            socket.close();
	        }//for
	    }//inviaReport
	    
	    public boolean isAttivo()  { return attivo; }//getAttivo
	    public void setAttivo(boolean attivo){ this.attivo = attivo;}//setAttivo
	    public Map<String, Map<String, Integer>> getMediciEsami() {return mediciEsami;}//getMediciEsami
	    public Semaphore getMutex() { return mutex; }//getMutex
	    public Map<String , Map<String,Integer>> getProgressivi() { return progressivi; }//getProgressivi
	    public Map<String , LinkedList<Socket>> getListeAttesa() { return listeAttesa; }//getListeAttesa
	
	    
	   
	    
}

