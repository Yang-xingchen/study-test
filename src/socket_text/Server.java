package socket_text;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Server extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3354001740862289104L;
	private ArrayList<Socket> sockets = new ArrayList<Socket>();
	private JPanel contentPane;
	private JTextArea textArea;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Server frame = new Server();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Server() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		new Thread(new Runnable() {
			@Override
			public void run() {
				start();
			}
		}).start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean isMove = false;
				while (true) {
					for (Socket socket : sockets) {
						if (socket.isClosed()||null==socket) {
							sockets.remove(socket);
							isMove = true;
							break;
						}
					}
					if (isMove) {
						isMove = false;
						continue;
					}
					setTitle(" "+sockets.size()+" \n");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	private void start() {
		ServerSocket sever = null;
		try {
			sever = new ServerSocket(1025);
			textArea.append(" "+InetAddress.getLocalHost().getHostAddress()+":1025\n");
			while (true) {
				Socket socket = sever.accept();
				sockets.add(socket);
				new Thread(new Runnable() {
					@Override
					public void run() {
						getText(socket);
					}
				}).start();
			}
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	}

	private void getText(Socket socket) {
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader br = null;
		try {
			inputStream = socket.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream);
			br = new BufferedReader(inputStreamReader);
			boolean isFrist = true;
			while (true) {
				String string= br.readLine();
				System.err.println(string);
				if (isFrist) {
					string = "***�û�:"+string.substring(0, string.indexOf(":"))+" ";
					isFrist = false;
				}
				if (string.indexOf("\\\\exit")>=0) {
					textArea.append(" :"+string.substring(string.indexOf(" "), string.length())+" \n" );
					returnText(socket," "+string.substring(string.indexOf(" "), string.length())+" ");
					sockets.remove(socket);
					socket.close();
					return;
				}	
				textArea.append(string+"\n");
				returnText(socket, string);
			}
		} catch (Exception e) {
//			e.printStackTrace();
			sockets.remove(socket);
		} 
//		finally {
//			try {
//				br.close();
//				inputStreamReader.close();
//				inputStream.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
	}
	
	private void returnText(Socket socket,String string) {
		OutputStream outputStream = null;
		OutputStreamWriter outputStreamWriter = null;
		BufferedWriter bw = null;
		for (Iterator<Socket> iterator = sockets.iterator(); iterator.hasNext();) {
			Socket s = (Socket) iterator.next();
			if (s.equals(socket)) {
				continue;
			}
			try {
				outputStream = s.getOutputStream();
				outputStreamWriter = new OutputStreamWriter(outputStream);
				bw = new BufferedWriter(outputStreamWriter);
				bw.write(string+"\n");
				bw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
//			finally {
//				try {
//					bw.close();
//					outputStreamWriter.close();
//					outputStream.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
		}
	}
}





