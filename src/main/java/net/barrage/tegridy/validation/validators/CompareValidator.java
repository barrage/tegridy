package net.barrage.tegridy.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import lombok.SneakyThrows;
import net.barrage.tegridy.validation.annotation.compare.Compare;

public class CompareValidator implements ConstraintValidator<Compare, Object> {

  private String baseField;
  private String comparisonField;
  private String comparisonMethod;
  private String message;

  @Override
  public void initialize(Compare constraintAnnotation) {
    this.baseField = constraintAnnotation.baseField();
    this.comparisonField = constraintAnnotation.comparisonField();
    this.message = constraintAnnotation.message();
    this.comparisonMethod = constraintAnnotation.comparisonMethod();
  }

  @SneakyThrows
  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }

    Field field1 = null;
    field1 = value.getClass().getDeclaredField(baseField);

    field1.setAccessible(true);
    Object field1Value = field1.get(value);

    Field field2 = value.getClass().getDeclaredField(comparisonField);
    field2.setAccessible(true);
    Object field2Value = field2.get(value);

    Method method = value.getClass()
        .getDeclaredMethod(comparisonMethod, field1.getType(), field2.getType());
    method.setAccessible(true);

    boolean valid = (boolean) method.invoke(value, field1Value, field2Value);

    if (!valid) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(message)
          .addConstraintViolation();
    }
    return valid;
  }
}
