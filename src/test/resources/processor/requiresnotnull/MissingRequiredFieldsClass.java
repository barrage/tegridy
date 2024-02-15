import net.barrage.tegridy.validation.annotation.RequiresNotNull;

@RequiresNotNull(
    field = "mainField",
    requiresFields = {"missingRelatedField", "missingRelatedField2"})
public class MissingRequiredFieldsClass {
  private String mainField;
  private String missingRelatedField;
}
