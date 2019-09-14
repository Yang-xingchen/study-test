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

public class j_02 extends JFrame implements Runnable{


	private static final long serialVersionUID = -2786624428172212752L;
    private Point2D[] point2d;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
            public void run() {
				try {
					j_02 frame = new j_02();
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
	public j_02() {
		setAlwaysOnTop(true);
		setUndecorated(true);
		setOpacity(0.5f);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(500, 500, 300, 300);
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
			}
		});
		drawSnack();
		//		repaint();
		new Thread(this).start();
		new Thread(new Runnable() {
			@Override
			public void run() {
//				Long time = System.currentTimeMillis()/1000;
//				int i=0;
				while (true) {
					repaint();
//					if (System.currentTimeMillis()/1000!=time) {
//						time = System.currentTimeMillis()/1000;
//						System.err.println(i);
//						i=0;
//					}
//					i++;
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
		super.paint(g);
		Graphics2D g2d = (Graphics2D)g;
		
//		Robot robot = null;
//		try {
//			robot = new Robot();
//		} catch (AWTException e) {
//			e.printStackTrace();
//		}
//		BufferedImage background = robot.createScreenCapture(new Rectangle(getX(), getY(), (int)getWidth(), (int)getHeight()));
//		g2d.drawImage(background, 0, 0, null);
		
//		g2d.setColor(Color.WHITE);
		g2d.fillOval((int)point2d[0].getX(), (int)point2d[0].getY(), 13, 13);
		for (int i = 1; i < point2d.length; i++) {
			g2d.drawOval((int)point2d[i].getX(), (int)point2d[i].getY(), 8, 8);
		}
	}
	
	@Override
	public void run() {
		Random r = new Random(); 
		final int lim = 30;
		final int lim2 = 50;
		int rx = (int)point2d[0].getX()+r.nextInt(300)-150+getX();
		int ry = (int)point2d[0].getY()+r.nextInt(300)-150+getY();
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension dimension = toolkit.getScreenSize();
		while (true) {
			while (point2d[0].getX()>getWidth()-lim||point2d[0].getY()>getHeight()-lim||point2d[0].getX()<lim||point2d[0].getY()<lim) {
				if (point2d[0].getX()>getWidth()-lim) {
					point2d[0].setLocation(point2d[0].getX()-1, point2d[0].getY());
					setBounds(getX()+1, getY(), getWidth(), getHeight());
					rx--;
				}
				if (point2d[0].getY()>getHeight()-lim) {
					point2d[0].setLocation(point2d[0].getX(), point2d[0].getY()-1);
					setBounds(getX(), getY()+1, getWidth(), getHeight());
					ry--;
				}
				if (point2d[0].getX()<lim) {
					point2d[0].setLocation(point2d[0].getX()+1, point2d[0].getY());
					setBounds(getX()-1, getY(), getWidth(), getHeight());
					rx++;
				}
				if (point2d[0].getY()<lim) {
					point2d[0].setLocation(point2d[0].getX(), point2d[0].getY()+1);
					setBounds(getX(), getY()-1, getWidth(), getHeight());
					ry++;
				}
			}
			
			double x = rx - point2d[0].getX()-getX();
			double y = ry - point2d[0].getY()-getY();
			
//			if (rx<=(int)point2d[0].getX()+10 && ry<=(int)point2d[0].getY()+10 && rx>=(int)point2d[0].getX()-10 && ry>=(int)point2d[0].getY()-10) {
//				System.err.println("next");
//				rx = (int)point2d[0].getX()+r.nextInt(300)-150;
//				ry = (int)point2d[0].getY()+r.nextInt(300)-150;
//			}
			if (x<=10&&x>=-10&&y<10&&y>-10) {
				rx = (int)point2d[0].getX()+r.nextInt(300)-150+getX();
				ry = (int)point2d[0].getY()+r.nextInt(300)-150+getY();
				continue;
			}
//			System.err.println(x+"  "+y+"|"+point2d[0].getX()+"  "+point2d[0].getY());
			point2d[0].setLocation(point2d[0].getX()+x/5, point2d[0].getY()+y/5);
			if (getX()<lim2) {
//				System.err.println("err1 "+rx+"  "+ry);
				rx = (int)point2d[0].getX()+r.nextInt(300)+getX();
				continue;
			}
			if (getX()>dimension.getWidth()-getWidth()-lim2) {
//				System.err.println("err2 "+rx+"  "+ry);
				rx = (int)point2d[0].getX()+r.nextInt(300)-300+getX();
				continue;
			}
			if (getY()<lim2) {
//				System.err.println("err1 "+rx+"  "+ry);
				ry = (int)point2d[0].getY()+r.nextInt(300)+getY();
				continue;
			}
			if (getY()>dimension.getHeight()-getHeight()-lim2) {
//				System.err.println("err2 "+rx+"  "+ry);
				ry = (int)point2d[0].getY()+r.nextInt(300)-300+getY();
				continue;
			}
			
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
