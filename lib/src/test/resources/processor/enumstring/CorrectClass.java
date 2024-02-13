package validation.processors.enumstring.testClasses;

import net.barrage.tegridy.validation.annotation.EnumString;

public class CorrectClass {
  private String validField;
  @EnumString(Enum.class)
  private String relatedField1;
  private String relatedField2;

  enum Enum {
    TEST
  }

}
