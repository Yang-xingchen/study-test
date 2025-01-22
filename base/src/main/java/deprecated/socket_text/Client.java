package deprecated.socket_text;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2207729308720914544L;
	public static String name;
	private JPanel contentPane;
	private static Socket socket = null;
	private JTextField daiFa;
	private JTextArea textWindow;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		String string = JOptionPane.showInputDialog("");
		String ip = string.substring(0, string.indexOf(":"));
		int port = Integer.parseInt(string.substring(string.indexOf(":")+1, string.length()));
		name = JOptionPane.showInputDialog(" ");
		try {
			socket = new Socket(ip, port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		EventQueue.invokeLater(new Runnable() {
			@Override
            public void run() {
				try {
					Client frame = new Client();
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
	public Client() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JButton shoot = new JButton("����");
		shoot.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					super.mouseClicked(e);
					if ("".equals(daiFa.getText())) {
						daiFa.setBackground(new Color(255, 200, 200));
						return;
					}
					faSong(daiFa.getText());
					daiFa.setText("");
					daiFa.setBackground(Color.white);
				}
		});
		daiFa = new JTextField();
		daiFa.setFocusCycleRoot(true);
		daiFa.setColumns(10);
		daiFa.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				if ('\n'==e.getKeyChar()) {
					if ("".equals(daiFa.getText())) {
						daiFa.setBackground(new Color(255, 200, 200));
						return;
					}
					faSong(daiFa.getText());
					daiFa.setText("");
				}
				daiFa.setBackground(Color.white);
			}
		});
		
		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addComponent(daiFa, GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(shoot))
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(shoot)
						.addComponent(daiFa, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
		);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				super.windowClosing(arg0);
				faSong("\\\\exit "+name);
				try {
//					Thread.sleep(50);
//					socket.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		textWindow = new JTextArea();
		textWindow.setEditable(false);
		scrollPane.setViewportView(textWindow);
		contentPane.setLayout(gl_contentPane);
		setTitle(name);
		new Thread(new Runnable() {
			@Override
			public void run() {
				faSong("");
				getText();
			}
		}).start();
	}
	
	private void faSong(String text) {
		String string =name + ":" + text+"\n";
		if ("".equals(text)) {
			textWindow.append("******\n");
		}else {
			textWindow.append(":"+text+"\n");
		}
		OutputStream outputStream = null;
		OutputStreamWriter outputStreamWriter = null;
		BufferedWriter bw = null;
		try {
			outputStream = socket.getOutputStream();
			outputStreamWriter = new OutputStreamWriter(outputStream);
			bw = new BufferedWriter(outputStreamWriter);
			bw.write(string);
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		finally {
//			try {
//				bw.close();
//				outputStreamWriter.close();
//				outputStream.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
	}
	
	private void getText() {
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader br = null;
		try {
			inputStream = socket.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream);
			br = new BufferedReader(inputStreamReader);
			while (true) {
				String string= br.readLine();
				textWindow.append(string+"\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
			textWindow.append(" ");
			daiFa.setText("");
			daiFa.setEditable(false);
			try {
				br.close();
				inputStreamReader.close();
				inputStream.close();
				Thread.sleep(5000);
				System.exit(0);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
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
}






