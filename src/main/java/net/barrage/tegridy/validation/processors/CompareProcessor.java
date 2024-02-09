package net.barrage.tegridy.validation.processors;

import com.google.auto.service.AutoService;
import java.util.List;
import java.util.Set;
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
import net.barrage.tegridy.validation.annotation.compare.Compare;
import net.barrage.tegridy.validation.annotation.compare.CompareList;

@SupportedAnnotationTypes({ "net.barrage.tegridy.validation.annotations.compare.Compare",
    "net.barrage.tegridy.validation.annotations.compare.CompareList" })
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor.class)
public class CompareProcessor extends AbstractProcessor {

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    roundEnv.getElementsAnnotatedWithAny(Set.of(CompareList.class, Compare.class))
        .forEach(annotatedElement -> {
          Compare[] compares = annotatedElement.getAnnotationsByType(Compare.class);
          for (Compare compare : compares) {
            validateFieldsAndMethod(annotatedElement, compare);
          }
        });
    return true;
  }

  private void validateFieldsAndMethod(Element annotatedElement, Compare compare) {
    String baseField = compare.baseField();
    String comparisonField = compare.comparisonField();
    String comparisonMethod = compare.comparisonMethod();

    Types typeUtils = processingEnv.getTypeUtils();
    Elements elementUtils = processingEnv.getElementUtils();

    if (!elementHasField(annotatedElement, baseField) ||
        !elementHasField(annotatedElement, comparisonField)) {
      processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
          String.format("The class '%s' is missing required fields by @Compare: '%s', '%s'.",
              annotatedElement.getSimpleName(), baseField, comparisonField),
          annotatedElement);
      return;
    }

    TypeMirror baseFieldType = getFieldAsType(annotatedElement, baseField, elementUtils);
    TypeMirror comparisonFieldType =
        getFieldAsType(annotatedElement, comparisonField, elementUtils);

    annotatedElement.getEnclosedElements().stream()
        .filter(enclosed -> enclosed.getKind() == ElementKind.METHOD &&
            enclosed.getSimpleName().toString().equals(comparisonMethod))
        .map(ExecutableElement.class::cast)
        .findFirst()
        .ifPresentOrElse(
            method -> validateMethod(method, baseFieldType, comparisonFieldType, typeUtils,
                elementUtils, annotatedElement, comparisonMethod),
            () -> processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                String.format("Method '%s' required by @Compare not found in class '%s'.",
                    comparisonMethod, annotatedElement.getSimpleName()), annotatedElement));
  }

  private boolean elementHasField(Element element, String fieldName) {
    return element.getEnclosedElements().stream()
        .anyMatch(enclosed -> enclosed.getKind() == ElementKind.FIELD &&
            enclosed.getSimpleName().toString().equals(fieldName));
  }

  private TypeMirror getFieldAsType(Element element, String fieldName, Elements elementUtils) {
    return element.getEnclosedElements().stream()
        .filter(enclosed -> enclosed.getKind() == ElementKind.FIELD &&
            enclosed.getSimpleName().toString().equals(fieldName))
        .findFirst()
        .map(Element::asType)
        .orElse(elementUtils.getTypeElement(Object.class.getCanonicalName()).asType());
  }

  private void validateMethod(ExecutableElement method, TypeMirror baseFieldType,
                              TypeMirror comparisonFieldType,
                              Types typeUtils, Elements elementUtils, Element annotatedElement,
                              String comparisonMethod) {
    TypeMirror returnType = method.getReturnType();
    boolean isBooleanType = returnType.getKind() == TypeKind.BOOLEAN ||
        typeUtils.isSameType(returnType,
            elementUtils.getTypeElement(Boolean.class.getCanonicalName()).asType());

    if (!isBooleanType) {
      processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
          String.format("Method '%s' in class '%s' must return boolean as required by @Compare.",
              comparisonMethod, annotatedElement.getSimpleName()), annotatedElement);
      return;
    }

    List<? extends VariableElement> parameters = method.getParameters();
    if (parameters.size() != 2 ||
        !typeUtils.isSameType(parameters.get(0).asType(), baseFieldType) ||
        !typeUtils.isSameType(parameters.get(1).asType(), comparisonFieldType)) {
      processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
          String.format(
              "Method '%s' in class '%s' must have exactly 2 parameters matching types of '%s' and '%s' as required by @Compare.",
              comparisonMethod, annotatedElement.getSimpleName(), baseFieldType,
              comparisonFieldType), annotatedElement);
    }
  }
}
