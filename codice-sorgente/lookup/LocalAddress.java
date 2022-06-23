package lookup;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LocalAddress {
	
	public static void main(String...strings) {
		
		try {
			
			InetAddress mySelf = InetAddress.getLocalHost();
			
			System.out.println("My name: " + mySelf.getHostName());
			System.out.println("My address: " + mySelf.getHostAddress());
                        
			
		}catch(UnknownHostException e) {
			e.printStackTrace();
		}
		
	}
	

}
