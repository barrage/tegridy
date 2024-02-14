import net.barrage.tegridy.validation.annotation.RequiresNull;

@RequiresNull(field = "mainField", forbiddenFields = { "missingRelatedField",
    "missingRelatedField2" })
public class MissingRequiredFieldsClass {
  private String mainField;
  private String missingRelatedField;
}
