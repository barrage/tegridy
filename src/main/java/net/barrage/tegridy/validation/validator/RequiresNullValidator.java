package net.barrage.tegridy.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import lombok.SneakyThrows;
import net.barrage.tegridy.validation.annotation.RequiresNull;

public class RequiresNullValidator implements ConstraintValidator<RequiresNull, Object> {

  private String fieldName;
  private String[] forbiddenFields;
  private String message;

  @Override
  public void initialize(RequiresNull annotation) {
    this.fieldName = annotation.field();
    this.forbiddenFields = annotation.forbiddenFields();
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

    for (String dependFieldName : forbiddenFields) {
      Field dependField = object.getClass().getDeclaredField(dependFieldName);
      dependField.setAccessible(true);
      Object dependFieldValue = dependField.get(object);

      if (dependFieldValue == null) {
        continue;
      }

      context.disableDefaultConstraintViolation();
      String finalMessage = message;
      if (message.equals(RequiresNull.class.getMethod("message").getDefaultValue().toString())) {
        finalMessage =
            fieldName
                + " is present but the following fields are forbidden: "
                + String.join(", ", forbiddenFields);
      }
      context.buildConstraintViolationWithTemplate(finalMessage).addConstraintViolation();

      return false;
    }

    return true;
  }
}
