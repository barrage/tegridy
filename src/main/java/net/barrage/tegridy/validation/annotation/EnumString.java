package net.barrage.tegridy.validation.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import net.barrage.tegridy.validation.validator.EnumStringValidator;

/**
 * The {@code EnumString} annotation is a constraint that validates if the value provided as a
 * String or CharSequence matches any of the constant names defined in a specified enumeration
 * ({@code value}). This constraint can be applied to single string fields or method parameters to
 * ensure that their value corresponds to one of the enum constants in the specified enum type.
 *
 * <p>This annotation is useful for validating string inputs (e.g., form or API inputs) against a
 * predefined set of accepted values represented by an enum.
 *
 * <p>Usage example on a field that takes a String representing an enum value:
 *
 * <pre>
 * {@code @EnumString(value = MyEnum.class, message = "must be one of {value}"}
 * private String myEnumValue;
 * </pre>
 *
 * <p>Attributes: - {@code value}: The enumeration class against which the String value will be
 * validated. - {@code message}: The default message that will be displayed when the validation
 * fails. The message can include the {@code {value}} placeholder, which will be replaced with the
 * list of valid enum names. - {@code groups}: Used to specify validation groups for selective
 * validation. - {@code payload}: Can be used by clients of the Bean Validation API to assign custom
 * payload objects to a constraint.
 *
 * @see EnumStringValidator The validator implementing the constraint logic, checking if the
 *     provided String or CharSequence matches one of the constant names in the specified
 *     enumeration.
 */
@Target({FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = EnumStringValidator.class)
public @interface EnumString {
  /**
   * Specifies the enumeration class that contains the constants against which the String or
   * CharSequence value will be validated.
   *
   * @return The enum class containing valid constants.
   */
  Class<? extends Enum<?>> value();

  /**
   * The message to display when a validation error occurs. Can include {@code {value}} placeholder
   * to list valid enum names.
   *
   * @return The validation error message template.
   */
  String message() default "must be one of {value}";

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
}
