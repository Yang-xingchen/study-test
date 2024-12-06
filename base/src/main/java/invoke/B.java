package invoke;

public class B {

    public void print() {
        System.out.println("B 输出");
    }

    public void print2() {
        System.out.println("B 另一个输出");
    }

    public void print(String value) {
        System.out.println("B 带参数的输出: " + value);
    }

    public void print(String value1, String value2) {
        System.out.println("value1:" + value1 + " value2:" + value2);
    }

    public static void staticPrint(String value1, String value2) {
        System.out.println("value1:" + value1 + " value2:" + value2);
    }

}
