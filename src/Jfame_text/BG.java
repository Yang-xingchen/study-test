package Jfame_text;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

/**
 * 背景图片
 */
public class BG extends JFrame{

    private Image background;

    private final int TOP = 50;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
            try {
                JFrame jFrame = new BG();
                jFrame.setVisible(true);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    public BG(){
        setBounds(160,90,1600,900);
        setUndecorated(true);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                System.out.println(e);
                if (e.getY() < TOP){
                    setUndecorated(false);
                }else {
                    setUndecorated(true);
                }
            }

        });
    }

    private void initBackGround(){
        background = createImage(getWidth(),getHeight());
        Graphics g2d = background.getGraphics();
        Image image = getImage();
        g2d.drawImage(image, 0, 0, 1600, 900, this);
        g2d.setColor(new Color(1,1,1,0.1f));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setColor(new Color(0,0,0,0.7f));
        g2d.fillRect(1100, 0, 400, 900);
//        g2d.fillRect(1550, 0, 50, 50);
    }

    private BufferedImage bg = null;

    private Image getImage(){
        final int RADIUS = 9 ;
        if (bg!=null) {
            return bg;
        }
        Image image = new ImageIcon("D:\\MyPictures\\67101372_p0.jpg").getImage();
        float[] data = new float[RADIUS];
        for (int i = 0; i < data.length; i++) {
            data[i] = 1f / RADIUS;
        }
        bg = new BufferedImage(1600,900,BufferedImage.TYPE_INT_ARGB);
        bg.getGraphics().drawImage(image, 0, 0, 1600,900, null);
        bg = new ConvolveOp(new Kernel(RADIUS,1,data)).filter(bg, null);
        bg = new ConvolveOp(new Kernel(1, RADIUS,data)).filter(bg, null);
        return bg;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (null == background){
            initBackGround();
        }
        System.out.println(g.getClass());
        g.drawImage(background, 0,0,getWidth(),getHeight(),this);
    }

}
