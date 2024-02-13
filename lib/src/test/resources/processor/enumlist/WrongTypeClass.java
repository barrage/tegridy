package validation.processors.enumstring.testClasses;

import net.barrage.tegridy.validation.annotation.EnumList;

public class WrongTypeClass {
  private String validField;

  @EnumList(Enum.class)
  private String relatedField1;

  private String relatedField2;

  enum Enum {
    TEST
  }

}
