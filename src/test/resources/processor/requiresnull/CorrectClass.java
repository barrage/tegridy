import net.barrage.tegridy.validation.annotation.RequiresNull;

@RequiresNull(
    field = "validField",
    forbiddenFields = {"relatedField1", "relatedField2"})
public class CorrectClass {
  private String validField;
  private String relatedField1;
  private String relatedField2;
}
