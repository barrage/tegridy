import net.barrage.tegridy.modification.Modifier;
import net.barrage.tegridy.modification.Modify;
import net.barrage.tegridy.modification.annotation.ModifyNested;

public class NestedNoModify implements Modify {

  @ModifyNested
  Child child;

  public static class Child {
    String bar;
  }

  public static class ModifyMe implements Modifier<String> {
    @Override
    public String doModify(String input) {
      return "I HAVE BEEN MODDED";
    }
  }
}