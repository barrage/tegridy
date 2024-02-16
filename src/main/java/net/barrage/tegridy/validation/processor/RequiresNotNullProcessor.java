package net.barrage.tegridy.validation.processor;

import static net.barrage.tegridy.validation.processor.ProcessorUtils.elementHasField;

import com.google.auto.service.AutoService;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import net.barrage.tegridy.validation.annotation.RequiresNotNull;
import net.barrage.tegridy.validation.annotation.RequiresNotNullList;

@SupportedAnnotationTypes({
  "net.barrage.tegridy.validation.annotation.RequiresNotNull",
  "net.barrage.tegridy.validation.annotation.RequiresNotNullList",
})
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor.class)
public class RequiresNotNullProcessor extends AbstractProcessor {

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    roundEnv
        .getElementsAnnotatedWithAny(Set.of(RequiresNotNullList.class, RequiresNotNull.class))
        .forEach(
            annotatedElement -> {
              RequiresNotNull[] requiresNotNulls =
                  annotatedElement.getAnnotationsByType(RequiresNotNull.class);
              for (RequiresNotNull requiresNotNull : requiresNotNulls) {
                validateFieldsAndMethod(annotatedElement, requiresNotNull);
              }
            });
    return true;
  }

  private void validateFieldsAndMethod(Element annotatedElement, RequiresNotNull requiresNotNull) {
    String field = requiresNotNull.field();
    String[] requiresFields = requiresNotNull.requiresFields();

    if (!elementHasField(annotatedElement, field)) {
      processingEnv
          .getMessager()
          .printMessage(
              Diagnostic.Kind.ERROR,
              String.format(
                  "The class '%s' is missing required field by @RequiresNotNull: '%s'.",
                  annotatedElement.getSimpleName(), field),
              annotatedElement);
      return;
    }

    for (String fieldName : requiresFields) {
      if (!elementHasField(annotatedElement, fieldName)) {
        processingEnv
            .getMessager()
            .printMessage(
                Diagnostic.Kind.ERROR,
                String.format(
                    "The class '%s' is missing required field by @RequiresNotNull: '%s'.",
                    annotatedElement.getSimpleName(), fieldName),
                annotatedElement);
        return;
      }
    }
  }
}
