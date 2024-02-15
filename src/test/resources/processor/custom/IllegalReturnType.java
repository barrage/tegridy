package validation.processor.custom.testClasses;

import net.barrage.tegridy.validation.annotation.Custom;

@Custom(baseField = "base", argumentFields = { "argument1",
    "argument2" }, method = "valid")
public class IllegalReturnType {
  Integer base;
  Integer argument1;
  Integer argument2;

  public Integer valid(Integer base, Integer argument1, Integer argument2) {
    return 1;
  }
}
