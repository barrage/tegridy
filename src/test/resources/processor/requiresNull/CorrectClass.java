package validation.processors.requiresNull.testClasses;

import net.barrage.tegridy.validation.annotation.requiresNull.RequiresNull;

@RequiresNull(field = "validField", forbiddenFields = { "relatedField1", "relatedField2" })
public class CorrectClass {
  private String validField;
  private String relatedField1;
  private String relatedField2;
}
