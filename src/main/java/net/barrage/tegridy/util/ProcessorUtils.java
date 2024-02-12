package net.barrage.tegridy.util;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

public class ProcessorUtils {
  public static boolean elementHasField(Element element, String fieldName) {
    return element.getEnclosedElements().stream().anyMatch(
        enclosed -> enclosed.getKind() == ElementKind.FIELD &&
            enclosed.getSimpleName().toString().equals(fieldName));
  }

  public static TypeMirror getFieldAsType(Element element, String fieldName, Elements elementUtils) {
    return element.getEnclosedElements().stream().filter(
            enclosed -> enclosed.getKind() == ElementKind.FIELD &&
                enclosed.getSimpleName().toString().equals(fieldName)).findFirst().map(Element::asType)
        .orElse(elementUtils.getTypeElement(Object.class.getCanonicalName()).asType());
  }
}
