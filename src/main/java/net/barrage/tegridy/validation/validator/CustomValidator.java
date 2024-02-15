package net.barrage.tegridy.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import lombok.SneakyThrows;
import net.barrage.tegridy.validation.annotation.Custom;

public class CustomValidator implements ConstraintValidator<Custom, Object> {

  private String baseField;
  private String[] argumentFields;
  private String _validationMethod;
  private String message;

  @Override
  public void initialize(Custom constraintAnnotation) {
    this.baseField = constraintAnnotation.baseField();
    this.argumentFields = constraintAnnotation.argumentFields();
    this.message = constraintAnnotation.message();
    this._validationMethod = constraintAnnotation.method();
  }

  @SneakyThrows
  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }

    Field baseField = value.getClass().getDeclaredField(this.baseField);
    baseField.setAccessible(true);
    Object baseFieldValue = baseField.get(value);

    if (baseFieldValue == null) {
      return true;
    }

    List<Object> arguments = new ArrayList<>();
    arguments.add(baseField.get(value));
    for (String argument : argumentFields) {
      Field field = value.getClass().getDeclaredField(argument);
      field.setAccessible(true);
      arguments.add(field.get(value));
    }

    Class<?>[] parameterTypes = new Class<?>[arguments.size()];
    for (int i = 0; i < arguments.size(); i++) {
      Object arg = arguments.get(i);
      Class<?> type = arg.getClass();
      parameterTypes[i] = type;
    }

    Method method = value.getClass().getDeclaredMethod(_validationMethod, parameterTypes);
    method.setAccessible(true);

    boolean valid = (boolean) method.invoke(value, arguments.toArray());

    if (!valid) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
    return valid;
  }
}
