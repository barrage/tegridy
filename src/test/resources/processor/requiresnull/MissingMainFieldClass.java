import net.barrage.tegridy.validation.annotation.RequiresNull;

@RequiresNull(
    field = "mainField",
    forbiddenFields = {"missingRelatedField", "missingRelatedField2"})
public class MissingMainFieldClass {
  private String missingRelatedField;
  private String missingRelatedField2;
}
