package net.barrage.tegridy.modification;

import java.util.HashMap;
import java.util.Map;
import lombok.SneakyThrows;
import net.barrage.tegridy.modification.annotation.ModifyCapitalize;
import net.barrage.tegridy.modification.annotation.ModifyCustom;
import net.barrage.tegridy.modification.annotation.ModifyLowerCase;
import net.barrage.tegridy.modification.annotation.ModifyNested;
import net.barrage.tegridy.modification.annotation.ModifyTrim;
import net.barrage.tegridy.modification.annotation.ModifyUpperCase;

/**
 * Enables instance modification through the use of `Modify*` annotations.
 *
 * <p>The single `modify` method of this interface should never have to be implemented manually as
 * it is intended to be used in conjunction with Modify annotations.
 *
 * <p>Its default implementation will reflect on the class and apply any modifiers specified through
 * annotations.
 *
 * <p>Annotations for modifying strings are readily available. In special cases where one needs
 * specific/custom modification the `ModifyCustom` annotation can be used. Additionally, any nested
 * classes can also be modified from a single call from the parent by annotating them with
 * `ModifyNested`.
 */
public interface Modify {

  static final Map<Class<?>, Class<?>> MODIFIERS =
      new HashMap<>() {
        {
          put(
              ModifyUpperCase.class,
              net.barrage.tegridy.modification.modifier.ModifyUpperCase.class);
          put(
              ModifyLowerCase.class,
              net.barrage.tegridy.modification.modifier.ModifyLowerCase.class);
          put(ModifyTrim.class, net.barrage.tegridy.modification.modifier.ModifyTrim.class);
          put(
              ModifyCapitalize.class,
              net.barrage.tegridy.modification.modifier.ModifyCapitalize.class);
        }
      };

  @SneakyThrows // TODO: Actually handle errors
  default void modify() {
    var clazz = this.getClass();

    var fields = clazz.getDeclaredFields();

    for (var field : fields) {

      field.setAccessible(true);
      var annotations = field.getDeclaredAnnotations();

      for (var annotation : annotations) {
        // Handle nested modifiers first
        if (annotation.annotationType().equals(ModifyNested.class)) {
          var fieldType = field.getType();
          for (var iface : fieldType.getInterfaces()) {

            // Make sure the child implements `Modify`
            if (iface.equals(Modify.class)) {
              var modifier = fieldType.getMethod("modify");
              var fieldValue = field.get(this);
              modifier.invoke(fieldValue);
              break;
            }
          }

          continue;
        }

        // Handle custom modifiers separately since they are not in the map
        // due to their dynamic nature
        if (annotation.annotationType().equals(ModifyCustom.class)) {
          var custom = (ModifyCustom) annotation;

          Object fieldValue = field.get(this);

          if (fieldValue != null) {
            var modInstance = custom.value().getConstructor().newInstance();
            var modifyMethod = custom.value().getMethod("doModify", field.getType());
            field.set(this, modifyMethod.invoke(modInstance, fieldValue));
          }

          continue;
        }

        var modifier = MODIFIERS.get(annotation.annotationType());

        if (modifier == null) {
          continue;
        }

        Object fieldValue = field.get(this);

        if (fieldValue != null) {
          var modInstance = modifier.getConstructor().newInstance();
          var modifyMethod = modifier.getMethod("doModify", field.getType());
          field.set(this, modifyMethod.invoke(modInstance, fieldValue));
        }
      }
    }
  }
}
