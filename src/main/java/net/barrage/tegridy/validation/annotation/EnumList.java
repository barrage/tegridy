package net.barrage.tegridy.validation.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import net.barrage.tegridy.util.Strum;
import net.barrage.tegridy.validation.validator.EnumListValidator;

/**
 * Basically the same as {@link EnumString}, but for a list of strings.
 *
 * <p>Usage example on a field that takes an array of Strings representing enum values:
 *
 * <pre>
 * {@code @EnumList(value = MyEnum.class})
 * private String[] myEnumValues;
 * </pre>
 *
 * <p>Attributes: <br>
 * - {@code value}: The enumeration class against which the String values will be validated. <br>
 * - {@code message}: The default message that will be displayed when the validation fails. It can
 * incorporate the {@code {value}} placeholder to include the list of valid enum names in the
 * validation message. <br>
 * - {@code groups}: Used to specify validation groups for selective validation. <br>
 * - {@code payload}: Can be used by clients of the Bean Validation API to assign custom payload
 * objects to a constraint.
 *
 * @see EnumListValidator The validator implementing the constraint logic, checking if provided
 *     String values match enum constants in the specified enumeration.
 */
@Target({FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = EnumListValidator.class)
public @interface EnumList {
  /**
   * Specifies the enumeration class that contains the constants against which the String values
   * will be validated.
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
   * The remap strategy to use for the enum variants. Do note that all variants must be in
   * SCREAM_CASE.
   *
   * @return The re-mapper.
   */
  Class<? extends Strum> remap() default Strum.NoMap.class;

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
