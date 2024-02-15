package net.barrage.tegridy.validation.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import net.barrage.tegridy.validation.validator.CustomValidator;

/**
 * The {@code Custom} annotation is designed to define a generic validation constraint that can be
 * customized through the specification of a base field, a set of argument fields, and a validation
 * method. This flexibility allows for the implementation of complex validation logic that can be
 * reused across different types.
 *
 * <p>This annotation is intended for use at the class level, enabling the validation of class
 * fields based on the specified custom logic encapsulated within the validation method.
 *
 * <p>Attributes: - {@code message}: Provides a default message for validation failures. This can be
 * overridden when the annotation is applied. - {@code groups}: Specifies the validation groups with
 * which the constraint is associated, allowing for selective validation. - {@code payload}: Can be
 * used by clients of the Bean Validation API to assign custom payload objects to the constraint. -
 * {@code baseField}: The name of the field that is the primary target of the validation. - {@code
 * argumentFields}: An optional array of field names that the validation method can use as
 * additional arguments for performing the validation logic. - {@code method}: The name of the
 * method that contains the custom validation logic. This method should be implemented within the
 * class to which the annotation is applied and is expected to return a boolean indicating the
 * validation result.
 *
 * <p>Example usage:
 *
 * <pre>
 * &#64;Custom(
 *     baseField = "startDate",
 *     argumentFields = {"endDate"},
 *     method = "validateDateRange",
 *     message = "Start date must be before end date"
 * )
 * public class DateRange {
 *     private LocalDate startDate;
 *     private LocalDate endDate;
 *
 *     public boolean validateDateRange() {
 *         if (startDate != null && endDate != null) {
 *             return startDate.isBefore(endDate);
 *         }
 *         return true;
 *     }
 * }
 * </pre>
 *
 * @see CustomValidator The validator implementing the constraint logic based on the defined custom
 *     method.
 */
@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = CustomValidator.class)
@Repeatable(value = CustomList.class)
@Documented
public @interface Custom {

  /**
   * Default message to be used in case of validation failure.
   *
   * @return The validation error message template.
   */
  String message() default "Custom validation failed";

  /**
   * Specifies the validation groups with which this constraint is associated.
   *
   * @return The validation groups.
   */
  Class<?>[] groups() default {};

  /**
   * Can be used by clients of the Bean Validation API to assign custom payload objects to a
   * constraint.
   *
   * @return The custom payload classes.
   */
  Class<? extends Payload>[] payload() default {};

  /**
   * The name of the base field involved in the validation.
   *
   * @return The base field name.
   */
  String baseField();

  /**
   * Optional array of field names that serve as arguments to the custom validation method.
   *
   * @return The argument fields.
   */
  String[] argumentFields() default {};

  /**
   * The name of the method that encapsulates the custom validation logic.
   *
   * @return The method name.
   */
  String method();
}
