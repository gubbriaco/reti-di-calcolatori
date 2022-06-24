package time_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class TimeServer {
	
	
	public static void main(String...strings) {
		
		try {
			
			@SuppressWarnings("resource")
			Socket socket = new Socket("ntp1.inrim.it", 13);
			
			BufferedReader br = new BufferedReader(new InputStreamReader( socket.getInputStream()));
			
			boolean more = true;
			String line;
			while(more) {
				line = br.readLine();
				if(line==null)
					more = false;
				else
					System.out.println(line);
			}
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	

}
