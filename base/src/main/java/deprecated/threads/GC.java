package deprecated.threads;

import java.util.Random;
/*
 */
public class GC implements Runnable{
	String name;
	CK[] ck;
	private Random random = new Random();
	
	public GC(String name, CK[] ck) {
		this.name = name;
		this.ck = ck;
	}
	/*
	 * 
	 * ������Ʒ
	 */
	@Override
	public void run() {
		while (true) {
			CP cp;
			
			switch (random.nextInt(3)) {
			case 0:
				cp = new CP1();
				break;
			case 1:
				cp = new CP2();
				break;
			case 2:
				cp = new CP3();
				break;

			default:
				return;
			}
			/*
			 * ����ֿ�
			 */
			int i = random.nextInt(ck.length);
			try {
				ck[i].in(cp);
				Thread.sleep(10);
			} catch (CKmanExecption e) {
//				ck[i].look();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	
}

