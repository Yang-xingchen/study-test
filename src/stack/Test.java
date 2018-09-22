package stack;


public class Test {

    public static void main(String[] args) {
        Stack stack = new Stack();
        invoke(stack);

        logTest();
    }

    private static void invoke(Stack s){
        s.show();
    }

    private static void logTest(){
        System.out.println(Stack.log("logTest"));
    }

}
