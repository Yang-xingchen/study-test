package Jfame_text;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

/**
 * 椭圆窗口、绝对布局
 */
public class CustomWindow extends JFrame {

    public static void main(String[] args) {
        EventQueue.invokeLater(()->{
            JFrame jf = new CustomWindow();
            jf.setVisible(true);
        });
    }

    private Point ePoint;

    public CustomWindow() throws HeadlessException {
        setBounds(160, 90, 1600, 900);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        Shape shape = new Ellipse2D.Float(0, 0, 1600, 900);
        setShape(shape);
        final JPanel main = new JPanel();
        main.setLayout(null);
        main.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JLabel jLabel = new JLabel("click");
                jLabel.setBounds(e.getX(),e.getY(),50,20);
                main.add(jLabel);
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                ePoint = e.getPoint();
                System.out.println(e);
                repaint();
            }
        });
        add(main);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if (ePoint!=null) {
            g2d.translate(ePoint.x, ePoint.y);
        }
        super.paint(g2d);
    }
}
