package validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import net.barrage.tegridy.validation.annotation.Custom;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

// Test cases here check how our SchemeValidation works in different situations.
// They handle cases with one or more argument fields. Remember to tweak or add more tests
// depending on what you need for your specific validation rules.

public class CustomTests {

  private static Validator validator;

  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void testValidationPass() {
    TestClass obj = new TestClass(6, "Test", "Test");
    Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
    assertTrue(violations.isEmpty(), "Validation should pass");
  }

  @Test
  void testValidationFailGreaterThan5() {
    TestClass obj = new TestClass(5, "Test", "Test");
    Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
    assertFalse(violations.isEmpty(), "Validation should fail");
    String expectedMessage = "field1 must be greater than 5";
    assertEquals(expectedMessage, violations.iterator().next().getMessage());
  }

  @Test
  void testValidationFailGreaterThan4() {
    TestClass obj = new TestClass(1, "Test", "Test");
    Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
    assertFalse(violations.isEmpty(), "Validation should fail");
    String expectedMessage = "field1 must be greater than 5";
    assertEquals(2, violations.size(), "Field1 should fail");
    assertTrue(
        violations.stream()
            .map(ConstraintViolation::getMessage)
            .toList()
            .contains(expectedMessage));
    expectedMessage = "field1 must be greater than 4";
    assertTrue(
        violations.stream()
            .map(ConstraintViolation::getMessage)
            .toList()
            .contains(expectedMessage));
  }

  @Test
  void testValidationFailSecond() {
    TestClass obj = new TestClass(6, "Fail", "Test");
    Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
    assertFalse(violations.isEmpty(), "Validation should fail");
    String expectedMessage = "field2 must contain 's'";
    assertEquals(expectedMessage, violations.iterator().next().getMessage());
  }

  @Test
  void testValidationFailAll() {
    TestClass obj = new TestClass(1, "Fail", "Test");
    Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
    assertFalse(violations.isEmpty(), "Validation should fail");
    String expectedMessage = "field1 must be greater than 5";
    assertEquals(3, violations.size(), "All should fail");
    assertTrue(
        violations.stream()
            .map(ConstraintViolation::getMessage)
            .toList()
            .contains(expectedMessage));
    expectedMessage = "field1 must be greater than 4";
    assertTrue(
        violations.stream()
            .map(ConstraintViolation::getMessage)
            .toList()
            .contains(expectedMessage));
    expectedMessage = "field2 must contain 's'";
    assertTrue(
        violations.stream()
            .map(ConstraintViolation::getMessage)
            .toList()
            .contains(expectedMessage));
  }

  public static class TestClass {
    @Custom(
        _validatorClass = TestClass.class,
        method = "higherThan5",
        message = "field1 must be greater than 5")
    @Custom(
        _validatorClass = TestClass.class,
        method = "higherThan4",
        message = "field1 must be greater than 4")
    public Integer field1;

    @Custom(
        _validatorClass = TestClass.class,
        method = "containsS",
        message = "field2 must contain 's'")
    public String field2;

    public String field3;

    public TestClass(Integer field1, String field2, String field3) {
      this.field1 = field1;
      this.field2 = field2;
      this.field3 = field3;
    }

    private static boolean higherThan5(Integer val) {
      return val > 5;
    }

    private static boolean higherThan4(Integer val) {
      return val > 4;
    }

    private static boolean containsS(String val) {
      return val.contains("s");
    }
  }
}
