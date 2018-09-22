package threads;
/*
 */
public class CP {
	private final String NAME = " ";
	private String master;
	
	public void setMaster(String name) {
		this.master = name;
	}
	public void use() {
		System.out.println(master+" "+NAME);
	}

	public String getname() {
		return NAME;
	}
	
	@Override
	public boolean equals(Object obj) {
		CP cp = (CP)obj;
		String n = cp.NAME;
		return n.equals(this.NAME);
	}
}

class CP1 extends CP{
	private final String NAME = " ";
	private String master;
	
	public void setMaster(String name) {
		this.master = name;
	}
	
	public void use() {
		System.out.println(master+" "+NAME);
	}
	
	public String getname() {
		return NAME;
	}
	
	@Override
	public boolean equals(Object obj) {
		CP1 cp = (CP1)obj;
		String n = cp.NAME;
		return n.equals(this.NAME);
	}
}

class CP2 extends CP{
	private final String NAME = "";
	private String master;
	
	public void setMaster(String name) {
		this.master = name;
	}
	
	public void use() {
		System.out.println(master+" "+NAME);
	}

	public String getname() {
		return NAME;
	}
	
	@Override
	public boolean equals(Object obj) {
		CP2 cp = (CP2)obj;
		String n = cp.NAME;
		return n.equals(this.NAME);
	}
}

class CP3 extends CP{
	private final String NAME = " ";
	private String master;
	
	public void setMaster(String name) {
		this.master = name;
	}
	
	public void use() {
		System.out.println(master+" "+NAME);
	}
	
	public String getname() {
		return NAME;
	}
	
	@Override
	public boolean equals(Object obj) {
		CP3 cp = (CP3)obj;
		String n = cp.NAME;
		return n.equals(this.NAME);
	}
}