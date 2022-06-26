package porto;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class Porto {
	
    private final int TCP_PORT = 3000; //porta richiesta navi
    private final boolean[] banchineMax40;   //banchine navi lunghezza <= 40
    private final boolean[] banchineSup40;   //banchine navi lunghezza >  40
    
    private final Map<Integer,InetAddress> operatori;
    
    private final Semaphore bMax40, bSup40,mutex,mutex1;
    private ServerSocket serverPorto;
    private boolean attivo;
    
    
    public static void main(String...strings) {
    	int numeroNavi = 10;
    	Nave[] navi = new Nave[numeroNavi];
    	for(int i=0;i<numeroNavi;++i)
    		navi[i] = new Nave(i+1);
    		
    	Porto porto = new Porto(new HashMap<Integer,InetAddress>());
    }
    
    
    public Porto(Map<Integer,InetAddress> operatori){
        this.operatori     = operatori;
        this.banchineMax40 = new boolean[5];
        this.banchineSup40 = new boolean[5];
        this.bMax40        = new Semaphore(5,true);
        this.bSup40        = new Semaphore(5,true);
        this.mutex         = new Semaphore(1,true);
        this.mutex1        = new Semaphore(1,true);
        this.attivo        = true;
        initServer();
    }
    
    private void initServer(){
        try{
            this.serverPorto         = new ServerSocket(TCP_PORT);
            System.out.println("In Attesa Di Navi...");
            while(attivo){
                Socket nave          = serverPorto.accept();
                System.out.println("Nave Al Porto: "+nave.toString());
                Operatore handler  = new Operatore(nave,this);
                handler.start();
            }
        }catch(IOException ex){
            System.out.println("Error 301 - I/O Exception Porto --> initServer");
        }
    }
    
    public int requestBanchina(int lunghezzaNave){
        if(lunghezzaNave <= 40) 
        	return banchinaPiccola();
        return banchinaGrande();
    }
    
    private int banchinaGrande(){
        int numBanchina = -1;
        try{
            //controlla se è disponibile una banchina
            this.bSup40.acquire();
            this.mutex1.acquire();
            //controlla quale è libera
            for(int i = 0; i < this.banchineSup40.length; i++)
                if(this.banchineSup40[i] == false)
                    numBanchina = i;
            this.banchineSup40[numBanchina] = true; //occupa la banchina
            this.mutex1.release();
        }catch(InterruptedException ex){
            System.out.println("Error 303 - InterruptedException Porto --> banchinaGrande");
        }
        return numBanchina;
    }
    
    private int banchinaPiccola(){
        int numBanchina = -1;
        try{
        //controlla innanzitutto se è disponibile una banchina 
        this.bMax40.acquire();
        this.mutex.acquire();
        //controlla quale è libero
        for(int i = 0; i < this.banchineMax40.length; i++)
            if(this.banchineMax40[i] == false)
                numBanchina = i;
        this.banchineMax40[numBanchina] = true; //occupa la banchina
        this.mutex.release();
        }catch(InterruptedException ex){
            System.out.println("Error 303 -  InterruptedException Porto --> banchinaPiccola");
        }
        return numBanchina;
    }
    
    public void releaseBanchina(int lunghezzaNave,int nBanchina){
        if(lunghezzaNave <= 40) 
        	releasePiccola(nBanchina);
        else releaseGrande(nBanchina);
    }
    
    private void releasePiccola(int nBanchina){
        try{
            this.mutex.acquire();
            //rilascia la banchina
            this.banchineMax40[nBanchina] = false;
            this.mutex.release();
            this.bMax40.release();
        }catch(InterruptedException ex){
            System.out.println("Error 303 - InterruptedException Porto --> releasePiccola");
        }
    }
    
    private void releaseGrande(int nBanchina){
        try{
            this.mutex1.acquire();
            //rilascia la banchina
            this.banchineSup40[nBanchina] = false;
            this.mutex1.release();
            this.bSup40.release();
        }catch(InterruptedException ex){
            System.out.println("Error 303 - InterruptedException Porto --> releaseGrande");
        }
    }
    
    public synchronized InetAddress getOperatore(int numOperatore){
        return this.operatori.get(numOperatore);
    }
    
    public boolean isAttivo() { return attivo; }//isAttivo
    public void setAttivo(boolean attivo){ this.attivo = attivo; }//setAttivo
	
}
