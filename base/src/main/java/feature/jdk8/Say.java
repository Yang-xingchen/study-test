package feature.jdk8;

import java.util.function.Supplier;

public class Say implements SayHello, SayBye{
	public void say(SaySomething s) {
		System.out.println("Say.say : "+this.hashCode());
		s.say("say");
	}
	public static void staticSay(SaySomething s) {
		System.out.println("staticSay.say");
		s.say("staticSay arg");
		s.say();
	}
	public static Say create(final Supplier<Say> s) {
		return s.get();
	}
	@Override
	public void say() {
		System.out.println("say...");
	}
}
