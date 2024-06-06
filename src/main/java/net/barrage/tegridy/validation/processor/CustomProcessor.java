package net.barrage.tegridy.validation.processor;

import com.google.auto.service.AutoService;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import net.barrage.tegridy.validation.annotation.Custom;
import net.barrage.tegridy.validation.annotation.CustomList;

@SupportedAnnotationTypes({
  "net.barrage.tegridy.validation.annotation.Custom",
  "net.barrage.tegridy.validation.annotation.CustomList"
})
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor.class)
public class CustomProcessor extends AbstractProcessor {

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    roundEnv
        .getElementsAnnotatedWithAny(Set.of(Custom.class, CustomList.class))
        .forEach(
            annotatedElement -> {
              Custom[] customs = annotatedElement.getAnnotationsByType(Custom.class);
              for (Custom custom : customs) {
                validateMethod(annotatedElement, custom);
              }
            });
    return true;
  }

  private void validateMethod(Element annotatedElement, Custom custom) {
    TypeMirror validatorTypeMirror = getValidatorClassTypeMirror(custom);
    String validationMethod = custom.method();

    Elements elementUtils = processingEnv.getElementUtils();
    Types typeUtils = processingEnv.getTypeUtils();
    assert validatorTypeMirror != null;
    TypeElement validatorTypeElement = elementUtils.getTypeElement(validatorTypeMirror.toString());

    boolean methodFound = false;

    for (Element enclosedElement : validatorTypeElement.getEnclosedElements()) {
      if (enclosedElement instanceof ExecutableElement executableElement) {
        if (executableElement.getSimpleName().toString().equals(validationMethod)) {
          methodFound = true;

          if (!typeUtils.isSameType(
              executableElement.getReturnType(), typeUtils.getPrimitiveType(TypeKind.BOOLEAN))) {
            error(
                annotatedElement,
                "Method %s in class %s must return boolean required by @%s",
                validationMethod,
                validatorTypeElement.getQualifiedName(),
                Custom.class.getCanonicalName());
            return;
          }

          if (executableElement.getParameters().size() != 1) {
            error(
                annotatedElement,
                "Method %s in class %s must have exactly one parameter required by @%s",
                validationMethod,
                validatorTypeElement.getQualifiedName(),
                Custom.class.getCanonicalName());
            return;
          }

          TypeMirror parameterType = executableElement.getParameters().get(0).asType();
          if (!typeUtils.isAssignable(annotatedElement.asType(), parameterType)) {
            error(
                annotatedElement,
                "Method %s in class %s must accept a parameter of type %s to match @%s field",
                validationMethod,
                validatorTypeElement.getQualifiedName(),
                annotatedElement.asType().toString(),
                Custom.class.getCanonicalName());
          }

          break;
        }
      }
    }

    if (!methodFound) {
      error(
          annotatedElement,
          "Method %s not found in class %s required by @%s",
          validationMethod,
          validatorTypeElement.getQualifiedName(),
          Custom.class.getCanonicalName());
    }
  }

  private TypeMirror getValidatorClassTypeMirror(Custom custom) {
    try {
      custom._validatorClass();
    } catch (MirroredTypeException mte) {
      return mte.getTypeMirror();
    }
    return null;
  }

  private void error(Element e, String msg, Object... args) {
    processingEnv
        .getMessager()
        .printMessage(javax.tools.Diagnostic.Kind.ERROR, String.format(msg, args), e);
  }
}
