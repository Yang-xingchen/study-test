package Jfame_text;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class j_01 extends JFrame implements Runnable{


	private static final long serialVersionUID = -2786624428172212752L;
	private Point2D point2d[];
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					j_01 frame = new j_01();
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
	public j_01() {
		setUndecorated(true);
		setOpacity(0.5f);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 300, 300);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				System.exit(0);
			}
		});
		drawSnack();
		//		repaint();
		new Thread(this).start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					repaint();
					try {
						Thread.sleep(8);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();;
	}

	private void drawSnack() {
		point2d = new Point2D[10];
		for (int i = 0; i < point2d.length; i++) {
			point2d[i]=new Point();
			point2d[i].setLocation(getWidth()/2+getX(), getHeight()/2+getY());
		}
	}


	@Override
	public void paint(Graphics g) {
//		super.paint(g);
		Graphics2D g2d = (Graphics2D)g;
		Robot robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		BufferedImage background = robot.createScreenCapture(new Rectangle(getX(), getY(), (int)getWidth(), (int)getHeight()));
		g2d.drawImage(background, 0, 0, null);
		g2d.setColor(Color.WHITE);
		g2d.fillOval((int)point2d[0].getX(), (int)point2d[0].getY(), 13, 13);
		for (int i = 1; i < point2d.length; i++) {
			g2d.drawOval((int)point2d[i].getX(), (int)point2d[i].getY(), 8, 8);
		}
	}
	
	@Override
	public void run() {
//		while (true) {
//			Point point = java.awt.MouseInfo.getPointerInfo().getLocation();
//			setBounds(point.x-getWidth()/2, point.y-getHeight()/2, getWidth(), getHeight());
//		}
		while (true) {
			while (point2d[0].getX()>getWidth()-10||point2d[0].getY()>getHeight()-10||point2d[0].getX()<10||point2d[0].getY()<10) {
				if (point2d[0].getX()>getWidth()-10) {
					point2d[0].setLocation(point2d[0].getX()-1, point2d[0].getY());
					setBounds(getX()+1, getY(), getWidth(), getHeight());
				}
				if (point2d[0].getY()>getHeight()-10) {
					point2d[0].setLocation(point2d[0].getX(), point2d[0].getY()-1);
					setBounds(getX(), getY()+1, getWidth(), getHeight());
				}
				if (point2d[0].getX()<10) {
					point2d[0].setLocation(point2d[0].getX()+1, point2d[0].getY());
					setBounds(getX()-1, getY(), getWidth(), getHeight());
				}
				if (point2d[0].getY()<10) {
					point2d[0].setLocation(point2d[0].getX(), point2d[0].getY()+1);
					setBounds(getX(), getY()-1, getWidth(), getHeight());
				}
			}
			Point MousePoint = java.awt.MouseInfo.getPointerInfo().getLocation();
			double x = MousePoint.x - point2d[0].getX()-getX();
			double y = MousePoint.y - point2d[0].getY()-getY();
			point2d[0].setLocation(point2d[0].getX()+x/5, point2d[0].getY()+y/5);
			for (int j = point2d.length-1; j > 0; j--) {
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				point2d[j].setLocation(point2d[j-1]);
			}
//			repaint();
		}
	}
	

	
}
