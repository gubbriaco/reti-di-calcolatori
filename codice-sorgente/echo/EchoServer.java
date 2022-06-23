package echo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
	
	public static void main(String...strings) {
		
		try {
			
			@SuppressWarnings("resource")
			ServerSocket server = new ServerSocket(8189);
			
			Socket in = server.accept();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(in.getInputStream()));
			
			PrintWriter pw = new PrintWriter(in.getOutputStream(), true);
			
			pw.println("Hello! Enter BYE to exit.");
			
			boolean done = false;
			String line;
			while(!done) {
				line = br.readLine();
				if(line == null)
					done = true;
				else {
					pw.println("Echo: " + line);
					if(line.trim().equals("BYE"))
						done = true;
				}
			}
			pw.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}
