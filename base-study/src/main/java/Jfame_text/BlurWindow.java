package Jfame_text;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class BlurWindow extends JFrame {

    public static void main(String[] args) {
        new BlurWindow();
    }

    BlurWindow(){
        setBounds(100, 100, 1600, 900);
        RepaintManager.setCurrentManager(new RepaintManager(){
            @Override
            public void addDirtyRegion(JComponent c, int x, int y, int w, int h) {
                System.out.println(c);
                System.out.println(x+" "+y+" "+w+" "+h);
                super.addDirtyRegion(BlurWindow.this, -c.getX(), -c.getY(), BlurWindow.this.getWidth(), BlurWindow.this.getHeight());
            }
        });
        setLayout(new FlowLayout());
        for (int i = 0; i < 10; i++) {
            JButton jButton = new JButton("button" + i);
            jButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.out.println(e.getComponent().getName());
                }
            });
            add(jButton);
        }
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        BufferedImage image = (BufferedImage) createImage(getWidth(),getHeight());
        Graphics graphics = image.getGraphics();
        super.paint(graphics);
        int RADIUS = 3;
        float[] data = new float[RADIUS];
        for (int i = 0; i < data.length; i++) {
            data[i] = 1f / RADIUS;
        }
        image = new ConvolveOp(new Kernel(RADIUS,1,data)).filter(image, null);
        image = new ConvolveOp(new Kernel(1, RADIUS,data)).filter(image, null);
        g.drawImage(image, 0, 0, null);
    }
}
