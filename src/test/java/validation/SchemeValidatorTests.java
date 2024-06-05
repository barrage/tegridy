package validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import net.barrage.tegridy.validation.annotation.Scheme;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

// Test cases here check how our SchemeValidation works in different situations.
// They handle cases with one or more argument fields. Remember to tweak or add more tests
// depending on what you need for your specific validation rules.

public class SchemeValidatorTests {

  private static Validator validator;

  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void testValidationPass() {
    TestClass obj = new TestClass(5, 1, "Test");
    Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
    assertTrue(violations.isEmpty(), "Validation should pass");
  }

  @Test
  void testValidationFail() {
    TestClass obj = new TestClass(1, 5, "Test");
    Set<ConstraintViolation<TestClass>> violations = validator.validate(obj);
    assertFalse(violations.isEmpty(), "Validation should fail");
    String expectedMessage = "Base field must be greater than field1";
    assertEquals(expectedMessage, violations.iterator().next().getMessage());
  }

  @Test
  void testValidationPassMultiple() {
    TestClassMultiple obj = new TestClassMultiple(5, 1, "Test", "true");
    Set<ConstraintViolation<TestClassMultiple>> violations = validator.validate(obj);
    assertTrue(violations.isEmpty(), "Validation should pass");
  }

  @Test
  void testValidationFailMultipleFirst() {
    TestClassMultiple obj = new TestClassMultiple(1, 5, "Test", "true");
    Set<ConstraintViolation<TestClassMultiple>> violations = validator.validate(obj);
    assertEquals(1, violations.size(), "Only first should fail");
    String expectedMessage = "Base field must be greater than field1";
    assertEquals(expectedMessage, violations.iterator().next().getMessage());
  }

  @Test
  void testValidationFailMultipleSecond() {
    TestClassMultiple obj = new TestClassMultiple(5, 1, "Test", "FALSE");
    Set<ConstraintViolation<TestClassMultiple>> violations = validator.validate(obj);
    assertEquals(1, violations.size(), "Only second should fail");
    String expectedMessage = "field3 should say true";
    assertEquals(expectedMessage, violations.iterator().next().getMessage());
  }

  @Test
  void testValidationFailMultipleAll() {
    TestClassMultiple obj = new TestClassMultiple(5, 6, "Test", "FALSE");
    Set<ConstraintViolation<TestClassMultiple>> violations = validator.validate(obj);
    assertEquals(2, violations.size(), "All should fail");
    String expectedMessage = "Base field must be greater than field1";
    assertTrue(
        violations.stream()
            .map(ConstraintViolation::getMessage)
            .toList()
            .contains(expectedMessage));
    expectedMessage = "field3 should say true";
    assertTrue(
        violations.stream()
            .map(ConstraintViolation::getMessage)
            .toList()
            .contains(expectedMessage));
  }

  @Scheme(
      baseField = "baseField",
      argumentFields = {"field1", "field2"},
      method = "validate",
      message = "Base field must be greater than field1")
  public static class TestClass {
    public Integer baseField;
    public Integer field1;
    public String field2;

    public TestClass(Integer baseField, Integer field1, String field2) {
      this.baseField = baseField;
      this.field1 = field1;
      this.field2 = field2;
    }

    private boolean validate(Integer baseField, Integer field1, String c) {
      return baseField > field1;
    }
  }

  @Scheme(
      baseField = "baseField",
      argumentFields = {"field1", "field2"},
      method = "validate1",
      message = "Base field must be greater than field1")
  @Scheme(
      baseField = "field1",
      argumentFields = {"field3", "baseField"},
      method = "validate2",
      message = "field3 should say true")
  public static class TestClassMultiple {
    public Integer baseField;
    public Integer field1;
    public String field2;

    public String field3;

    public TestClassMultiple(Integer baseField, Integer field1, String field2, String field3) {
      this.baseField = baseField;
      this.field1 = field1;
      this.field2 = field2;
      this.field3 = field3;
    }

    private boolean validate1(Integer baseField, Integer field1, String c) {
      return baseField > field1;
    }

    private boolean validate2(Integer field1, String field3, Integer baseField) {
      return field3 == "true";
    }
  }
}
