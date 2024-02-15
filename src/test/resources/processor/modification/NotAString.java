import net.barrage.tegridy.modification.Modify;
import net.barrage.tegridy.modification.annotation.ModifyCapitalize;
import net.barrage.tegridy.modification.annotation.ModifyLowerCase;
import net.barrage.tegridy.modification.annotation.ModifyTrim;
import net.barrage.tegridy.modification.annotation.ModifyUpperCase;

public class NotAString implements Modify {
  @ModifyTrim public Integer foo;
  @ModifyCapitalize public Integer bar;
  @ModifyLowerCase public Integer baz;
  @ModifyUpperCase public Integer qux;
}
