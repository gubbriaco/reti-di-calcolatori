package lookup;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class RemoteAddress {
	
	public static void main(String...strings) {
	
		try {
			
			String hostName = "nvidia.com";
			
			InetAddress machine = InetAddress.getByName(hostName);
			
			System.out.println("Host name: " + machine.getHostName());
			System.out.println("Host address: " + machine.getHostAddress());
			
		
		}catch(UnknownHostException e) {
			e.printStackTrace();
		}
		
	}

}
