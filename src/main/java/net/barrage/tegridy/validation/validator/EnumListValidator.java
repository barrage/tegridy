package net.barrage.tegridy.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.barrage.tegridy.util.StringUtils;
import net.barrage.tegridy.validation.annotation.EnumList;

public class EnumListValidator implements ConstraintValidator<EnumList, String[]> {
  private List<String> acceptedValues;

  @Override
  public void initialize(EnumList annotation) {
    acceptedValues = Stream.of(annotation.value().getEnumConstants())
        .map(Enum::name)
        .map(String::toLowerCase)
        .map(StringUtils::toLowerCamelCase)
        .collect(Collectors.toList());
  }

  @Override
  public boolean isValid(String[] value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }

    var isValid = true;

    for (var val : value) {
      if (!acceptedValues.contains(StringUtils.toLowerCamelCase(val))) {
        isValid = false;
        break;
      }
    }

    if (!isValid) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(
              "must be one of the following: " + String.join(", ", acceptedValues))
          .addConstraintViolation();
    }

    return isValid;
  }
}