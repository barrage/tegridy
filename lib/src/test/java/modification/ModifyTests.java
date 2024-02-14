package modification;

import static org.junit.jupiter.api.Assertions.assertEquals;

import lombok.AllArgsConstructor;
import net.barrage.tegridy.modification.Modifier;
import net.barrage.tegridy.modification.Modify;
import net.barrage.tegridy.modification.annotation.ModifyCapitalize;
import net.barrage.tegridy.modification.annotation.ModifyCustom;
import net.barrage.tegridy.modification.annotation.ModifyLowerCase;
import net.barrage.tegridy.modification.annotation.ModifyNested;
import net.barrage.tegridy.modification.annotation.ModifyTrim;
import net.barrage.tegridy.modification.annotation.ModifyUpperCase;
import org.junit.jupiter.api.Test;

public class ModifyTests {

  @Test
  void modifiersWork() {
    var test = new StringModifierTest(
        "   trim  \n ",
        "capitalize me.",
        "MAKE ME WHISPER",
        "make me shout");

    test.modify();

    assertEquals("trim", test.foo);
    assertEquals("Capitalize me.", test.bar);
    assertEquals("make me whisper", test.baz);
    assertEquals("MAKE ME SHOUT", test.qux);
  }

  @Test
  void customModifierWorks() {
    var test = new CustomModifierTest("baFOOnaFOOna");
    test.modify();
    assertEquals("banana", test.foo);
  }

  @Test
  void nestedModifierWorks() {
    var test = new NestedModifierTest(new NestedModifierTest.Child("    trim    "));
    test.modify();
    assertEquals("trim", test.child.foo);
  }

  @AllArgsConstructor
  static class StringModifierTest implements Modify {
    @ModifyTrim
    public String foo;
    @ModifyCapitalize
    public String bar;
    @ModifyLowerCase
    public String baz;
    @ModifyUpperCase
    public String qux;
  }

  @AllArgsConstructor
  static class CustomModifierTest implements Modify {
    @ModifyCustom(RemoveFOO.class)
    public String foo;

    public static class RemoveFOO implements Modifier<String> {

      @Override
      public String doModify(String input) {
        return input.replaceAll("FOO", "");
      }
    }
  }

  @AllArgsConstructor
  static class NestedModifierTest implements Modify {
    @ModifyNested
    Child child;

    @AllArgsConstructor
    public static class Child implements Modify {
      @ModifyTrim
      String foo;
    }
  }
}
