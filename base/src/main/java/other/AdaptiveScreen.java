package other;

public class AdaptiveScreen {

    public static void main(String[] args) {
        for (int len = 1; len < 50; len++) {
            int column = (int) Math.round(Math.sqrt(len));
            int row = len / column;
            int more = len - row * column;
            int index = 0;
            System.out.format("--- len: %d ---\n", len);
            for (int i = 0; i < column; i++) {
                for (int j = 0; j < row + (i < more ? 1 : 0); j++) {
                    System.out.printf("%02d ", ++index);
                }
                System.out.println();
            }
        }
    }

}
