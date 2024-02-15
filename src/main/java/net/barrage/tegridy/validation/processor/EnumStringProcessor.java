package net.barrage.tegridy.validation.processor;

import com.google.auto.service.AutoService;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import net.barrage.tegridy.validation.annotation.EnumString;

@SupportedAnnotationTypes("net.barrage.tegridy.validation.annotation.EnumString")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor.class)
public class EnumStringProcessor extends AbstractProcessor {

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    Types typeUtils = processingEnv.getTypeUtils();
    TypeElement charSequenceElement =
        processingEnv.getElementUtils().getTypeElement(CharSequence.class.getCanonicalName());
    TypeMirror charSequenceType = charSequenceElement.asType();
    roundEnv
        .getElementsAnnotatedWith(EnumString.class)
        .forEach(
            annotatedElement -> {
              TypeMirror annotatedElementType = annotatedElement.asType();

              if (!typeUtils.isAssignable(annotatedElementType, charSequenceType)) {

                processingEnv
                    .getMessager()
                    .printMessage(
                        Diagnostic.Kind.ERROR,
                        String.format(
                            "Annotation @%s is not applicable to type %s.",
                            EnumString.class.getSimpleName(), annotatedElementType.toString()),
                        annotatedElement);
              }
            });
    return true;
  }
}
