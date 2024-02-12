package validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.barrage.tegridy.validation.annotation.requiresNotNull.RequiresNotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class RequiresNotNullTests {
  private static Validator validator;

  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void validAllFieldsPresent() {
    TestClass testObj = new TestClass();
    testObj.field1 = "test";
    testObj.field2 = "test";
    testObj.field3 = "test";
    testObj.field4 = "test";
    testObj.field5 = "test";
    testObj.field6 = "test";

    assertTrue(validator.validate(testObj).isEmpty());
  }

  @Test
  void invalidField3Field2Missing() {
    TestClass testObj = new TestClass();
    testObj.field1 = "test";
    testObj.field4 = "test";
    testObj.field5 = "test";
    testObj.field6 = "test";
    Set<ConstraintViolation<TestClass>> validationErrors = validator.validate(testObj);
    assertFalse(validationErrors.isEmpty());

    String expectedMessage =
        "field1 is present but the following fields are required: field2, field3";
    assertTrue(validationErrors.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toList())
        .contains(expectedMessage), "Error message not expected");
  }

  @Test
  void invalidField2Missing() {
    TestClass testObj = new TestClass();
    testObj.field1 = "test";
    testObj.field3 = "test";
    testObj.field4 = "test";
    testObj.field5 = "test";
    testObj.field6 = "test";
    Set<ConstraintViolation<TestClass>> validationErrors = validator.validate(testObj);
    assertFalse(validationErrors.isEmpty());

    String expectedMessage =
        "field1 is present but the following fields are required: field2, field3";
    assertTrue(validationErrors.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toList())
        .contains(expectedMessage), "Error message not expected");
  }

  @Test
  void invalidField3Missing() {
    TestClass testObj = new TestClass();
    testObj.field1 = "test";
    testObj.field2 = "test";
    testObj.field4 = "test";
    testObj.field5 = "test";
    testObj.field6 = "test";
    Set<ConstraintViolation<TestClass>> validationErrors = validator.validate(testObj);
    assertFalse(validationErrors.isEmpty());

    String expectedMessage =
        "field1 is present but the following fields are required: field2, field3";
    assertTrue(validationErrors.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toList())
        .contains(expectedMessage), "Error message not expected");
  }

  @Test
  void invalidField5Missing() {
    TestClass testObj = new TestClass();
    testObj.field1 = "test";
    testObj.field2 = "test";
    testObj.field3 = "test";
    testObj.field4 = "test";
    testObj.field6 = "test";
    Set<ConstraintViolation<TestClass>> validationErrors = validator.validate(testObj);
    assertFalse(validationErrors.isEmpty());

    String expectedMessage = "field4 is present but the following fields are required: field5";
    assertTrue(validationErrors.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toList())
        .contains(expectedMessage), "Error message not expected");
  }

  @Test
  void invalidField2Field5Missing() {
    TestClass testObj = new TestClass();
    testObj.field1 = "test";
    testObj.field3 = "test";
    testObj.field4 = "test";
    testObj.field6 = "test";
    Set<ConstraintViolation<TestClass>> validationErrors = validator.validate(testObj);
    assertFalse(validationErrors.isEmpty());
    List<String> messages = validationErrors.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toList());
    String expectedMessage = "field4 is present but the following fields are required: field5";
    assertTrue(messages
        .contains(expectedMessage), "Error message not expected");
    expectedMessage = "field1 is present but the following fields are required: field2, field3";
    assertTrue(messages
        .contains(expectedMessage), "Error message not expected");
  }

  @Test
  void invalidAllMissing() {
    TestClass testObj = new TestClass();
    testObj.field1 = "test";
    testObj.field4 = "test";
    testObj.field6 = "test";
    Set<ConstraintViolation<TestClass>> validationErrors = validator.validate(testObj);
    assertFalse(validationErrors.isEmpty());
    List<String> messages = validationErrors.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toList());
    String expectedMessage = "field4 is present but the following fields are required: field5";
    assertTrue(messages
        .contains(expectedMessage), "Error message not expected");
    expectedMessage = "field1 is present but the following fields are required: field2, field3";
    assertTrue(messages
        .contains(expectedMessage), "Error message not expected");
  }

  @Test
  void invalidCustomMessage() {
    TestClassDefaultMessage testObj = new TestClassDefaultMessage();
    testObj.field4 = "test";
    Set<ConstraintViolation<TestClassDefaultMessage>> validationErrors =
        validator.validate(testObj);
    assertFalse(validationErrors.isEmpty());
    List<String> messages = validationErrors.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toList());
    String expectedMessage = "Message overwritten";
    assertTrue(messages
        .contains(expectedMessage), "Error message not expected");
  }

  @RequiresNotNull(field = "field1", requiresFields = { "field2", "field3" })
  @RequiresNotNull(field = "field4", requiresFields = { "field5" })
  private static class TestClass {
    public String field1;
    public String field2;
    public String field3;
    public String field4;
    public String field5;
    public String field6;

  }

  @RequiresNotNull(field = "field4", requiresFields = { "field5" }, message = "Message overwritten")
  private static class TestClassDefaultMessage {
    public String field1;
    public String field2;
    public String field3;
    public String field4;
    public String field5;
    public String field6;

  }
}
