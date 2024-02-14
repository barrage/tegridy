import net.barrage.tegridy.validation.annotation.RequiresNotNull;

@RequiresNotNull(field = "mainField", requiresFields = {"missingRelatedField", "missingRelatedField2"})
public class MissingMainFieldClass {
  private String missingRelatedField;
  private String missingRelatedField2;
}
