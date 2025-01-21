package other;

import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

public class StackTrace {

    public static void main(String[] args) {
        StackTrace stack = new StackTrace();
        invoke(stack);

        System.out.println("log---------------------------------------------------------------------------------------------");
        logTest();
    }

    private static void invoke(StackTrace s){
        s.show();
    }

    private static void logTest(){
        System.out.println(StackTrace.log("logTest"));
    }

    public void show(){
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        System.out.println(stackTraceElements.length);
        Stream.of(stackTraceElements).forEach(stackTraceElement -> {
            System.out.println("------------------------------------------------------------------------------------------------");
            System.out.println("getClassLoaderName: "+stackTraceElement.getClassLoaderName());
            System.out.println("getClassName: "+stackTraceElement.getClassName());
            System.out.println("getFileName: "+stackTraceElement.getFileName());
            System.out.println("getLineNumber: "+stackTraceElement.getLineNumber());
            System.out.println("getMethodName: "+stackTraceElement.getMethodName());
            System.out.println("getModuleName: "+stackTraceElement.getModuleName());
            System.out.println("getModuleVersion: "+stackTraceElement.getModuleVersion());
            System.out.println("isNativeMethod: "+stackTraceElement.isNativeMethod());
            try {
                System.out.println("("+Class.forName(stackTraceElement.getClassName()).getSimpleName()+".java:"+stackTraceElement.getLineNumber()+")");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    public static String log(String msg){
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[2];
        StringBuilder sb = new StringBuilder();
        sb.append(new Date());
        sb.append(" [").append(Thread.currentThread().getName()).append("] ");
        sb.append(stackTraceElement.getMethodName());
        sb.append("(").append(stackTraceElement.getClassName()).append(':').append(stackTraceElement.getLineNumber()).append(')');
        sb.append(" - ").append(Optional.ofNullable(msg).orElse(""));
        return sb.toString();
    }
}
