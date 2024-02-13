package net.barrage.tegridy.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import net.barrage.tegridy.util.StringUtils;
import net.barrage.tegridy.validation.annotation.EnumString;

public class EnumStringValidator implements ConstraintValidator<EnumString, CharSequence> {
  private List<String> acceptedValues;
  private String message;

  @Override
  public void initialize(EnumString annotation) {
    message = annotation.message();
    acceptedValues = Stream.of(annotation.value().getEnumConstants())
        .map(Enum::name)
        .map(String::toLowerCase)
        .map(StringUtils::toLowerCamelCase)
        .collect(Collectors.toList());
  }

  @SneakyThrows
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
      String finalMessage = message;
      if (message.equals(EnumString.class.getMethod("message").getDefaultValue().toString())) {
        finalMessage = "must be one of: " + String.join(", ", acceptedValues);
      }
      context.buildConstraintViolationWithTemplate(
              finalMessage)
          .addConstraintViolation();
    }

    return isValid;
  }
}