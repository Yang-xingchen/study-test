package compiler.processor.print;

import compiler.CompileToMemory;

import javax.annotation.processing.Processor;
import javax.tools.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;

public class Main {

    public static final String CLASS_NAME = "ProcessorTest";
    public static final String ANNOTATION_CODE = """
            import java.lang.annotation.ElementType;
            import java.lang.annotation.Retention;
            import java.lang.annotation.RetentionPolicy;
            import java.lang.annotation.Target;
            
            @Retention(RetentionPolicy.SOURCE)
            @Target(ElementType.TYPE)
            @interface ProcessorClass {
            }
            """;

    public static void main(String[] args) throws Exception {
        // 基础
        String baseCode = """
                @ProcessorClass
                public class ProcessorTest implements java.io.Serializable {
                    private static int psi;
                    private final String fs = "fs";
                    static {
                        System.out.println("static");
                    }
                    {
                        System.out.println("init");
                    }
                    private ProcessorTest() {}
                    private void show(int arg) {
                        System.out.println(arg);
                    }
                    private int sum(int a, int b) {
                        return a + b;
                    }
                    private void var(int first, int... a) {
                    }
                
                    private class Inner {
                        private int i = 1;
                    }
                }
                """;
        // 枚举
        String enumCode = """
                @ProcessorClass
                public enum ProcessorTest {
                    ONE(1), TWO(2), THREE(3);
                    public final int number;
                    private ProcessorTest(int number) {
                        this.number = number;
                    }
                }
                """;
        // 记录
        String recordCode = """
                @ProcessorClass
                public record ProcessorTest(int x, int y) {
                }
                """;
        // 泛型
        String genericsCode = """
                @ProcessorClass
                public class ProcessorTest<T> {
                    private T t;
                    private T t(T t) {
                        return t;
                    }
                    private <E extends T> T e(E e) {
                        return e;
                    }
                }
                """;
        // 注释
        String docCode = """
                /**
                 * class doc
                 */
                @ProcessorClass
                public class ProcessorTest {
                    /**
                     * field doc
                     */
                     private int i;
                     // not print
                     private int i2;
                     /**
                      * f doc
                      * @param a a doc
                      * @param b b doc
                      */
                     private void f(int a, int b) {
                     }
                }
                """;

        // 执行
        compile(ANNOTATION_CODE + baseCode, new PrintProcessor());

        // 清除编译结果
        Files.delete(Paths.get(CLASS_NAME + ".class"));
        Files.delete(Paths.get("ProcessorClass.class"));
    }

    private static void compile(String code, Processor processor) throws IOException {
        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> listener = new DiagnosticCollector<>();
        try (StandardJavaFileManager fileManager = javaCompiler.getStandardFileManager(listener, Locale.CHINA, StandardCharsets.UTF_8)) {
            List<CompileToMemory.StringJavaObject> compilationUnits = List.of(new CompileToMemory.StringJavaObject(CLASS_NAME, code));
            JavaCompiler.CompilationTask task = javaCompiler.getTask(null, fileManager, listener, null, null, compilationUnits);
            task.setProcessors(List.of(processor));
            task.call();
            System.out.println("=== Diagnostics ===");
            listener.getDiagnostics().forEach(System.out::println);
        }
    }

}
