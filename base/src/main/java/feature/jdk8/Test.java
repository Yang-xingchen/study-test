package feature.jdk8;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Test {
	public static void main(String[] args) {
		final String Lambda = "Lambda";
		/*
		 * Lambda简化
		 */
		new Thread(()->System.out.println(Lambda)).start();
		try{
			Thread.sleep(100);
		}catch (Exception e) {
			e.printStackTrace();
		}
		System.err.println("----------------------------------");
		/*
		 * 带参数的接口
		 */
		Say.staticSay((arg)->System.out.println("mian:"+arg));
		try{
			Thread.sleep(100);
		}catch (Exception e) {
			e.printStackTrace();
		}
		System.err.println("----------------------------------");
		/*
		 * 多条语句
		 */
		new Thread(()->{
			System.out.println("Lambda1");
			System.out.println("Lambda2");
			System.out.println("Lambda3");
		}) .start();
		try{
			Thread.sleep(100);
		}catch (Exception e) {
			e.printStackTrace();
		}
		System.err.println("----------------------------------");
		/*
		 * 方法重载,需要强制转换
		 */
		new Sayable().say((SayHello)()->{
			System.out.println("SAY...HELLO?");
		});
		new Sayable().say((SayBye)()->{
			System.out.println("SAY...BYE?");
		});
		new Sayable().say(()->{
			System.out.println("SAY...HELLO?");
		},()->{
			System.out.println("SAY...BYE?");
		});
		try{
			Thread.sleep(100);
		}catch (Exception e) {
			e.printStackTrace();
		}
		System.err.println("----------------------------------");
		/*
		 * 方法引用
		 */
		ArrayList<Integer> arr = new ArrayList<>();
		arr.add(1);
		arr.add(2);
		arr.add(3);
		arr.add(4);
		arr.add(5);
		arr.add(6);
		arr.forEach(System.out::println);
		try{
			Thread.sleep(100);
		}catch (Exception e) {
			e.printStackTrace();
		}
		System.err.println("----------------------------------");
		/*
		 * 方法引用
		 */
		Say say = Say.create(Say::new);
		say.say((arg)->System.out.println("main:arg = "+arg));
		try{
			Thread.sleep(100);
		}catch (Exception e) {
			e.printStackTrace();
		}
		System.err.println("----------------------------------");
		/*
		 * 
		 */
	    List<String> names = new ArrayList<>();	        
	    names.add("Google");
	    names.add("Runoob");
	    names.add("Taobao");
	    names.add("Baidu");
	    names.add("Sina");	  
	    names.stream().forEach(System.out::println);
		try{
			Thread.sleep(100);
		}catch (Exception e) {
			e.printStackTrace();
		}
		System.err.println("----------------------------------");
		/*
		 * 排序50个随机数
		 */
		new Random().ints(0,100).limit(50).sorted().forEach((e)->System.out.print(e+" "));
	}
	
}










