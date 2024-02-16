package net.barrage.tegridy.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import net.barrage.tegridy.validation.annotation.EnumList;

public class EnumListValidator implements ConstraintValidator<EnumList, String[]> {
  private List<String> acceptedValues;
  private String message;

  @Override
  @SneakyThrows
  public void initialize(EnumList annotation) {
    message = annotation.message();
    var mapper = annotation.remap().getConstructor().newInstance();
    acceptedValues =
        Stream.of(annotation.value().getEnumConstants())
            .map(Enum::name)
            .map(mapper::remap)
            .collect(Collectors.toList());
  }

  @SneakyThrows
  @Override
  public boolean isValid(String[] value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }

    var isValid = true;

    for (var val : value) {
      if (val == null || !acceptedValues.contains(val)) {
        isValid = false;
        break;
      }
    }

    if (!isValid) {
      context.disableDefaultConstraintViolation();
      String finalMessage = message;
      if (message.equals(EnumList.class.getMethod("message").getDefaultValue().toString())) {
        finalMessage = "must be one of: " + String.join(", ", acceptedValues);
      }
      context.buildConstraintViolationWithTemplate(finalMessage).addConstraintViolation();
    }

    return isValid;
  }
}
