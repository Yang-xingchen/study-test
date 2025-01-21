package feature.jdk21;

public class SwitchPattern {

    public static void main(String[] args) {
        System.out.println(pattern(1));
        System.out.println(pattern(1L));
        System.out.println(pattern("1"));
        System.out.println(pattern("11"));
        System.out.println(pattern("111"));
        System.out.println(pattern(1.1));
        System.out.println(pattern(new Point1(1, 1)));
        System.out.println(pattern(new Point2(1, 1)));
        System.out.println(pattern(1.1));
        System.out.println(pattern(null));
        System.out.println(pattern(new Object()));
    }

    private static String pattern(Object o) {
        return switch (o) {
            case Integer i -> "int: " + i;
            case Long l -> "long: " + l;
            case String s when s.length() == 1 -> "string(1): " + s;
            case String s when s.length() == 2 -> "string(2): " + s;
            case String s -> "string(*): " + s;
            case Double d -> "double: " + d;
            case Point1 p -> "point: " + p;
            case Point2(int x, int y) -> "point2: (" + x + ", " + y + ")";
            case null -> "null";
            default -> "obj: " + o;
        };
    }

    private record Point1(int x, int y) {
        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }

    private record Point2(int x, int y) {

    }

}
