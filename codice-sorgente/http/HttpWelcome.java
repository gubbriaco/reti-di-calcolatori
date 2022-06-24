package http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

public class HttpWelcome {
	
	private static int port = 80;
	
	private static String HTMLWelcomeMessage() {
		return "<html>\n" + 
			   "	<head>\n" +
			   "		<title>UNICAL-Ingegneria Informatica</title>\n" +
			   "	</head>\n" +
			   "	<body>\n" + 
			   "	<h2 align=\"center\">\n" +
			   "		<font color=\"#0000FF\">\n" + 
			   "			Benvenunti al corso di Reti di Calcolatori\n" + 
			   "		</font>\n" + 
			   "	</h2>\n" +
			   "	</body>\n" +
			   "</html>";
	}
	
	
	public static void main(String...strings) {
		
		try {
			
			ServerSocket server = new ServerSocket(port);
			System.out.println(server.toString());
			System.out.println("HTTP server running on port: " + port);
			
			while(true) {
				
				Socket client = server.accept();
				BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
				
				String request = br.readLine();
				System.out.println("REQUEST: " + request);
				StringTokenizer st = new StringTokenizer(request);
				
				if( (st.countTokens() >= 2) && st.nextToken().equals("GET") ) {
					
					String msg = HTMLWelcomeMessage();
					
					pw.println("HTTP/1.0 200 OK");
					pw.println("Content-Length: " + msg.length());
					pw.println("Content-Type: text/html");
					pw.println();
					pw.println(msg);
					
				}
				else {
					pw.println("400 Bad Request");
				}
				pw.flush();
				client.close();
				server.close();
			}
			
		}catch(Exception e) {
			System.err.println(e);
		}
		
	}

}
