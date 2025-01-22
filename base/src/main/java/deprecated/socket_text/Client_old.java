package deprecated.socket_text;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client_old {
	public static void main(String[] args) {
		start();
		
	}
	
	private static void start() {
		Socket socket=null;
		try {
			socket = new Socket("192.168.137.1", 1025);
			System.out.println(" ");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
