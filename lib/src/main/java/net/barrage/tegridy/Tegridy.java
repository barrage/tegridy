package net.barrage.tegridy;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import net.barrage.tegridy.modification.Modify;

/**
 * Maintain 'tegridy.
 */
public class Tegridy {

  /**
   * Validate the input based on its validation annotations.
   *
   * @param input Instance to validate.
   * @param <T>   The input type.
   * @throws ConstraintViolationException When an error occurs during validation.
   */
  public static <T> void validate(T input) throws ConstraintViolationException {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    var validator = factory.getValidator();

    var errors = validator.validate(input);

    if (errors.isEmpty()) {
      return;
    }

    throw new ConstraintViolationException(errors);
  }

  /**
   * Runs the input's {@link Modify} implementation then validates it based on its validation
   * annotations.
   *
   * @param input Instance to validify.
   * @param <T>   The input type.
   * @throws ConstraintViolationException When an error occurs during validation.
   */
  public static <T extends Modify> void validify(T input) throws ConstraintViolationException {
    input.modify();
    Tegridy.validate(input);
  }
}
