package net.barrage.tegridy.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.barrage.tegridy.util.Strum;
import net.barrage.tegridy.validation.annotation.EnumString;

public class EnumStringValidator implements ConstraintValidator<EnumString, CharSequence> {
  private List<String> acceptedValues;
  private String message;

  @Override
  public void initialize(EnumString annotation) {
    message = annotation.message();
    Strum mapper;
    try {
      mapper = annotation.remap().getConstructor().newInstance();
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      // NoSuchMethod - each child of Strum has an empty constructor
      // IllegalAccess - each child of Strum has a public constructor
      // InvocationTarget - none of the children of Strum can throw during construction
      return;
    } catch (InstantiationException e) {
      // Here we will throw in case the abstract class Strum was passed as the remap argument
      throw new RuntimeException(e.getMessage(), e.getCause());
    }
    acceptedValues =
        Stream.of(annotation.value().getEnumConstants())
            .map(Enum::name)
            .map(mapper::remap)
            .collect(Collectors.toList());
  }

  @Override
  public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }

    boolean isValid = acceptedValues.stream().toList().contains(value.toString());

    if (!isValid) {
      context.disableDefaultConstraintViolation();
      String finalMessage = message;
      try {
        if (message.equals(EnumString.class.getMethod("message").getDefaultValue().toString())) {
          finalMessage = "must be one of: " + String.join(", ", acceptedValues);
        }
      } catch (NoSuchMethodException e) {
        // No chance of this happening since EnumString will always have the `message` method
        return false;
      }
      context.buildConstraintViolationWithTemplate(finalMessage).addConstraintViolation();
    }

    return isValid;
  }
}
