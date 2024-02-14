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
import net.barrage.tegridy.validation.annotation.RequiresNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class RequiresNullTests {
  private static Validator validator;

  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void validAllFieldsNull() {
    TestClass testObj = new TestClass();
    assertTrue(validator.validate(testObj).isEmpty());
  }

  @Test
  void validAllRequiredNull() {
    TestClass testObj = new TestClass();
    testObj.field1 = "test";
    testObj.field4 = "test";

    assertTrue(validator.validate(testObj).isEmpty());
  }

  @Test
  void invalidField1PresentField2Present() {
    TestClass testObj = new TestClass();
    testObj.field1 = "test";
    testObj.field2 = "test";
    Set<ConstraintViolation<TestClass>> validationErrors = validator.validate(testObj);
    assertFalse(validationErrors.isEmpty());
    assertTrue(validationErrors.stream()
        .anyMatch(v -> v.getMessage()
            .contains("field1 is present but the following fields are forbidden: field2, field3")));
  }

  @Test
  void invalidField4PresentField5Present() {
    TestClass testObj = new TestClass();
    testObj.field4 = "test";
    testObj.field5 = "test";
    Set<ConstraintViolation<TestClass>> validationErrors = validator.validate(testObj);
    assertFalse(validationErrors.isEmpty());
    assertTrue(validationErrors.stream()
        .anyMatch(v -> v.getMessage()
            .contains("field4 is present but the following fields are forbidden: field5")));
  }

  @Test
  void validField1PresentOthersNull() {
    TestClass testObj = new TestClass();
    testObj.field1 = "test";
    assertTrue(validator.validate(testObj).isEmpty());
  }

  @Test
  void validField4PresentOthersNull() {
    TestClass testObj = new TestClass();
    testObj.field4 = "test";
    assertTrue(validator.validate(testObj).isEmpty());
  }

  @Test
  void invalidField2Field5NotNull() {
    TestClass testObj = new TestClass();
    testObj.field1 = "test";
    testObj.field2 = "test";
    testObj.field4 = "test";
    testObj.field5 = "test";
    Set<ConstraintViolation<TestClass>> validationErrors = validator.validate(testObj);
    assertFalse(validationErrors.isEmpty());
    List<String> messages = validationErrors.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toList());
    String expectedMessage = "field4 is present but the following fields are forbidden: field5";
    assertTrue(messages
        .contains(expectedMessage), "Error message not expected");
    expectedMessage = "field1 is present but the following fields are forbidden: field2, field3";
    assertTrue(messages
        .contains(expectedMessage), "Error message not expected");
  }

  @Test
  void invalidAllNotNull() {
    TestClass testObj = new TestClass();
    testObj.field1 = "test";
    testObj.field2 = "test";
    testObj.field3 = "test";
    testObj.field4 = "test";
    testObj.field5 = "test";
    Set<ConstraintViolation<TestClass>> validationErrors = validator.validate(testObj);
    assertFalse(validationErrors.isEmpty());
    List<String> messages = validationErrors.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toList());
    String expectedMessage = "field4 is present but the following fields are forbidden: field5";
    assertTrue(messages
        .contains(expectedMessage), "Error message not expected");
    expectedMessage = "field1 is present but the following fields are forbidden: field2, field3";
    assertTrue(messages
        .contains(expectedMessage), "Error message not expected");
  }

  @Test
  void invalidCustomMessage() {
    RequiresNullTests.TestClassDefaultMessage
        testObj = new RequiresNullTests.TestClassDefaultMessage();
    testObj.field4 = "test";
    testObj.field5 = "test";
    Set<ConstraintViolation<RequiresNullTests.TestClassDefaultMessage>> validationErrors =
        validator.validate(testObj);
    assertFalse(validationErrors.isEmpty());
    List<String> messages = validationErrors.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toList());
    String expectedMessage = "Message overwritten";
    assertTrue(messages
        .contains(expectedMessage), "Error message not expected");
  }

  @RequiresNull(field = "field1", forbiddenFields = { "field2", "field3" })
  @RequiresNull(field = "field4", forbiddenFields = { "field5" })
  private static class TestClass {
    public String field1;
    public String field2;
    public String field3;
    public String field4;
    public String field5;
    public String field6;

  }

  @RequiresNull(field = "field4", forbiddenFields = { "field5" }, message = "Message overwritten")
  private static class TestClassDefaultMessage {
    public String field1;
    public String field2;
    public String field3;
    public String field4;
    public String field5;
    public String field6;

  }
}
