package net.barrage.tegridy.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.NotNull;
import java.lang.reflect.Modifier;
import net.barrage.tegridy.validation.annotation.RequireAny;

/**
 * Validator that requires at least one of the classes' fields to be non-null. Useful for DTOs whose
 * params can vary.
 */
public class RequireAnyValidator implements ConstraintValidator<RequireAny, Object> {
  private String message;

  @Override
  public void initialize(RequireAny annotation) {
    this.message = annotation.message();
  }

  @Override
  public boolean isValid(@NotNull Object o, ConstraintValidatorContext context) {
    for (var field : o.getClass().getDeclaredFields()) {
      // Skip static fields
      if (Modifier.isStatic(field.getModifiers())) {
        continue;
      }

      field.setAccessible(true);
      try {
        var value = field.get(o);
        if (value != null) {
          return true;
        }
      } catch (IllegalArgumentException | IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }

    context.disableDefaultConstraintViolation();
    context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    context.disableDefaultConstraintViolation();

    return false;
  }
}
