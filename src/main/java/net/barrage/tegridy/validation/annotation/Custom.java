package net.barrage.tegridy.validation.annotation;

import static java.lang.annotation.ElementType.FIELD;
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
 * customized through the validation method on the field.
 *
 * <p>This annotation is intended for use at the field level.
 *
 * <p>Attributes:<br>
 * - {@code message}: Provides a default message for validation failures. This can be overridden
 * when the annotation is applied. <br>
 * - {@code groups}: Specifies the validation groups with which the constraint is associated,
 * allowing for selective validation. <br>
 * - {@code payload}: Can be used by clients of the Bean Validation API to assign custom payload
 * objects to the constraint. <br>
 * - {@code _validatorClass}: Class that will contain method for validation. <br>
 * - {@code method}: The name of the method that contains the custom validation logic. This method
 * should be implemented within the {@code _validatorClass} and is expected to return a boolean
 * indicating the validation result.
 *
 * <p>Example usage:
 *
 * <pre>
 * {@code public static class TestClass {
 *     @Custom(_validatorClass = TestClass.class, method = "higherThan5", message = "field1 must be greater than 5")
 *     @Custom(_validatorClass = TestClass.class, method = "higherThan4", message = "field1 must be greater than 4")
 *     public Integer field1;
 *     @Custom(_validatorClass = TestClass.class, method = "containsS", message = "field2 must contain 's'")
 *     public String field2;
 *     public String field3;
 *
 *     public TestClass(Integer field1, String field2, String field3) {
 *       this.field1 = field1;
 *       this.field2 = field2;
 *       this.field3 = field3;
 *     }
 *
 *     private static boolean higherThan5(Integer val) {
 *       return val > 5;
 *     }
 *
 *     private static boolean higherThan4(Integer val) {
 *       return val > 4;
 *     }
 *
 *     private static boolean containsS(String val) {
 *       return val.contains("s");
 *     }
 *   }
 * }
 * </pre>
 *
 * @see CustomValidator The validator implementing the constraint logic based on the defined custom
 *     method in validator class.
 */
@Target({FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = CustomValidator.class)
@Repeatable(value = CustomList.class)
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

  /** Custom class that contains method for validation. */
  Class<?> _validatorClass();

  /**
   * The name of the method that encapsulates the custom validation logic.
   *
   * @return The method name.
   */
  String method();
}
