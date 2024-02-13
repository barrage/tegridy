package net.barrage.tegridy.validation.annotation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import net.barrage.tegridy.validation.validator.EnumListValidator;

/**
 * The {@code EnumList} annotation is a constraint that validates if the values provided in a String array
 * are valid names of the constants defined in a specified enumeration ({@code value}). This can be used
 * on fields, method parameters, and other elements where an array of Strings representing enumeration values is expected.
 * <p>
 * Usage example on a field that takes an array of Strings representing enum values:
 * <pre>
 * {@code @EnumList(value = MyEnum.class, message = "must be one of {value}"}
 * private String[] myEnumValues;
 * </pre>
 * <p>
 * Attributes:
 * - {@code value}: The enumeration class against which the String values will be validated.
 * - {@code message}: The default message that will be displayed when the validation fails.
 * It can incorporate the {@code {value}} placeholder to include the list of valid enum names
 * in the validation message.
 * - {@code groups}: Used to specify validation groups for selective validation.
 * - {@code payload}: Can be used by clients of the Bean Validation API to assign custom payload objects to a constraint.
 *
 * @see EnumListValidator The validator implementing the constraint logic, checking if provided String values
 * match enum constants in the specified enumeration.
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = EnumListValidator.class)
public @interface EnumList {
  /**
   * Specifies the enumeration class that contains the constants against which
   * the String values will be validated.
   *
   * @return The enum class containing valid constants.
   */
  Class<? extends Enum<?>> value();

  /**
   * The message to display when a validation error occurs.
   * Can include {@code {value}} placeholder to list valid enum names.
   *
   * @return The validation error message template.
   */
  String message() default "must be one of {value}";

  /**
   * Specifies the validation groups with which this constraint is associated.
   *
   * @return The validation groups.
   */
  Class<?>[] groups() default { };

  /**
   * Can be used by clients of the Bean Validation API to assign custom payload objects to a constraint.
   *
   * @return The custom payload classes.
   */
  Class<? extends Payload>[] payload() default { };
}
