import net.barrage.tegridy.modification.Modifier;
import net.barrage.tegridy.modification.Modify;
import net.barrage.tegridy.modification.annotation.ModifyCustom;

public class MismatchedTypes implements Modify {
  @ModifyCustom(ModifyMe.class)
  String custom;

  public static class ModifyMe implements Modifier<Integer> {
    @Override
    public Integer doModify(Integer input) {
      return 42069;
    }
  }
}
