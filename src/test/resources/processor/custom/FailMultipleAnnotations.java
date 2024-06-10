package validation.processor.custom.testClasses;

import net.barrage.tegridy.validation.annotation.Custom;

public class FailMultipleAnnotations {
  @Custom(_validatorClass = FailMultipleAnnotations.class, method = "validate")
  @Custom(_validatorClass = FailMultipleAnnotations.class, method = "validate1")
  Integer base;

  public static Integer validate(Integer base) {
    return true;
  }

  public static boolean validate2(Integer base) {
    return true;
  }
}