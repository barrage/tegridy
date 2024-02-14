import net.barrage.tegridy.validation.annotation.RequiresNotNull;

@RequiresNotNull(field = "validField", requiresFields = { "relatedField1", "relatedField2" })
public class CorrectClass {
  private String validField;
  private String relatedField1;
  private String relatedField2;
}
