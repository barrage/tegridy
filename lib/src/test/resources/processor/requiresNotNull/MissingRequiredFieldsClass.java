package validation.processors.requiresNotNull.testClasses;

import net.barrage.tegridy.validation.annotation.requiresNotNull.RequiresNotNull;

@RequiresNotNull(field = "mainField", requiresFields = {"missingRelatedField", "missingRelatedField2"})
public class MissingRequiredFieldsClass {
  private String mainField;
  private String missingRelatedField;
}
