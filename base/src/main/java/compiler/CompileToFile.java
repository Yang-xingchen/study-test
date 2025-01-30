package compiler;

import javax.tools.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Locale;

public class CompileToFile {

    public static void main(String[] args) throws Exception {
        Path source = getSourcePath();
        Path target = compile(source);
        invoke(target);

        // 删除
        Files.deleteIfExists(source);
        Files.deleteIfExists(target);
    }

    /**
     * 获取源码文件path
     */
    private static Path getSourcePath() throws IOException {
        String code = """
                public class CompilerTest {
                    public static void main() {
                        System.out.println("hello world");
                    }
                }
                """;
        Path source = Paths.get("CompilerTest.java");
        Files.deleteIfExists(source);
        Files.writeString(source, code, StandardOpenOption.CREATE);
        return source;
    }

    /**
     * 编译
     */
    private static Path compile(Path source) throws IOException {
        Path target = Paths.get("CompilerTest.class");
        Files.deleteIfExists(target);

        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        try (StandardJavaFileManager fileManager = javaCompiler.getStandardFileManager(null, Locale.CHINA, StandardCharsets.UTF_8)) {
            Iterable<? extends JavaFileObject> javaFileObjects = fileManager.getJavaFileObjects(source);
            DiagnosticCollector<JavaFileObject> listener = new DiagnosticCollector<>();
            JavaCompiler.CompilationTask task = javaCompiler.getTask(null, fileManager, listener, null, null, javaFileObjects);
            task.call();
            listener.getDiagnostics().forEach(System.out::println);
        }
        return target;
    }

    /**
     * 调用
     */
    private static void invoke(Path target) throws Exception {
        new ClassLoader(){
            @Override
            protected Class<?> findClass(String name) throws ClassNotFoundException {
                try {
                    byte[] bytes = Files.readAllBytes(target);
                    return defineClass(name, bytes, 0, bytes.length);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
                .loadClass("CompilerTest")
                .getMethod("main")
                .invoke(null);
    }

}
