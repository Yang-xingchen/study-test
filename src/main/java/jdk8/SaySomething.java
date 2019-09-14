package jdk8;

public interface SaySomething {
	void say(String str);
	default void say() {
		System.out.println("SaySomething: default say");
	};
}
