package porto;

public class Operatore {
	
	private String address = "localhost";
	private static final int TCP_PORT = 4000;

	
	public Operatore(String address) {
		this.address = address;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public int getTCP_PORT() {
		return TCP_PORT;
	}
	
}
