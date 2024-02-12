package net.barrage.tegridy.modification;

import com.google.auto.service.AutoService;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import net.barrage.tegridy.modification.annotation.Modifier;
import net.barrage.tegridy.modification.annotation.Modify;

@SupportedAnnotationTypes("net.barrage.tegridy.modification.annotation.*")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor.class)
public class ModificationProcessor extends AbstractProcessor {

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    roundEnv.getElementsAnnotatedWithAny(Set.of(Modify.class))
        .forEach(annotatedElement -> {
          Modify[] compares = annotatedElement.getAnnotationsByType(Modify.class);
          for (Modify compare : compares) {
          }
        });
    return true;
  }
}