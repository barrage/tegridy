package validation.processor.custom.testClasses;

import net.barrage.tegridy.validation.annotation.Custom;

public class CorrectClass {
  @Custom(_validatorClass = CorrectClass.class, method = "validate")
  @Custom(_validatorClass = CorrectClass.class, method = "validate2")
  Integer base;

  public static boolean validate(Integer base) {
    return true;
  }

  public static boolean validate2(Integer base) {
    return true;
  }
}