package validation.processor.custom.testClasses;

import net.barrage.tegridy.validation.annotation.Custom;

public class MethodWrongParam {
  @Custom(_validatorClass = MethodWrongParam.class, method = "validate")
  @Custom(_validatorClass = MethodWrongParam.class, method = "validate2")
  Integer base;

  public static boolean validate(String base) {
    return true;
  }

  public static boolean validate2(Integer base) {
    return true;
  }
}