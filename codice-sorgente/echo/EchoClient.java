package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class EchoClient {
	
	public static void main(String...strings) {
		
		try {
	
			Socket s = new Socket("localhost", 8189);
	
			BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			PrintWriter pw = new PrintWriter(s.getOutputStream(), true);
	
			pw.println("Prova invio");
			pw.println("Prova(2) invio");
			pw.println("BYE");
	
			boolean more = true;
			while (more) {
				String line = br.readLine();
				if (line == null)
					more = false;
				else
					System.out.println(line);
			}
	
			pw.close();
			br.close();
			s.close();
	
		} catch (IOException e) {
			System.out.println("Error" + e);
		}

	}

}
