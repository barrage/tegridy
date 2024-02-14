package net.barrage.tegridy.modification;

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
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import net.barrage.tegridy.modification.annotation.ModifyCapitalize;
import net.barrage.tegridy.modification.annotation.ModifyCustom;
import net.barrage.tegridy.modification.annotation.ModifyLowerCase;
import net.barrage.tegridy.modification.annotation.ModifyNested;
import net.barrage.tegridy.modification.annotation.ModifyTrim;
import net.barrage.tegridy.modification.annotation.ModifyUpperCase;

@SupportedAnnotationTypes("net.barrage.tegridy.modification.annotation.*")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor.class)
public class ModificationProcessor extends AbstractProcessor {

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

    // Process default string modifiers
    var elements = roundEnv.getElementsAnnotatedWithAny(
        Set.of(ModifyLowerCase.class, ModifyUpperCase.class, ModifyTrim.class,
            ModifyCapitalize.class));

    for (var element : elements) {
      if (element.getKind() != ElementKind.FIELD) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
            "String modifiers can only be applied to fields", element);
      }

      VariableElement field = (VariableElement) element;
      var strType = processingEnv.getElementUtils().getTypeElement("java.lang.String").asType();
      var typesMatch = processingEnv.getTypeUtils().isSameType(field.asType(), strType);

      if (!typesMatch) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
            "Field '" + field.getSimpleName() +
                "' must be of type String. Required because of String modifier.", element);
      }
    }

    // Process custom modifiers
    var customElements = roundEnv.getElementsAnnotatedWithAny(Set.of(ModifyCustom.class));

    for (var element : customElements) {
      if (element.getKind() != ElementKind.FIELD) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
            "Custom modifiers can only be applied to fields", element);
      }

      VariableElement field = (VariableElement) element;

      // The following is a hack to get the type in the annotation.
      TypeMirror customAnnot = null;
      try {
        var _thisWillThrow = element.getAnnotation(ModifyCustom.class).value();
      } catch (MirroredTypeException e) {
        customAnnot = e.getTypeMirror();
      }

      // Get the class in question and check whether it has the doModify method
      TypeElement clazz = processingEnv.getElementUtils().getTypeElement(customAnnot.toString());

      List<? extends Element> methods = processingEnv.getElementUtils().getAllMembers(clazz);

      for (Element method : methods) {
        // Horrendously janky, but then again this whole language is
        if (method.getSimpleName().toString().equals("doModify")) {
          var modArg = method.toString().split(method.getSimpleName().toString() + "\\(")[1];
          var input = modArg.substring(0, modArg.length() - 1);
          var type = processingEnv.getElementUtils().getTypeElement(input).asType();
          var typesMatch = processingEnv.getTypeUtils().isSameType(field.asType(), type);
          if (!typesMatch) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                "Field '" + field.getSimpleName() +
                    "' of type (" + field.asType() + ") does not match the modifier type (" + type +
                    ").",
                element);
          }
        }
      }
    }

    // Process nested modifiers
    var nestedElements = roundEnv.getElementsAnnotatedWithAny(Set.of(ModifyNested.class));

    for (var element : nestedElements) {
      if (element.getKind() != ElementKind.FIELD) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
            "Nested modifiers can only be applied to fields", element);
      }

      VariableElement field = (VariableElement) element;

      TypeElement type = processingEnv.getElementUtils().getTypeElement(field.asType().toString());
      List<? extends Element> methods = processingEnv.getElementUtils().getAllMembers(type);

      var hasModify = methods.stream()
          .map(el -> el.getSimpleName().toString())
          .toList()
          .contains("modify");

      if (!hasModify) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
            "@NestedModify is valid only on fields that are classes implementing `Modify`." +
                " Found '" + type.toString() + "'.");
      }
    }

    return true;
  }
}