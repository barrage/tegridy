package util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import net.barrage.tegridy.util.StringUtils;
import org.junit.jupiter.api.Test;

public class StringUtilsTests {
  @Test
  public void testToLowerCamelCase_SingleWord() {
    String result = StringUtils.toLowerCamelCase("word");
    assertEquals("word", result);
  }

  @Test
  public void testToLowerCamelCase_MultipleWords() {
    String result = StringUtils.toLowerCamelCase("hello_world_one");
    assertEquals("helloWorldOne", result);
  }

  @Test
  public void testToLowerCamelCaseCamelCase() {
    String result = StringUtils.toLowerCamelCase("HELLO_WORLD");
    assertEquals("helloWorld", result);
  }

  @Test
  public void testToLowerCamelCase_EmptyString() {
    String result = StringUtils.toLowerCamelCase("");
    assertEquals("", result);
  }

  @Test
  public void testToLowerCamelCase_NullInput() {
    // Depending on how you want to handle null input, this test can be adjusted.
    // Here, it's assumed that a null input should result in a NullPointerException.
    assertThrows(
        NullPointerException.class,
        () -> {
          StringUtils.toLowerCamelCase(null);
        });
  }

  @Test
  public void testToLowerCamelCase_WithNumbers() {
    String result = StringUtils.toLowerCamelCase("version_2_update_3_test");
    assertEquals("version2Update3Test", result);
  }

  @Test
  public void testToLowerCamelCase_WithSpecialCharacters() {
    String result = StringUtils.toLowerCamelCase("hello_world$");
    assertEquals("helloWorld$", result);
  }
}
