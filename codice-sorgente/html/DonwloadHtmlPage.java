package html;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class DonwloadHtmlPage {
    
    public static void main(String...strings){
        
        try{
            
            String URL = "stackoverlow.com";
            @SuppressWarnings("resource")
			Socket socket = new Socket(URL, 80);
            
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            pw.println("GET/questions HTTP/1.1");
            pw.println("Host: " + URL);
            pw.println();
            
            boolean more = true;
            String line;
            while(more) {
            	line = br.readLine();
            	if(line==null)
            		more = false;
            	else
            		System.out.println(line);
            }
            
            
        }catch(IOException e){
            e.printStackTrace();
        }
        
    }
    
}
