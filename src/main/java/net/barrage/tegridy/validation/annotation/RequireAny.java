package net.barrage.tegridy.validation.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import net.barrage.tegridy.validation.validator.RequireAnyValidator;

/** Validates that at least one of the classes' fields is non-null. */
@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = RequireAnyValidator.class)
public @interface RequireAny {
  /**
   * The message to display when a validation error occurs. Can include {@code {value}} placeholder
   * to list valid enum names.
   *
   * @return The validation error message template.
   */
  String message() default "Object must have at least one non-null field";

  /**
   * Can be used by clients of the Bean Validation API to assign custom payload objects to a
   * constraint.
   *
   * @return The custom payload classes.
   */
  Class<? extends Payload>[] payload() default {};

  /**
   * Specifies the validation groups with which this constraint is associated.
   *
   * @return The validation groups.
   */
  Class<?>[] groups() default {};
}
