package validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import java.util.stream.Collectors;
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
    assertTrue(validator.validate(testObj).isEmpty());

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
  void validEnumValuesMultipleWordsSnakeCased() {
    TestClass testObj = new TestClass("VALUE_ONE_TWO_THREE");
    assertTrue(validator.validate(testObj).isEmpty());

    testObj = new TestClass("value_one_two_three");
    assertTrue(validator.validate(testObj).isEmpty());

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
            .collect(Collectors.toList())
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
            .collect(Collectors.toList())
            .contains(expectedMessage),
        "Error message not expected");
  }

  private static class TestClass {
    @EnumString(TestEnum.class)
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
    @EnumString(value = TestClass.TestEnum.class, message = "Custom")
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
