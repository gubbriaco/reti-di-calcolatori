package url;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class URLTest {
	
	
	public static void main(String...strings) {
		
		try {
			
			URL url = new URL("http://www.w3.org");
			
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			
			boolean more = true;
			String line;
			while(more) {
				
				line = br.readLine();
				if(line==null)
					more = false;
				else
					System.out.println(line);
				
			}
			
			br.close();
			
		}catch(IOException e) {
			System.err.println(e);
		}
		
	}
	

}
