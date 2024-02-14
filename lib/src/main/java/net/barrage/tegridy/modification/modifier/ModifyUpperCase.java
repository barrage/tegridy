package net.barrage.tegridy.modification.modifier;

import net.barrage.tegridy.modification.Modifier;

public class ModifyUpperCase implements Modifier<String> {
  @Override
  public String doModify(String input) {
    return input.toUpperCase();
  }
}
