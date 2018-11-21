package temp;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

public class FanShe extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel parent;
	private JTextField me;
	private JList<String> sonlist;
	Class<?> clazz;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
            public void run() {
				try {
					FanShe frame = new FanShe();
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
	public FanShe() {
		clazz = Object.class;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		parent = new JLabel("");
		
		me = new JTextField("");
		me.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				super.keyReleased(e);
				if (e.getKeyCode()=='\n') {
					try {
//						flshList(Class.forName(me.getText()));
						clazz = Class.forName(me.getText());
						flshList();
					} catch (ClassNotFoundException e1) {
//						me.setCaretColor(Color.RED);
						me.setSelectionColor(Color.red);
						e1.printStackTrace();
					}
				}
				me.setSelectionColor(Color.white);
			}
			
		});
		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(parent, GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE)
				.addComponent(me, GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(parent)
					.addGap(18)
					.addComponent(me)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE))
		);
		sonlist = new JList<String>();
		sonlist.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				String name = sonlist.getSelectedValue();
				Method method = null;
				try {
					method = clazz.getMethod(name);
				} catch (NoSuchMethodException e1) {
					e1.printStackTrace();
				} catch (SecurityException e1) {
					e1.printStackTrace();
				}
				System.err.println(method.getName());
			}
		});
		flshList();
		scrollPane.setViewportView(sonlist);
		contentPane.setLayout(gl_contentPane);
	}
	
	private void flshList() {
		if (clazz.getSuperclass()==null) {
			parent.setText("");
		}else {
			parent.setText(" ࣺ"+clazz.getSuperclass().getName());
		}
		me.setText(clazz.getName());
        Method[] method = clazz.getMethods();
        String[] methodName = new String[method.length];
		for (int i = 0; i < methodName.length; i++) {
			methodName[i] = method[i].getName();
		}
		sonlist.setListData(methodName);
//		Class<?>[] classes = clazz.getClasses();
//		String className[] = new String[classes.length];
//		for (int i = 0; i < className.length; i++) {
//			className[i] = classes[i].getName();
//		}
//		sonlist.setListData(className);
	}
}
