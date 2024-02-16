package util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import net.barrage.tegridy.util.Strum;
import org.junit.jupiter.api.Test;

class StrumTests {

  @Test
  void noMapWorks() {
    var mapper = new Strum.NoMap();

    var input = "HELLO";
    var output = mapper.remap(input);
    assertEquals("HELLO", output);

    var input2 = "HELLO_WORLD";
    var output2 = mapper.remap(input2);
    assertEquals("HELLO_WORLD", output2);

    var input3 = "HELLO_WORLD_V_M";
    var output3 = mapper.remap(input3);
    assertEquals("HELLO_WORLD_V_M", output3);
  }

  @Test
  void camelCaseWorks() {
    var mapper = new Strum.CamelCase();

    var input = "HELLO";
    var output = mapper.remap(input);
    assertEquals("hello", output);

    var input2 = "HELLO_WORLD";
    var output2 = mapper.remap(input2);
    assertEquals("helloWorld", output2);

    var input3 = "HELLO_WORLD_V_M";
    var output3 = mapper.remap(input3);
    assertEquals("helloWorldVM", output3);
  }

  @Test
  void pascalCaseWorks() {
    var mapper = new Strum.PascalCase();

    var input = "HELLO";
    var output = mapper.remap(input);
    assertEquals("Hello", output);

    var input2 = "HELLO_WORLD";
    var output2 = mapper.remap(input2);
    assertEquals("HelloWorld", output2);

    var input3 = "HELLO_WORLD_V_M";
    var output3 = mapper.remap(input3);
    assertEquals("HelloWorldVM", output3);
  }

  @Test
  void snakeCaseWorks() {
    var mapper = new Strum.SnakeCase();

    var input = "HELLO";
    var output = mapper.remap(input);
    assertEquals("hello", output);

    var input2 = "HELLO_WORLD";
    var output2 = mapper.remap(input2);
    assertEquals("hello_world", output2);

    var input3 = "HELLO_WORLD_V_M";
    var output3 = mapper.remap(input3);
    assertEquals("hello_world_v_m", output3);
  }

  @Test
  void kebabCaseWorks() {
    var mapper = new Strum.KebabCase();

    var input = "HELLO";
    var output = mapper.remap(input);
    assertEquals("hello", output);

    var input2 = "HELLO_WORLD";
    var output2 = mapper.remap(input2);
    assertEquals("hello-world", output2);

    var input3 = "HELLO_WORLD_V_M";
    var output3 = mapper.remap(input3);
    assertEquals("hello-world-v-m", output3);
  }

  @Test
  void screamingKebabCaseWorks() {
    var mapper = new Strum.ScreamingKebabCase();

    var input = "HELLO";
    var output = mapper.remap(input);
    assertEquals("HELLO", output);

    var input2 = "HELLO_WORLD";
    var output2 = mapper.remap(input2);
    assertEquals("HELLO-WORLD", output2);

    var input3 = "HELLO_WORLD_V_M";
    var output3 = mapper.remap(input3);
    assertEquals("HELLO-WORLD-V-M", output3);
  }

  @Test
  void camelToVariant() throws Exception {
    var out = Strum.camelToVariant("superWickedOMG", TestNum.class);
    assertEquals(TestNum.SUPER_WICKED_O_M_G, out);
  }

  @Test
  void pascalToVariant() throws Exception {
    var out = Strum.pascalToVariant("SuperWickedOMG", TestNum.class);
    assertEquals(TestNum.SUPER_WICKED_O_M_G, out);
  }

  @Test
  void kebabToVariant() throws Exception {
    var out = Strum.kebabToVariant("super-wicked-o-m-g", TestNum.class);
    assertEquals(TestNum.SUPER_WICKED_O_M_G, out);
  }

  @Test
  void screamingKebabToVariant() throws Exception {
    var out = Strum.screamingKebabToVariant("SUPER-WICKED-O-M-G", TestNum.class);
    assertEquals(TestNum.SUPER_WICKED_O_M_G, out);
  }

  @Test
  void snakeToVariant() throws Exception {
    var out = Strum.snakeToVariant("super_wicked_o_m_g", TestNum.class);
    assertEquals(TestNum.SUPER_WICKED_O_M_G, out);
  }

  static enum TestNum {
    SUPER_WICKED_O_M_G
  }

  @Test
  void testToLowerCamelCase_SingleWord() {
    String result = new Strum.CamelCase().remap("word");
    assertEquals("word", result);
  }

  @Test
  void testToLowerCamelCase_MultipleWords() {
    String result = new Strum.CamelCase().remap("hello_world_one");
    assertEquals("helloWorldOne", result);
  }

  @Test
  void testToLowerCamelCaseCamelCase() {
    var result = new Strum.CamelCase().remap("HELLO_WORLD");
    assertEquals("helloWorld", result);
  }

  @Test
  void testToLowerCamelCase_EmptyString() {
    String result = new Strum.CamelCase().remap("");
    assertEquals("", result);
  }

  @Test
  void testToLowerCamelCase_NullInput() {
    // Depending on how you want to handle null input, this test can be adjusted.
    // Here, it's assumed that a null input should result in a NullPointerException.
    assertThrows(
        NullPointerException.class,
        () -> {
          new Strum.CamelCase().remap(null);
        });
  }

  @Test
  void testToLowerCamelCase_WithNumbers() {
    String result = new Strum.CamelCase().remap("version_2_update_3_test");
    assertEquals("version2Update3Test", result);
  }

  @Test
  void testToLowerCamelCase_WithSpecialCharacters() {
    String result = new Strum.CamelCase().remap("hello_world$");
    assertEquals("helloWorld$", result);
  }
}
