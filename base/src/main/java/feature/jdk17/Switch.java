package feature.jdk17;

public class Switch {

    public static void main(String[] args) {
        System.out.println(complete('+'));
        System.out.println(complete('-'));
        System.out.println(complete('*'));
        System.out.println(complete('/'));
    }

    private static String complete(char c) {
        return switch (c) {
            case '+' -> "1 + 1 = " + (1 + 1);
            case '-' -> "1 - 1 = " + (1 - 1);
            case '*' -> "1 * 1 = " + (1 * 1);
            case '/' -> "1 / 1 = " + (1 / 1);
            default -> throw new IllegalArgumentException();
        };
    }

}
