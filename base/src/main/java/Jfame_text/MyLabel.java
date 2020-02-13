package Jfame_text;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Random;

/**
 * 闪烁的标签
 */
public class MyLabel extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5242964722394454641L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
            public void run() {
				try {
					MyLabel frame = new MyLabel();
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
	public MyLabel() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		Label lblNewLabel = new Label("New label");
		add(lblNewLabel);
		Label lblNewLabel2 = new Label("New label 2");
		add(lblNewLabel2);
		Label lblNewLabel3 = new Label("New label 3");
		add(lblNewLabel3);
		new Thread(new Runnable() {
			@Override
			public void run() {
				Random r = new Random();
				while (true) {
					lblNewLabel.setColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255), r.nextInt(200)));
					lblNewLabel2.setColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255), r.nextInt(200)));
					lblNewLabel3.setColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255), r.nextInt(200)));
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
//		GroupLayout gl_contentPane = new GroupLayout(contentPane);
//		gl_contentPane.setHorizontalGroup(
//			gl_contentPane.createParallelGroup(Alignment.LEADING)
//				.addGroup(gl_contentPane.createSequentialGroup()
//					.addGap(54)
//					.addComponent(lblNewLabel)
//					.addContainerGap(296, Short.MAX_VALUE))
//		);
//		gl_contentPane.setVerticalGroup(
//			gl_contentPane.createParallelGroup(Alignment.LEADING)
//				.addGroup(gl_contentPane.createSequentialGroup()
//					.addGap(109)
//					.addComponent(lblNewLabel)
//					.addContainerGap(116, Short.MAX_VALUE))
//		);
//		contentPane.setLayout(gl_contentPane);
	}

}
class Label extends JLabel{

	private static final long serialVersionUID = -5449804551160092147L;
	private Color color;
	
	@Override
	public void paint(Graphics g) {
		Graphics2D graphics2d = (Graphics2D)g;
		graphics2d.setColor(color);
		graphics2d.fillRoundRect(0, 0, getWidth(), getHeight(), getWidth()/3, getHeight()/3);
		super.paint(g);
	}
	
	public Label(String string) {
		super(" "+string+" ");
		color = new Color(255, 200, 200, 200);
//		repaint();
	}
	
	public void setColor(Color color) {
		this.color = color;
		repaint();
	}
}











