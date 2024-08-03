package jdk17;

public class InstanceOf {


    public static void main(String[] args) {
        System.out.println(pattern(1));
        System.out.println(pattern(1L));
        System.out.println(pattern("1"));
        System.out.println(pattern(1.1));
        System.out.println(pattern(1.1));
        System.out.println(pattern(new Point(1, 1)));
        // obj: null
        System.out.println(pattern(null));
        System.out.println(pattern(new Object()));
    }

    private static String pattern(Object o) {
        if (o instanceof Integer i) {
            return "int: " + i;
        } else if (o instanceof Long l) {
            return "long: " + l;
        } else if (o instanceof String s) {
            return "string: " + s;
        } else if (o instanceof Double d) {
            return "double: " + d;
        } else if (o instanceof Point p) {
            return "point: " + p;
        } else {
            return "obj: " + o;
        }
    }

    private record Point(int x, int y) {
    }

}
