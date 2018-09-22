package jdk8;

public interface SayHello {
	public void say();
	default void sayHello() {
		System.out.println("hello world");
	}
}
