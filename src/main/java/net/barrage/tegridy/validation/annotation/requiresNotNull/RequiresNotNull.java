package net.barrage.tegridy.validation.annotation.requiresNotNull;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import net.barrage.tegridy.validation.validators.RequiresNotNullValidator;

/**
 * Schema violation constraint that
 * validates that fields {@code requiresFieldNames} are not null if
 * field {@code field} is present.
 **/
@Target({ TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = RequiresNotNullValidator.class)
@Repeatable(value = RequiresNotNullList.class)
@Documented
public @interface RequiresNotNull {

  /**
   * The field which, when present, requires other fields to be present as well.
   *
   * @return The field name.
   */
  String field();

  /**
   * The fields which are required to be present when {@code value} is present.
   *
   * @return An array of field names.
   */
  String[] requiresFields();

  String message() default "{RequiresNotNull.message}";

  Class<?>[] groups() default { };

  Class<? extends Payload>[] payload() default { };

}

