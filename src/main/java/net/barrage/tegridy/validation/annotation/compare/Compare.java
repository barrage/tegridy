package net.barrage.tegridy.validation.annotation.compare;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import net.barrage.tegridy.validation.validators.CompareValidator;

/**
 * The {@code Compare} annotation is used to enforce a comparison constraint between two fields
 * of a Java Bean class. This annotation allows you to specify two fields and a comparator class
 * which will be used to perform the comparison logic.
 *
 * <p>This constraint is repeatable, meaning multiple {@code Compare} annotations can be applied to the same class.</p>
 *
 * <p>Example usage:
 * <pre>
 * &#64;Compare(
 *     baseField = "startDate",
 *     comparisonField = "endDate",
 *     comparator = DateComparator.class,
 *     message = "End date must be after start date"
 * )
 * public class Event {
 *     private LocalDate startDate;
 *     private LocalDate endDate;
 *     // ...
 * }
 * </pre>
 * </p>
 *
 * @see CompareValidator
 */
@Target({ TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = CompareValidator.class)
@Repeatable(CompareList.class)
@Documented
public @interface Compare {

  /**
   * The default message that will be used when the validation fails.
   *
   * @return the error message template.
   */
  String message() default "{tegridy.validation.annotation.Compare.message}";

  /**
   * The groups with which the constraint declaration is associated.
   *
   * @return the groups
   */
  Class<?>[] groups() default { };

  /**
   * Payload that can be used by clients of the Bean Validation API to assign
   * custom payload objects to a constraint.
   *
   * @return the payload classes
   */
  Class<? extends Payload>[] payload() default { };

  /**
   * The name of the base field that is involved in the comparison.
   *
   * @return the name of the base field
   */
  String baseField();

  /**
   * The name of the field that will be compared with the base field.
   *
   * @return the name of the comparison field
   */
  String comparisonField();

  /**
   * The name of the method that will be used to perform the comparison between fields.
   * This method should be defined in the class using this annotation.
   *
   * <p>The method specified by this attribute is invoked to compare the values of the
   * base field and the comparison field. The method should return a boolean value
   * indicating the result of the comparison.</p>
   *
   * @return the name of the comparison method
   */
  String comparisonMethod();

}
