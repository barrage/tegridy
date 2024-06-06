package validation.processor.custom.testClasses;

import net.barrage.tegridy.validation.annotation.Custom;

public class MethodMultipleParams {
  @Custom(_validatorClass = MethodMultipleParams.class, method = "validate")
  @Custom(_validatorClass = MethodMultipleParams.class, method = "validate2")
  Integer base;

  public static boolean validate(Integer base, Integer test) {
    return true;
  }

  public static boolean validate2(Integer base) {
    return true;
  }
}