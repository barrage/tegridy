package net.barrage.tegridy.modification.modifier;

import net.barrage.tegridy.modification.Modifier;

public class ModifyCapitalize implements Modifier<String> {
  @Override
  public String doModify(String input) {
    return input.substring(0, 1).toUpperCase() + input.substring(1);
  }
}
