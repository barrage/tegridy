package validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import lombok.AllArgsConstructor;
import net.barrage.tegridy.util.Strum;
import net.barrage.tegridy.validation.annotation.EnumString;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class EnumStringTests {
  private static Validator validator;

  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void validEnumValuesOneWord() {
    TestClass testObj = new TestClass("test");
    assertTrue(validator.validate(testObj).isEmpty());

    testObj = new TestClass("TEST");
    assertFalse(validator.validate(testObj).isEmpty());

    testObj = new TestClass("Test");
    assertFalse(validator.validate(testObj).isEmpty());

    testObj = new TestClass("tEsT");
    assertFalse(validator.validate(testObj).isEmpty());
  }

  @Test
  void validEnumValuesTwoWords() {
    TestClass testObj = new TestClass("valueOne");
    assertTrue(validator.validate(testObj).isEmpty());

    testObj = new TestClass("valueone");
    assertFalse(validator.validate(testObj).isEmpty());

    testObj = new TestClass("VALUEONE");
    assertFalse(validator.validate(testObj).isEmpty());
  }

  @Test
  void validEnumValuesMultipleWords() {
    TestClass testObj = new TestClass("valueOneTwoThree");
    assertTrue(validator.validate(testObj).isEmpty());

    testObj = new TestClass("valueonetwothree");
    assertFalse(validator.validate(testObj).isEmpty());

    testObj = new TestClass("VALUEONETWOTHREE");
    assertFalse(validator.validate(testObj).isEmpty());
  }

  @Test
  void invalidEnumValuesMultipleWordsSnakeCased() {
    TestClass testObj = new TestClass("VALUE_ONE_TWO_THREE");
    assertFalse(validator.validate(testObj).isEmpty());

    testObj = new TestClass("value_one_two_three");
    assertFalse(validator.validate(testObj).isEmpty());

    testObj = new TestClass("vAlUE_oNE_TWo_three");
    assertFalse(validator.validate(testObj).isEmpty());
  }

  @Test
  void invalidEnumValue() {
    TestClass testObj = new TestClass("invalidValue");
    Set<ConstraintViolation<TestClass>> validationErrors = validator.validate(testObj);
    assertFalse(validationErrors.isEmpty());
    String expectedMessage = "must be one of: test, valueOne, valueOneTwoThree";
    assertTrue(
        validationErrors.stream()
            .map(ConstraintViolation::getMessage)
            .toList()
            .contains(expectedMessage),
        "Error message not expected");
  }

  @Test
  void validNullEnumValue() {
    TestClass testObj = new TestClass(null);
    assertTrue(validator.validate(testObj).isEmpty());
  }

  @Test
  void invalidCustomMessage() {
    TestClassCustomMessage testObj = new TestClassCustomMessage("invalid");
    Set<ConstraintViolation<TestClassCustomMessage>> validationErrors = validator.validate(testObj);
    assertFalse(validationErrors.isEmpty());
    String expectedMessage = "Custom";
    assertTrue(
        validationErrors.stream()
            .map(ConstraintViolation::getMessage)
            .toList()
            .contains(expectedMessage),
        "Error message not expected");
  }

  @Test
  void remapping() {
    var test =
        new RemapTest("oneTwoBMS", "one_two_b_m_s", "one-two-b-m-s", "ONE-TWO-B-M-S", "OneTwoBMS");
    Set<ConstraintViolation<RemapTest>> validationErrors = validator.validate(test);

    assertTrue(validationErrors.isEmpty());
  }

  @AllArgsConstructor
  private static class RemapTest {

    @EnumString(value = TestStrum.class, remap = Strum.CamelCase.class)
    private String camel;

    @EnumString(value = TestStrum.class, remap = Strum.SnakeCase.class)
    private String snake;

    @EnumString(value = TestStrum.class, remap = Strum.KebabCase.class)
    private String kebab;

    @EnumString(value = TestStrum.class, remap = Strum.ScreamingKebabCase.class)
    private String scream;

    @EnumString(value = TestStrum.class, remap = Strum.PascalCase.class)
    private String pascal;

    private static enum TestStrum {
      ONE_TWO_B_M_S
    }
  }

  private static class TestClass {
    @EnumString(value = TestEnum.class, remap = Strum.CamelCase.class)
    private String testField;

    public TestClass(String testField) {
      this.testField = testField;
    }

    private enum TestEnum {
      TEST,
      VALUE_ONE,
      VALUE_ONE_TWO_THREE
    }
  }

  private static class TestClassCustomMessage {
    @EnumString(value = TestClass.TestEnum.class, remap = Strum.CamelCase.class, message = "Custom")
    private String testField;

    public TestClassCustomMessage(String testField) {
      this.testField = testField;
    }

    private enum TestEnum {
      TEST,
      VALUE_ONE,
      VALUE_ONE_TWO_THREE
    }
  }
}
