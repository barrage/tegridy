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
import net.barrage.tegridy.validation.validator.RequiresNotNullValidator;

/**
 * Defines a validation constraint that enforces certain fields to be non-null if a specific field is present and not null.
 * This annotation is intended for use on classes to establish conditional validation rules between fields, ensuring that
 * if a certain condition is met (specific field is not null), then other specified fields must also be not null.
 *
 * <p>This annotation is repeatable, allowing multiple conditional non-null validations to be applied to a class.</p>
 *
 * Usage example:
 * <pre>
 * &#64;RequiresNotNull(field = "conditionalField", requiresFields = {"requiredField1", "requiredField2"},
 * message = "requiredField1 and requiredField2 must not be null if conditionalField is present")
 * public class MyClass {
 *     private String conditionalField;
 *     private String requiredField1;
 *     private String requiredField2;
 *     // Constructor, getters, and setters...
 * }
 * </pre>
 *
 * Attributes:<br>
 * - field: Specifies the name of the field that triggers the validation.<br>
 * - requiresFields: Defines the fields that are required to be not null if the triggering field is present and not null.<br>
 * - message: Custom message to be used in case of validation failure.<br>
 * - groups: Specifies the validation groups with which the constraint declaration is associated.<br>
 * - payload: Used by clients of the Bean Validation API to assign custom payload objects to a constraint.<br>
 *
 * @see RequiresNotNullValidator The validator implementing the constraint logic.
 */
@Target({ TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = RequiresNotNullValidator.class)
@Repeatable(RequiresNotNullList.class)
@Documented
public @interface RequiresNotNull {

  /**
   * Specifies the field that, if not null, triggers validation of the specified {@code requiresFields}.
   *
   * @return The name of the field triggering validation.
   */
  String field();

  /**
   * Defines an array of field names that must not be null when the {@code field} is not null.
   *
   * @return Array of field names to be validated.
   */
  String[] requiresFields();

  /**
   * Provides a default message to be used for validation failure.
   *
   * @return The validation error message template.
   */
  String message() default "{NotNullIfAnother.message}";

  /**
   * Optionally specifies the validation groups with which the constraint declaration is associated.
   *
   * @return Validation groups.
   */
  Class<?>[] groups() default { };

  /**
   * Can be used by clients of the Bean Validation API to assign custom payload objects to a constraint.
   *
   * @return Custom payload classes.
   */
  Class<? extends Payload>[] payload() default { };

}
