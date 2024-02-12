package net.barrage.tegridy.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import lombok.SneakyThrows;
import net.barrage.tegridy.validation.annotation.requiresNotNull.RequiresNotNull;

public class RequiresNotNullValidator implements ConstraintValidator<RequiresNotNull, Object> {

  private String fieldName;
  private String[] dependFieldNames;
  private String message;

  @Override
  public void initialize(RequiresNotNull annotation) {
    this.fieldName = annotation.field();
    this.dependFieldNames = annotation.requiresFields();
    this.message = annotation.message();
  }

  @SneakyThrows
  @Override
  public boolean isValid(Object object, ConstraintValidatorContext context) {
    if (object == null) {
      return true;
    }
    Field field = object.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    Object fieldValue = field.get(object);
    if (fieldValue == null) {
      return true;
    }

    for (String dependFieldName : dependFieldNames) {
      Field dependField = object.getClass().getDeclaredField(dependFieldName);
      dependField.setAccessible(true);
      Object dependFieldValue = dependField.get(object);

      if (dependFieldValue != null) {
        continue;
      }

      context.disableDefaultConstraintViolation();
      String finalMessage = message;
      if (message.equals(RequiresNotNull.class.getMethod("message").getDefaultValue().toString())) {
        finalMessage = fieldName + " is present but the following fields are required: " +
            String.join(", ", dependFieldNames);
      }
      context.buildConstraintViolationWithTemplate(
              finalMessage)
          .addConstraintViolation();

      return false;
    }

    return true;
  }
}
