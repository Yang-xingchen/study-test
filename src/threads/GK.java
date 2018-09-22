package threads;

import java.util.Random;
/*
 *
 */
public class GK implements Runnable{
	private CK xck;
	private String name;
	private CK[] ck;
	private Random random = new Random();

	public GK(String name ,CK[] ck,int ckrl) {
		this.name = name;
		this.ck = ck;
		xck = new CK(null, ckrl);
	}
	private void start() {
		while (true) {
			if (random.nextBoolean()) {
				String name = CK.CP_NAME[random.nextInt(3)];
				use(name);
			}else {
				String name = CK.CP_NAME[random.nextInt(3)];
				get(name);
			}
		}
	}
	
	@Override
	public void run() {
		for (int i = 0; i < ck.length/2; i++) {
			get(CK.CP_NAME[random.nextInt(3)]);
		}
		start();
	}
	/*
	 *
	 */
	private void get(String name) {
		CP cp;
		int n = random.nextInt(ck.length);
		CK ck = this.ck[n];
		try {
			cp = ck.out(name);
		} catch (donfoungExecption e) {
//			System.out.println(""+this.name+""+ck.getname()+":"+name+"");
			try {
				Thread.sleep(20);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			start();
			return;
		}
		cp.setMaster(this.name);;
		chun(cp);
	}
	/*
	 *
	 */
	private void chun(CP cp) {
		try {
			xck.in(cp);
		} catch (CKmanExecption e) {
//			System.out.println(this.name+" ");
			try {
				Thread.sleep(20);
				chun(cp);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
	/*
	 *
	 */
	private void use(String name) {
		CP cp;
		try {
			cp = xck.out(name);
		} catch (donfoungExecption e) {
//			System.out.println(this.name+" "+name);
			start();
			return;
		}
		cp.use();
		
	}



}
