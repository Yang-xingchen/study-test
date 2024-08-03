package jdk17;

public class Record {

    public static void main(String[] args) {
        Point point = new Point(1, 1);
        System.out.println(point);
        System.out.println(point.x());
        System.out.println(point.y());
    }

    private record Point(int x, int y) {
    }

}
