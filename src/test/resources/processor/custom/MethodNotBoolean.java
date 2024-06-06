package validation.processor.custom.testClasses;

import net.barrage.tegridy.validation.annotation.Custom;

public class MethodNotBoolean {
  @Custom(_validatorClass = MethodNotBoolean.class, method = "validate")
  @Custom(_validatorClass = MethodNotBoolean.class, method = "validate2")
  Integer base;

  public static boolean validate(Integer base) {
    return true;
  }

  public static Integer validate2(Integer base) {
    return 1;
  }
}