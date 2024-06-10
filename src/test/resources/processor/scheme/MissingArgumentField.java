package validation.processor.scheme.testClasses;

import net.barrage.tegridy.validation.annotation.Scheme;

@Scheme(baseField = "base", argumentFields = { "argument1",
    "argument2" }, method = "valid")
public class MissingArgumentField {
  Integer base;
  Integer argument1;

  public Boolean valid(Integer base, Integer argument1, Integer argument2) {
    return true;
  }
}
