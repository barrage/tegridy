package validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import net.barrage.tegridy.util.Strum;
import net.barrage.tegridy.validation.annotation.EnumList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class EnumListTests {
  private static Validator validator;

  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void allValidEnumValues() {
    TestClass testObj = new TestClass(new String[] {"test", "valueOne", "valueOneTwoThree"});
    assertTrue(validator.validate(testObj).isEmpty());
  }

  @Test
  void includesInvalidEnumValue() {
    TestClass testObj = new TestClass(new String[] {"TEST", "invalid_value"});
    assertFalse(validator.validate(testObj).isEmpty());
  }

  @Test
  void emptyArrayIsValid() {
    TestClass testObj = new TestClass(new String[] {});
    assertTrue(validator.validate(testObj).isEmpty());
  }

  @Test
  void arrayWithNullElementIsInvalid() {
    TestClass testObj = new TestClass(new String[] {null, "VALUE_ONE"});
    assertFalse(validator.validate(testObj).isEmpty());
  }

  @Test
  void nullArrayIsValid() {
    TestClass testObj = new TestClass(null);
    assertTrue(validator.validate(testObj).isEmpty());
  }

  @Test
  void mixedCaseEnumValueIsInvalid() {
    TestClass testObj = new TestClass(new String[] {"Test", "Value_One"});
    assertFalse(validator.validate(testObj).isEmpty());
  }

  @Test
  void allUpperCaseEnumValuesFail() {
    TestClass testObj = new TestClass(new String[] {"TEST", "VALUE_ONE", "VALUE_ONE_TWO_THREE"});
    assertFalse(validator.validate(testObj).isEmpty());
  }

  @Test
  void containsAllInvalidEnumValues() {
    TestClass testObj = new TestClass(new String[] {"invalid1", "invalid2"});
    Set<ConstraintViolation<EnumListTests.TestClass>> validationErrors =
        validator.validate(testObj);
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
  void invalidCustomMessage() {
    TestClassCustomMessage testObj =
        new TestClassCustomMessage(new String[] {"invalid1", "invalid2"});
    Set<ConstraintViolation<EnumListTests.TestClassCustomMessage>> validationErrors =
        validator.validate(testObj);
    assertFalse(validationErrors.isEmpty());
    String expectedMessage = "Custom";
    assertTrue(
        validationErrors.stream()
            .map(ConstraintViolation::getMessage)
            .toList()
            .contains(expectedMessage),
        "Error message not expected");
  }

  private static class TestClass {
    @EnumList(value = TestEnum.class, remap = Strum.CamelCase.class)
    private String[] testField;

    public TestClass(String[] testField) {
      this.testField = testField;
    }

    private enum TestEnum {
      TEST,
      VALUE_ONE,
      VALUE_ONE_TWO_THREE
    }
  }

  private static class TestClassCustomMessage {
    @EnumList(value = TestEnum.class, remap = Strum.CamelCase.class, message = "Custom")
    private String[] testField;

    public TestClassCustomMessage(String[] testField) {
      this.testField = testField;
    }

    private enum TestEnum {
      TEST,
      VALUE_ONE,
      VALUE_ONE_TWO_THREE
    }
  }
}
