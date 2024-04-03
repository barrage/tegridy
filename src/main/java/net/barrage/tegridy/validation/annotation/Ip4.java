package net.barrage.tegridy.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.barrage.tegridy.validation.validator.Ip4Validator;

/** Annotation used to validate whether a string is a valid IPv4 address. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = Ip4Validator.class)
public @interface Ip4 {
  /**
   * The message to display when a validation error occurs.
   *
   * @return The validation error message.
   */
  String message() default "Invalid IPv4 address";

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
