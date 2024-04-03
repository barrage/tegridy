package net.barrage.tegridy.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import net.barrage.tegridy.validation.annotation.Ip4;

/** Validates whether the given string field is a valid IPv4 address. */
public class Ip4Validator implements ConstraintValidator<Ip4, Object> {
  static final String IP4_REGEX = "^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$";
  private String message;

  @Override
  public void initialize(Ip4 annotation) {
    this.message = annotation.message();
  }

  @Override
  public boolean isValid(Object o, ConstraintValidatorContext context) {
    if (o == null) {
      return true;
    }

    String ip;
    try {
      ip = (String) o;
    } catch (ClassCastException e) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
      context.disableDefaultConstraintViolation();
      return false;
    }

    if (!ip.matches(IP4_REGEX)) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
      context.disableDefaultConstraintViolation();
      return false;
    }
    return true;
  }
}
