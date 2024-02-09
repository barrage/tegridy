package net.barrage.tegridy.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import net.barrage.tegridy.validation.annotation.requiresNotNull.RequiresNotNull;

public class RequiresNotNullValidator implements ConstraintValidator<RequiresNotNull, Object> {

  private String fieldName;
  private String[] dependFieldNames;

  @Override
  public void initialize(RequiresNotNull annotation) {
    this.fieldName = annotation.field();
    this.dependFieldNames = annotation.requiresFields();
  }

  @Override
  public boolean isValid(Object object, ConstraintValidatorContext context) {
    if (object == null) {
      return true;
    }

    try {
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

        String requiredFields = String.join(", ", dependFieldNames);

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                "field '" + fieldName + "' requires the following fields to be present: " +
                    requiredFields)
            .addConstraintViolation();

        return false;
      }
    } catch (NoSuchFieldException | IllegalAccessException e) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate("Error accessing fields for validation")
          .addConstraintViolation();
      return false;
    }

    return true;
  }
}
