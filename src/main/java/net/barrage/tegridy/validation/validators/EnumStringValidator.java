package net.barrage.tegridy.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.barrage.tegridy.utils.StringUtils;
import net.barrage.tegridy.validation.annotation.EnumString;

public class EnumStringValidator implements ConstraintValidator<EnumString, CharSequence> {
  private List<String> acceptedValues;

  @Override
  public void initialize(EnumString annotation) {
    acceptedValues = Stream.of(annotation.value().getEnumConstants())
        .map(Enum::name)
        .map(String::toLowerCase)
        .map(StringUtils::toLowerCamelCase)
        .collect(Collectors.toList());
  }

  @Override
  public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }

    boolean isValid = acceptedValues.stream()
        .toList()
        .contains(StringUtils.toLowerCamelCase(value.toString()));

    if (!isValid) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(
          "must be one of: " + String.join(", ", acceptedValues)).addConstraintViolation();
    }

    return isValid;
  }
}