package compiler.processor.write;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.tools.JavaFileObject;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Set;
import java.util.stream.Collectors;

@SupportedAnnotationTypes("ProcessorClass")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class WriteProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // 仅处理接口
        roundEnv.getRootElements()
                .stream()
                .filter(TypeElement.class::isInstance)
                .map(TypeElement.class::cast)
                .filter(element -> element.getKind() == ElementKind.INTERFACE)
                .forEach(this::write);
        return false;
    }

    private void write(TypeElement root) {
        try {
            JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(root.getSimpleName() + "Impl");
            try (OutputStream outputStream = sourceFile.openOutputStream();
                 OutputStreamWriter writer = new OutputStreamWriter(outputStream)) {
                writer.write("public class " + root.getSimpleName() + "Impl implements " + root + " {\n");
                // 写入方法
                root.getEnclosedElements()
                        .stream()
                        .filter(ExecutableElement.class::isInstance)
                        .map(ExecutableElement.class::cast)
                        .forEach(element -> {
                            if (!element.getReturnType().equals(processingEnv.getTypeUtils().getNoType(TypeKind.VOID))) {
                                processingEnv.getMessager().printError("仅支持void返回", element);
                            }
                            String parameters = element.getParameters()
                                    .stream()
                                    .map(e -> e.asType() + " " + e.getSimpleName())
                                    .collect(Collectors.joining(", "));
                            try {
                                writer.write("    @Override\n");
                                writer.write("    public void " + element.getSimpleName() + "(" + parameters + ") {\n");
                                writer.write("        System.out.println(\"" + element.getSimpleName() + "(" + parameters + ") print\");\n");
                                writer.write("    }\n");
                            } catch (Exception e) {
                                processingEnv.getMessager().printError("失败: " + e.getMessage(), element);
                            }
                        });
                writer.write("}");
            }
        } catch (Exception e) {
            processingEnv.getMessager().printError("失败: " + e.getMessage(), root);
        }
    }
}
