package net.barrage.tegridy.validation.annotation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import net.barrage.tegridy.validation.validator.RequiresNullValidator;

/**
 * Defines a validation constraint that enforces certain fields to be null if a specific field is
 * present. This annotation is intended for use on classes to establish validation rules that ensure
 * if a specific condition is met (specific field is present), then other specified fields must be
 * null.
 *
 * <p>This annotation is repeatable, allowing multiple conditional null validations to be applied to
 * a class.
 *
 * <p>Usage example:
 *
 * <pre>
 * &#64;RequiresNull(field = "conditionalField", forbiddenFields = {"forbiddenField1", "forbiddenField2"},
 * message = "forbiddenField1 and forbiddenField2 must be null if conditionalField is present")
 * public class MyClass {
 *     private String conditionalField;
 *     private String forbiddenField1;
 *     private String forbiddenField2;
 *     // Constructor, getters, and setters...
 * }
 * </pre>
 *
 * <p>Attributes:<br>
 * - field: Specifies the name of the field that triggers the validation. If this field is present,
 * the specified forbiddenFields must be null.<br>
 * - forbiddenFields: Defines the fields that are required to be null if the triggering field is
 * present.<br>
 * - message: Custom message to be used in case of validation failure.<br>
 * - groups: Specifies the validation groups with which the constraint declaration is associated.
 * <br>
 * - payload: Used by clients of the Bean Validation API to assign custom payload objects to a
 * constraint.<br>
 *
 * @see RequiresNullValidator The validator implementing the constraint logic, ensuring the
 *     specified conditions are met.
 */
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = RequiresNullValidator.class)
@Repeatable(value = RequiresNullList.class)
@Documented
public @interface RequiresNull {

  /**
   * Specifies the field that, if present, enforces the specified {@code forbiddenFields} to be
   * null.
   *
   * @return The name of the field triggering validation.
   */
  String field();

  /**
   * Defines an array of field names that must be null when the {@code field} is present.
   *
   * @return Array of field names to be validated as null.
   */
  String[] forbiddenFields();

  /**
   * Provides a default message to be used for validation failure.
   *
   * @return The validation error message template.
   */
  String message() default "{RequiresNull.message}";

  /**
   * Optionally specifies the validation groups with which the constraint declaration is associated.
   *
   * @return Validation groups.
   */
  Class<?>[] groups() default {};

  /**
   * Can be used by clients of the Bean Validation API to assign custom payload objects to a
   * constraint.
   *
   * @return Custom payload classes.
   */
  Class<? extends Payload>[] payload() default {};
}
