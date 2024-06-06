package validation.processor.custom.testClasses;

import net.barrage.tegridy.validation.annotation.Custom;

public class MissingMethod {
  @Custom(_validatorClass = MissingMethod.class, method = "validate")
  @Custom(_validatorClass = MissingMethod.class, method = "validate2")
  Integer base;

  public static boolean validate2(Integer base) {
    return true;
  }
}