package modification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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
    var test =
        new StringModifierTest(
            "   trim  \n ", "capitalize me.", "MAKE ME WHISPER", "make me shout");

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
  void customModifierThrows() {
    var test = new CustomModifierThrowTest("baFOOnaFOOna");
    assertThrows(RuntimeException.class, test::modify);
  }

  @Test
  void nestedModifierWorks() {
    var test = new NestedModifierTest(new NestedModifierTest.Child("    trim    "));
    test.modify();
    assertEquals("trim", test.child.foo);
  }

  @Test
  void nestedModifierThrows() {
    var test = new NestedModifierThrowTest(new NestedModifierThrowTest.Child("    trim    "));
    assertThrows(RuntimeException.class, test::modify);
  }

  @AllArgsConstructor
  static class StringModifierTest implements Modify {
    @ModifyTrim public String foo;
    @ModifyCapitalize public String bar;
    @ModifyLowerCase public String baz;
    @ModifyUpperCase public String qux;
  }

  @AllArgsConstructor
  static class CustomModifierTest implements Modify {
    @ModifyCustom(RemoveFoo.class)
    String foo;

    protected static class RemoveFoo implements Modifier<String> {
      public RemoveFoo() {}

      @Override
      public String doModify(String input) {
        return input.replaceAll("FOO", "");
      }
    }
  }

  @AllArgsConstructor
  static class CustomModifierThrowTest implements Modify {
    @ModifyCustom(ThrowFoo.class)
    String foo;

    @NoArgsConstructor
    static class ThrowFoo implements Modifier<String> {

      @Override
      public String doModify(String input) {
        throw new RuntimeException("DONE GOOFD");
      }
    }
  }

  @AllArgsConstructor
  static class NestedModifierTest implements Modify {
    @ModifyNested Child child;

    @AllArgsConstructor
    static class Child implements Modify {
      @ModifyTrim String foo;
    }
  }

  @AllArgsConstructor
  static class NestedModifierThrowTest implements Modify {
    @ModifyNested Child child;

    @AllArgsConstructor
    static class Child implements Modify {
      @ModifyCustom(ChildModifier.class)
      String foo;
    }

    @NoArgsConstructor
    static class ChildModifier implements Modifier<Child> {
      @Override
      public Child doModify(Child input) {
        throw new RuntimeException("DONE GOOFD");
      }
    }
  }
}
