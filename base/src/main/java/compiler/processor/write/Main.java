package compiler.processor.write;

import compiler.StringJavaObject;

import javax.annotation.processing.Processor;
import javax.tools.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
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
        String code = """
                @ProcessorClass
                public interface ProcessorTest {
                    void print();
                    void show(String a);
                }
                """;

        // 获取类及实例
        compile(ANNOTATION_CODE + code, new WriteProcessor());
        Class<?> aClass = loadClass("ProcessorTestImpl");
        Object o = aClass.getConstructor().newInstance();

        // 执行方法
        aClass.getMethod("print").invoke(o);
        aClass.getMethod("show", String.class).invoke(o, "a");

        // 清除编译结果
        Files.delete(Paths.get(CLASS_NAME + ".class"));
        Files.delete(Paths.get("ProcessorClass.class"));
        Files.delete(Paths.get("ProcessorTestImpl.java"));
        Files.delete(Paths.get("ProcessorTestImpl.class"));
    }

    private static void compile(String code, Processor processor) throws IOException {
        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> listener = new DiagnosticCollector<>();
        try (StandardJavaFileManager fileManager = javaCompiler.getStandardFileManager(listener, Locale.CHINA, StandardCharsets.UTF_8)) {
            List<StringJavaObject> compilationUnits = List.of(new StringJavaObject(CLASS_NAME, code));
            JavaCompiler.CompilationTask task = javaCompiler.getTask(null, fileManager, listener, null, null, compilationUnits);
            task.setProcessors(List.of(processor));
            task.call();
            System.out.println("=== Diagnostics ===");
            listener.getDiagnostics().forEach(System.out::println);
        }
    }

    private static Class<?> loadClass(String cls) throws Exception {
        return new ClassLoader() {
            @Override
            protected Class<?> findClass(String name) throws ClassNotFoundException {
                try {
                    String file = name.replace(".", FileSystems.getDefault().getSeparator()) + ".class";
                    byte[] bytes = Files.readAllBytes(Paths.get(file));
                    return defineClass(name, bytes, 0, bytes.length);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }.loadClass(cls);
    }


}
