package jdk8;

public class Sayable {
	public void say(SayHello sayHello) {
		sayHello.say();
	}
	public void say(SayBye sayBye) {
		sayBye.say();
	}
	public void say(SayHello sayHello, SayBye sayBye) {
		sayHello.say();
		sayBye.say();
	}
}
