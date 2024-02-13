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
import net.barrage.tegridy.validation.annotation.EnumList;

@SupportedAnnotationTypes(
    "net.barrage.tegridy.validation.annotation.EnumList")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor.class)
public class EnumListProcessor extends AbstractProcessor {

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    Types typeUtils = processingEnv.getTypeUtils();
    TypeElement stringElement =
        processingEnv.getElementUtils().getTypeElement(String.class.getCanonicalName());
    TypeMirror stringArrayElementType = typeUtils.getArrayType(stringElement.asType());
    roundEnv.getElementsAnnotatedWith(EnumList.class)
        .forEach(annotatedElement -> {
          TypeMirror annotatedElementType = annotatedElement.asType();

          if (!typeUtils.isAssignable(annotatedElementType, stringArrayElementType)) {

            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                String.format("Annotation @%s is not applicable to type %s.",
                    EnumList.class.getSimpleName(), annotatedElementType.toString()),
                annotatedElement);
          }
        });
    return true;
  }
}
