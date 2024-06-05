package net.barrage.tegridy.validation.processor;

import static net.barrage.tegridy.validation.processor.ProcessorUtils.elementHasField;
import static net.barrage.tegridy.validation.processor.ProcessorUtils.getFieldAsType;

import com.google.auto.service.AutoService;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import net.barrage.tegridy.validation.annotation.Scheme;
import net.barrage.tegridy.validation.annotation.SchemeList;

@SupportedAnnotationTypes({
  "net.barrage.tegridy.validation.annotation.Scheme",
  "net.barrage.tegridy.validation.annotation.SchemeList"
})
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor.class)
public class SchemeProcessor extends AbstractProcessor {

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    roundEnv
        .getElementsAnnotatedWithAny(Set.of(Scheme.class, SchemeList.class))
        .forEach(
            annotatedElement -> {
              Scheme[] schemes = annotatedElement.getAnnotationsByType(Scheme.class);
              for (Scheme scheme : schemes) {
                validateFieldsAndMethod(annotatedElement, scheme);
              }
            });
    return true;
  }

  private void validateFieldsAndMethod(Element annotatedElement, Scheme scheme) {
    String baseField = scheme.baseField();
    String[] argumentFields = scheme.argumentFields();
    String validationMethod = scheme.method();

    Types typeUtils = processingEnv.getTypeUtils();
    Elements elementUtils = processingEnv.getElementUtils();

    if (!elementHasField(annotatedElement, baseField)) {
      processingEnv
          .getMessager()
          .printMessage(
              Diagnostic.Kind.ERROR,
              String.format(
                  "The class '%s' is missing 'baseField' required by @%s: '%s'.",
                  annotatedElement.getSimpleName(), Scheme.class.getSimpleName(), baseField),
              annotatedElement);
      return;
    }
    TypeMirror[] argumentFieldsType = new TypeMirror[argumentFields.length + 1];
    TypeMirror baseFieldType = getFieldAsType(annotatedElement, baseField, elementUtils);
    argumentFieldsType[0] = baseFieldType;

    for (int i = 0; i < argumentFields.length; i++) {

      if (!elementHasField(annotatedElement, argumentFields[i])) {
        processingEnv
            .getMessager()
            .printMessage(
                Diagnostic.Kind.ERROR,
                String.format(
                    "The class '%s' is missing one of argument fields required by @%s: '%s'.",
                    annotatedElement.getSimpleName(),
                    Scheme.class.getSimpleName(),
                    argumentFields[i]),
                annotatedElement);
        return;
      }
      argumentFieldsType[i + 1] = getFieldAsType(annotatedElement, argumentFields[i], elementUtils);
    }

    var enclosedStream = annotatedElement.getEnclosedElements().stream();

    var element =
        enclosedStream
            .filter(
                enclosed ->
                    enclosed.getKind() == ElementKind.METHOD
                        && enclosed.getSimpleName().toString().equals(validationMethod))
            .map(ExecutableElement.class::cast)
            .findFirst();

    if (element.isPresent()) {
      validateMethod(
          element.get(),
          argumentFieldsType,
          typeUtils,
          elementUtils,
          annotatedElement,
          validationMethod);
    } else {
      processingEnv
          .getMessager()
          .printMessage(
              Diagnostic.Kind.ERROR,
              String.format(
                  "Method '%s' required by @%s not found in class '%s'.",
                  validationMethod, Scheme.class.getSimpleName(), annotatedElement.getSimpleName()),
              annotatedElement);
    }
  }

  private void validateMethod(
      ExecutableElement method,
      TypeMirror[] argumentTypes,
      Types typeUtils,
      Elements elementUtils,
      Element annotatedElement,
      String validationMethod) {

    TypeMirror returnType = method.getReturnType();
    boolean isBooleanType =
        returnType.getKind() == TypeKind.BOOLEAN
            || typeUtils.isSameType(
                returnType, elementUtils.getTypeElement(Boolean.class.getCanonicalName()).asType());

    if (!isBooleanType) {
      processingEnv
          .getMessager()
          .printMessage(
              Diagnostic.Kind.ERROR,
              String.format(
                  "Method '%s' in class '%s' must return boolean as required by @%s.",
                  validationMethod, annotatedElement.getSimpleName(), Scheme.class.getSimpleName()),
              annotatedElement);
      return;
    }

    List<? extends VariableElement> parameters = method.getParameters();
    boolean isSignatureValid = true;

    if (parameters.size() != argumentTypes.length) {
      isSignatureValid = false;
    } else {
      for (int i = 0; i < argumentTypes.length; i++) {
        if (!typeUtils.isSameType(parameters.get(i).asType(), argumentTypes[i])) {
          isSignatureValid = false;
          break;
        }
      }
    }

    if (!isSignatureValid) {
      String typesString =
          Arrays.stream(argumentTypes)
              .map(
                  argumentType -> {
                    String[] typeSplit = argumentType.toString().split(" ");
                    return String.format("'%s'", typeSplit[typeSplit.length - 1]);
                  })
              .collect(Collectors.joining(", "));
      processingEnv
          .getMessager()
          .printMessage(
              Diagnostic.Kind.ERROR,
              String.format(
                  "Method '%s' in class '%s' must have exactly %s parameters matching types of %s as required by @%s.",
                  validationMethod,
                  annotatedElement.getSimpleName(),
                  argumentTypes.length,
                  typesString,
                  Scheme.class.getSimpleName()),
              annotatedElement);
    }
  }
}
