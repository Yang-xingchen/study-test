package Jfame_text;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PaintOrderTest extends JFrame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
            new PaintOrderTest();
        });
    }

    PaintOrderTest(){
        setBounds(100,100,500,300);
        JButton jButton = new JButton("test"){
            @Override
            protected void paintBorder(Graphics g) {
                System.out.println("        button paintBorder before");
                super.paintBorder(g);
                System.out.println("        button paintBorder after");
            }

            @Override
            protected void paintComponent(Graphics g) {
                System.out.println("        button paintComponent before");
                super.paintComponent(g);
                System.out.println("        button paintComponent after");
            }

            @Override
            protected void paintChildren(Graphics g) {
                System.out.println("        button paintChildren before");
                super.paintChildren(g);
                System.out.println("        button paintChildren after");
            }

            @Override
            public void paint(Graphics g) {
                System.out.println("        button paint before");
                super.paint(g);
                System.out.println("        button paint after");
            }
        };
        jButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.err.println("------------------------------------------------------");
                repaint();
            }
        });
        JPanel jPanel = new JPanel(){
            @Override
            protected void paintBorder(Graphics g) {
                System.out.println("    JPanel paintBorder before");
                super.paintBorder(g);
                System.out.println("    JPanel paintBorder after");
            }

            @Override
            protected void paintComponent(Graphics g) {
                System.out.println("    JPanel paintComponent before");
                super.paintComponent(g);
                System.out.println("    JPanel paintComponent after");
            }

            @Override
            protected void paintChildren(Graphics g) {
                System.out.println("    JPanel paintChildren before");
                super.paintChildren(g);
                System.out.println("    JPanel paintChildren after");
            }

            @Override
            public void paint(Graphics g) {
                System.out.println("    JPanel paint before");
                super.paint(g);
                System.out.println("    JPanel paint after");
            }
        };
        jPanel.add(jButton);
        add(jPanel);
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        System.out.println("paint before");
        super.paint(g);
        System.out.println("paint after");
    }

    @Override
    public void paintComponents(Graphics g) {
        System.out.println("paintComponents before");
        super.paintComponents(g);
        System.out.println("paintComponents after");
    }
}
