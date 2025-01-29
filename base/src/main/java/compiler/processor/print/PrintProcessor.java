package compiler.processor.print;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import java.util.Set;

@SupportedAnnotationTypes("ProcessorClass")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class PrintProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("=== process: " + annotations + " ===");
        System.out.println("sourceVersion: " + processingEnv.getSourceVersion());
        System.out.println("Locale: " + processingEnv.getLocale());
        System.out.println("Options: " + processingEnv.getOptions());
        System.out.println("----------------------------------------");
        roundEnv.getRootElements().forEach(element -> printRoot(element, 1));
        return false;
    }

    private void printRoot(Element element, int prefix) {
        print(prefix, element.getKind() + ": " + element.getSimpleName());
        print(prefix + 1, "Modifiers: " + element.getModifiers());
        print(prefix + 1, "asType: " + element.asType());
        Elements elementUtils = processingEnv.getElementUtils();
        print(prefix + 1, "Origin: " + elementUtils.getOrigin(element));
        print(prefix + 1, "doc: " + elementUtils.getDocComment(element));
        switch (element) {
            case PackageElement packageElement -> printPackage(packageElement, prefix + 1);
            case ModuleElement moduleElement -> printModule(moduleElement, prefix + 1);
            case TypeElement typeElement -> printType(typeElement, prefix + 1);
            case VariableElement variableElement -> printVar(variableElement, prefix + 1);
            case ExecutableElement executableElement -> printExec(executableElement, prefix + 1);
            case TypeParameterElement parameterElement -> printParam(parameterElement, prefix + 1);
            case RecordComponentElement recordComponentElement -> printRecord(recordComponentElement, prefix + 1);
            default -> throw new IllegalStateException("Unexpected value: " + element);
        }
        element.getEnclosedElements().forEach(e -> printRoot(e, prefix + 1));
        processingEnv.getMessager().printNote("message", element);
    }

    private void print(int prefix, String print) {
        for (int i = 0; i < prefix; i++) {
            System.out.print("   ");
        }
        System.out.println(" " + print);
    }

    private void printPackage(PackageElement element, int prefix) {
        print(prefix, "QualifiedName: " + element.getQualifiedName());
        print(prefix, "SimpleName: " + element.getSimpleName());

    }

    private void printModule(ModuleElement element, int prefix) {
        print(prefix, "QualifiedName: " + element.getQualifiedName());
        print(prefix, "SimpleName: " + element.getSimpleName());
        print(prefix, "Open: " + element.isOpen());
        print(prefix, "Unnamed: " + element.isUnnamed());
    }

    private void printType(TypeElement element, int prefix) {
        print(prefix, "NestingKind: " + element.getNestingKind());
        print(prefix, "QualifiedName: " + element.getQualifiedName());
        print(prefix, "SimpleName: " + element.getSimpleName());
        print(prefix, "Superclass: " + element.getSuperclass());
        print(prefix, "Interfaces: " + element.getInterfaces());
        print(prefix, "TypeParameters");
        element.getTypeParameters().forEach(e -> printRoot(e, prefix + 1));
        print(prefix, "RecordComponents: " + element.getRecordComponents());
        print(prefix, "PermittedSubclasses: " + element.getPermittedSubclasses());
    }

    private void printVar(VariableElement element, int prefix) {
        print(prefix, "ConstantValue: " + element.getConstantValue());
        print(prefix, "SimpleName: " + element.getSimpleName());
    }

    private void printExec(ExecutableElement element, int prefix) {
        print(prefix, "TypeParameters");
        element.getTypeParameters().forEach(e -> printRoot(e, prefix + 1));
        print(prefix, "ReturnType: " + element.getReturnType());
        print(prefix, "Parameters");
        element.getParameters().forEach(e -> printRoot(e, prefix + 1));
        print(prefix, "ReceiverType: " + element.getReceiverType());
        print(prefix, "VarArgs: " + element.isVarArgs());
        print(prefix, "Default: " + element.isDefault());
        print(prefix, "ThrownTypes: " + element.getThrownTypes());
        print(prefix, "DefaultValue: " + element.getDefaultValue());
        print(prefix, "SimpleName: " + element.getSimpleName());
    }

    private void printParam(TypeParameterElement element, int prefix) {
        print(prefix, "ReturnType: " + element.getGenericElement());
        print(prefix, "BoundsType: " + element.getBounds());
    }

    private void printRecord(RecordComponentElement element, int prefix) {
        print(prefix, "TypeParameters");
        printRoot(element.getAccessor(), prefix + 1);
    }

}
