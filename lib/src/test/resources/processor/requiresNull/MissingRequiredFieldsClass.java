package validation.processors.requiresNull.testClasses;

import net.barrage.tegridy.validation.annotation.requiresNull.RequiresNull;

@RequiresNull(field = "mainField", forbiddenFields = { "missingRelatedField",
    "missingRelatedField2" })
public class MissingRequiredFieldsClass {
  private String mainField;
  private String missingRelatedField;
}
