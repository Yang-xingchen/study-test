package feature.jdk8;

public interface SayBye {
	public void say();
	default void sayBye() {
		System.out.println("good bye");
	}
}
