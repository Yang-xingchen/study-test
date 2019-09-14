package threads;
/*
 */
public class CK {
	private String name;
	private CP[] ck;
	public static final String[] CP_NAME = {" "," "," "};
	
	public String getname() {
		return name;
	}
	public CK(String name, int rongliang) {
		this.name = name;
		ck = new CP[rongliang];
	}
	/*
	 */
	public synchronized void in(CP changping) throws CKmanExecption {
		for (int i = 0; i < ck.length; i++) {
			if (ck[i]==null) {
				ck[i]=changping;
				return;
			}
		}
		throw new CKmanExecption();
	}
	/*
	 */
	public synchronized CP out(String name) throws donfoungExecption{
		int index = found(name);
		CP cp = ck[index];
		ck[index] = null;
		return cp;
	}
	/*
	 */
	private int found(String name) throws donfoungExecption{
		for (int i = 0; i < ck.length; i++) {
			if (ck[i]!=null) {
				if (name.equals(ck[i].getname())) {
					return i;
				}
			}
		}
		throw new donfoungExecption();
	}
	/*
	 */
	public void look() {
		int w1=0;
		int w2=0;
		int w3=0;
		for (int i = 0; i < ck.length; i++) {
			String name = ck[i].getname();
			if (name == CP_NAME[0]) {
				w1++;
			}
			if (name == CP_NAME[1]) {
				w2++;
			}
			if (name == CP_NAME[2]) {
				w3++;
			}
			
		}
		System.out.println(this.name+" ("+ck.length+"):1("+w1+"),2("+w2+"),3("+w3+")");
		
	}
}
/*
 */
class CKmanExecption extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2762371616660777280L;}
class donfoungExecption extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5259700861946953930L;}
