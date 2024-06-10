package net.barrage.tegridy.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Method;
import lombok.SneakyThrows;
import net.barrage.tegridy.validation.annotation.Custom;

public class CustomValidator implements ConstraintValidator<Custom, Object> {
  private String message;
  private Class<?> validatorClass;
  private String method;

  @Override
  @SneakyThrows
  public void initialize(Custom annotation) {
    message = annotation.message();
    validatorClass = annotation._validatorClass();
    method = annotation.method();
  }

  @SneakyThrows
  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }

    Method method = this.validatorClass.getDeclaredMethod(this.method, value.getClass());
    method.setAccessible(true);

    boolean valid = (boolean) method.invoke(value, value);

    if (!valid) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
    return valid;
  }
}
