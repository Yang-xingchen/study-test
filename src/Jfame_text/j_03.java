package Jfame_text;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.util.Random;

import javax.swing.JFrame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class j_03 extends JFrame{


	private static final long serialVersionUID = -2786624428172212752L;
	private Point2D point2d[];
	private Point2D point = new Point();
	private boolean isInit = false;
	final int LIM = 30;
	final int LIM2 = 50;
	final int MAX = 300;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					j_03 frame = new j_03();
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
	public j_03() {
		setAlwaysOnTop(true);
		setUndecorated(true);
		setOpacity(0.5f);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setBounds(500, 500, 300, 300);
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 300; i++) {
					setBounds(600-i/2, 600-i/2, i, i);
					try {
						Thread.sleep(8);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				start();
				isInit = true;
			}
		}).start();
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				System.exit(0);
			}
			Point point = new Point();
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				point.setLocation(e.getX(),e.getY());
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				setBounds(e.getX()-point.x+getX(), e.getY()-point.y+getY(), getWidth(), getHeight());
				getRandomPoint();
			}
		});
		
	}
	
	private void start() {
		initSnack();
		new Thread(new Runnable() {
			@Override
			public void run() {
				getSnackPoint();
			}
		}).start();
		new Thread(new Runnable() {
			@Override
			public void run() {
//					Long time = System.currentTimeMillis()/1000;
//					int i=0;
				while (true) {
					repaint();
//						if (System.currentTimeMillis()/1000!=time) {
//							time = System.currentTimeMillis()/1000;
//							System.err.println(i);
//							i=0;
//						}
//						i++;
					try {
						Thread.sleep(8);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();;
	}

	private void initSnack() {
		point2d = new Point2D[10];
		for (int i = 0; i < point2d.length; i++) {
			point2d[i]=new Point();
			point2d[i].setLocation(getWidth()/2+getX(), getHeight()/2+getY());
		}
	}
	private void getRandomPoint() {
		Random r = new Random(); 
		int rx = (int)point2d[0].getX()+r.nextInt(MAX)-MAX/2;
		int ry = (int)point2d[0].getY()+r.nextInt(MAX)-MAX/2;
		point.setLocation(rx, ry);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (!isInit) {
			g.fillOval(getWidth()/2-7, getHeight()/2-7, 13, 13);
			return;
		}
//		Robot robot = null;
//		tpoint.getY() {
//			robot = new Robot();
//		} catch (AWTException e) {
//			e.printStackTrace();
//		}
//		BufferedImage background = robot.createScreenCapture(new Rectangle(getX(), getY(), (int)getWidth(), (int)getHeight()));
//		g2d.drawImage(background, 0, 0, null);
//		g2d.setColor(Color.WHITE);
		Graphics2D g2d = (Graphics2D)g;
		g2d.fillOval((int)point2d[0].getX() - getX(), (int)point2d[0].getY()-getY(), 13, 13);
		for (int i = 1; i < point2d.length; i++) {
			g2d.drawOval((int)point2d[i].getX()-getX(), (int)point2d[i].getY()-getY(), 8, 8);
		}
	}
	
	
	public void getSnackPoint() {
		getRandomPoint();
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension dimension = toolkit.getScreenSize();
		while (true) {
			while (point2d[0].getX()-getX()>getWidth()-LIM||point2d[0].getY()-getY()>getHeight()-LIM||point2d[0].getX()-getX()<LIM||point2d[0].getY()-getY()<LIM) {
				if (point2d[0].getX()-getX()>getWidth()-LIM) {
					point2d[0].setLocation(point2d[0].getX()-1, point2d[0].getY());
					setBounds(getX()+1, getY(), getWidth(), getHeight());
					point.setLocation(point.getX()-1, point.getY());
				}
				if (point2d[0].getY()-getY()>getHeight()-LIM) {
					point2d[0].setLocation(point2d[0].getX(), point2d[0].getY()-1);
					setBounds(getX(), getY()+1, getWidth(), getHeight());
					point.setLocation(point.getX(), point.getY()-1);
				}
				if (point2d[0].getX()-getX()<LIM) {
					point2d[0].setLocation(point2d[0].getX()+1, point2d[0].getY());
					setBounds(getX()-1, getY(), getWidth(), getHeight());
					point.setLocation(point.getX()+1, point.getY());
				}
				if (point2d[0].getY()-getY()<LIM) {
					point2d[0].setLocation(point2d[0].getX(), point2d[0].getY()+1);
					setBounds(getX(), getY()-1, getWidth(), getHeight());
					point.setLocation(point.getX(), point.getY()+1);
				}
			}
			if (getX()<LIM2) {
				getRandomPoint();
				continue;
			}
			if (getX()>dimension.getWidth()-getWidth()-LIM2) {
				getRandomPoint();
				continue;
			}
			if (getY()<LIM2) {
				getRandomPoint();
				continue;
			}
			if (getY()>dimension.getHeight()-getHeight()-LIM2) {
				getRandomPoint();
				continue;
			}
			double x = point.getX() - point2d[0].getX();
			double y = point.getY() - point2d[0].getY();
			if (x<=10&&x>=-10&&y<10&&y>-10) {
				getRandomPoint();
				continue;
			}
			point2d[0].setLocation(point2d[0].getX()+x/5, point2d[0].getY()+y/5);
			for (int j = point2d.length-1; j > 0; j--) {
				try {
					Thread.sleep(8);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				point2d[j].setLocation(point2d[j-1]);
			}
//			repaint();
		}
	}
	

	
}
