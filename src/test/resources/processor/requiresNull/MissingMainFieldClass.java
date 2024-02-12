package validation.processors.requiresNull.testClasses;

import net.barrage.tegridy.validation.annotation.requiresNull.RequiresNull;

@RequiresNull(field = "mainField", forbiddenFields = { "missingRelatedField",
    "missingRelatedField2" })
public class MissingMainFieldClass {
  private String missingRelatedField;
  private String missingRelatedField2;
}