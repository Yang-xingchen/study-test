package jdk17;

public class TextBlock {

    public static void main(String[] args) {
        String sql = """
                SELECT *
                FROM user
                where id=?
                """;
        System.out.println(sql);
    }

}
