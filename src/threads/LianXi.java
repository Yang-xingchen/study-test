package threads;


/*
 * @version 3.0
 * 
 */
public class LianXi {

	public static void main(String[] args) {
		CK[] cks ={new CK(" ", 300),new CK("   2", 300)};
		
		
		GC gc1 = new GC(" ", cks);
		GC gc2 = new GC(" ", cks);
		GC gc3 = new GC(" ", cks);
		
		GK gk1 = new GK(" 1", cks, 25);
		GK gk2 = new GK(" ", cks, 20);
		GK gk3 = new GK(" 3", cks, 15);
		
		Thread tgc1 = new Thread(gc1);
		Thread tgc2 = new Thread(gc2);
		Thread tgc3 = new Thread(gc3);
		Thread tgk1 = new Thread(gk1);
		Thread tgk2 = new Thread(gk2);
		Thread tgk3 = new Thread(gk3);
		
		tgc1.start();
		tgc2.start();
		tgc3.start();
		tgk1.start();
		tgk2.start();
		tgk3.start();
		
		
	}

}
