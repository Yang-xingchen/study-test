package compiler.processor.toPath;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@SupportedAnnotationTypes("compiler.processor.ProcessorClass")
@SupportedSourceVersion(SourceVersion.RELEASE_16)
public class MyProcessor extends AbstractProcessor {

    public MyProcessor() {
        System.out.println("processor...");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Path path = Paths.get("./process.txt");
        String collect = roundEnv.getRootElements()
                .stream()
                .filter(TypeElement.class::isInstance)
                .map(element -> ((TypeElement) element).getQualifiedName())
                .map(Objects::toString)
                .collect(Collectors.joining("\n"));
        try {
            Files.writeString(path, collect, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
