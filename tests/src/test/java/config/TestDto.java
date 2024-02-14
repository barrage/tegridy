package config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.barrage.tegridy.modification.Modifier;
import net.barrage.tegridy.modification.Modify;
import net.barrage.tegridy.modification.annotation.ModifyCustom;
import net.barrage.tegridy.modification.annotation.ModifyTrim;
import net.barrage.tegridy.modification.annotation.ModifyUpperCase;

@Data
@Valid
public class TestDto implements Modify {
  @ModifyTrim
  @ModifyUpperCase
  @Size(min = 2, max = 4)
  String foo;

  @ModifyCustom(ModifyMe.class)
  String custom;

  @NoArgsConstructor
  public static class ModifyMe implements Modifier<String> {
    @Override
    public String doModify(String input) {
      return "42069";
    }
  }

}
