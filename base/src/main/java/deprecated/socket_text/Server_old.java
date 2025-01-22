package deprecated.socket_text;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server_old {
	private static ArrayList<Socket> sockets = new ArrayList<Socket>();
	
	public static void main(String[] args) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				start();
			}
		}).start();
		
	}
	private static void start() {
		ServerSocket sever = null;
		try {
			sever = new ServerSocket(1025);
			System.out.println(InetAddress.getLocalHost().getHostAddress()+":1025");
			while (true) {
				Socket socket = sever.accept();
				sockets.add(socket);
				System.out.println(socket.getInetAddress()+" ");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
