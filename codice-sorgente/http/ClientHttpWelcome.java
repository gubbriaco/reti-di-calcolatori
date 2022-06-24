package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHttpWelcome {
	
    public static void main(String[] args) {
    	
        try {
        	
            Socket s = new Socket("localhost", 8188);
            System.out.format("Connessione creata %s.%n", s.toString());
            PrintWriter pw = new PrintWriter( s.getOutputStream(), true);
            BufferedReader br = new BufferedReader( new InputStreamReader(s.getInputStream()));

            new MyReader(s).start();

            while(true){
                @SuppressWarnings("resource")
				Scanner sc = new Scanner(System.in);
                String line = sc.nextLine();
                if(line.equals("EXIT"))
                    break;
                pw.println(line);
            }
            pw.close();
            br.close();
            
        } catch (IOException e) {
            System.out.println("Error" + e);
        }
        
    }
    
    private static class MyReader extends Thread {
    	
    	private BufferedReader in;
    	private PrintWriter out;
    	private Socket s;

    	public MyReader(Socket s) {
    		this.s = s;
    	}

    	@Override public void run() {
    		boolean more = true;
    		try {
    			out = new PrintWriter(s.getOutputStream(), true);
    			in = new BufferedReader(new InputStreamReader(s.getInputStream()));

    			String line = in.readLine();
    			System.out.println(line);

    			while (more) {
    				@SuppressWarnings("resource")
					Scanner sc = new Scanner(System.in);
    				String lineConsole = sc.nextLine();
    				out.println(lineConsole);

    				line = in.readLine();
    				System.out.println(line);
    				if(lineConsole.equals("BYE")) {
    					more = false;
    				}
    			}
    			s.close();
    		} catch (IOException e) {
    			System.out.println("Error" + e);
    		}
    	}

    }
    
}
