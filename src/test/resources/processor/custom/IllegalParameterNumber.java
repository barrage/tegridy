package validation.processor.compare.testClasses;

import net.barrage.tegridy.validation.annotation.Custom;

@Custom(baseField = "base", argumentFields = { "argument1",
    "argument2" }, method = "valid")
public class IllegalParameterNumber {
  Integer base;
  Integer argument1;
  String argument2;

  public Boolean valid(Integer base, Integer argument1) {
    return true;
  }
}
