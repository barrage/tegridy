import net.barrage.tegridy.modification.Modifier;
import net.barrage.tegridy.modification.Modify;
import net.barrage.tegridy.modification.annotation.ModifyCustom;
import net.barrage.tegridy.modification.annotation.ModifyNested;
import net.barrage.tegridy.modification.annotation.ModifyUpperCase;

@ModifyNested
public class NotFieldNested implements Modify {
  String custom;
}
