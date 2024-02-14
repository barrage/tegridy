package net.barrage.tegridy.validation.processor;

import static net.barrage.tegridy.util.ProcessorUtils.elementHasField;

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
import net.barrage.tegridy.validation.annotation.RequiresNull;
import net.barrage.tegridy.validation.annotation.RequiresNullList;

@SupportedAnnotationTypes({
    "net.barrage.tegridy.validation.annotation.RequiresNull",
    "net.barrage.tegridy.validation.annotation.RequiresNullList",
})
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor.class)
public class RequiresNullProcessor extends AbstractProcessor {

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    roundEnv.getElementsAnnotatedWithAny(Set.of(RequiresNullList.class, RequiresNull.class))
        .forEach(annotatedElement -> {
          RequiresNull[] requiresNulls =
              annotatedElement.getAnnotationsByType(RequiresNull.class);
          for (RequiresNull requiresNull : requiresNulls) {
            validateFieldsAndMethod(annotatedElement, requiresNull);
          }
        });
    return true;
  }

  private void validateFieldsAndMethod(Element annotatedElement, RequiresNull requiresNull) {
    String field = requiresNull.field();
    String[] forbiddenFields = requiresNull.forbiddenFields();

    if (!elementHasField(annotatedElement, field)) {
      processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
          String.format("The class '%s' is missing required field by @RequiresNull: '%s'.",
              annotatedElement.getSimpleName(), field), annotatedElement);
      return;
    }

    for (String fieldName : forbiddenFields) {
      if (!elementHasField(annotatedElement, fieldName)) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
            String.format("The class '%s' is missing required field by @RequiresNull: '%s'.",
                annotatedElement.getSimpleName(), fieldName), annotatedElement);
        return;
      }
    }
  }
}
