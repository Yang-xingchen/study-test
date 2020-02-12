package temp;

public class Reference {

    public static void main(String[] args) {
        StringBuilder stringBuilder = new StringBuilder("start ");
        add(stringBuilder);
        System.out.println(stringBuilder);
        add2(stringBuilder);
        System.out.println(stringBuilder);
    }

    private static void add(StringBuilder stringBuilder) {
        stringBuilder.append("append0 ");
        stringBuilder = new StringBuilder("new ");
        stringBuilder.append("append1 ");
    }

    private static void add2(StringBuilder stringBuilder) {
        StringBuilder temp = stringBuilder;
        stringBuilder = new StringBuilder("new2 ");
        temp.append("append2 ");
        stringBuilder.append("append3 ");
    }

}
