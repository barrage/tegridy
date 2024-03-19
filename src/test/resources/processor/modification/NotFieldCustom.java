import net.barrage.tegridy.modification.Modifier;
import net.barrage.tegridy.modification.Modify;
import net.barrage.tegridy.modification.annotation.ModifyCustom;

@ModifyCustom(ModifyMe.class)
public class NotFieldCustom implements Modify {
  @ModifyCustom(ModifyMe.class)
  String custom;

  public static class ModifyMe implements Modifier<NotField> {
    @Override
    public NotField doModify(NotField input) {
      return input;
    }
  }
}
