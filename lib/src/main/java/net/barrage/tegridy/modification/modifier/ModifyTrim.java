package net.barrage.tegridy.modification.modifier;

import net.barrage.tegridy.modification.Modifier;

public class ModifyTrim implements Modifier<String> {
  @Override
  public String doModify(String input) {
    return input.trim();
  }
}