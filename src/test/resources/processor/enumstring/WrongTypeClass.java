package validation.processors.enumstring.testClasses;

import net.barrage.tegridy.validation.annotation.EnumString;

public class WrongTypeClass {
  private String validField;

  @EnumString(Enum.class)
  private Integer relatedField1;

  private String relatedField2;

  enum Enum {
    TEST
  }
}
