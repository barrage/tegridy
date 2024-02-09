package validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;
import net.barrage.tegridy.validation.annotations.compare.Compare;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

// These test cases are designed to validate the functionality of the 'compare' method within the comparator.
// They should be adapted or extended based on the specific requirements and use cases of the comparison logic.
// This ensures comprehensive testing across different scenarios where the 'compare' method is utilized.

public class CompareTests {

  private static Validator validator;

  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  public void testValidComparison() {
    TestClass testObj = new TestClass(5, 3, 5, 3);
    Set<ConstraintViolation<TestClass>> violations = validator.validate(testObj);
    assertTrue(violations.isEmpty(), "Validation should pass for valid comparison");
  }

  @Test
  public void testInvalidComparison() {
    TestClass testObj = new TestClass(2, 3, 5, 3);
    Set<ConstraintViolation<TestClass>> violations = validator.validate(testObj);
    assertFalse(violations.isEmpty(), "Validation should fail for invalid comparison");

    String expectedMessage = "Field1 must be greater than Field2";
    assertEquals(expectedMessage, violations.iterator().next().getMessage());
  }

  @Test
  public void testInvalidComparisonAllFailed() {
    TestClass testObj = new TestClass(2, 3, 2, 3);
    Set<ConstraintViolation<TestClass>> violations = validator.validate(testObj);
    assertTrue(violations.size() == 2, "Validation should fail for invalid comparison");

    String expectedMessage = "Field1 must be greater than Field2";
    assertTrue(violations.stream().map(item -> item.getMessage()).collect(Collectors.toList())
        .contains(expectedMessage));
    expectedMessage = "Field3 must be greater than Field4";
    assertTrue(violations.stream().map(item -> item.getMessage()).collect(Collectors.toList())
        .contains(expectedMessage));
  }

  @Test
  public void testValidDateComparison() {
    DateClass date = new DateClass(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
    Set<ConstraintViolation<DateClass>> violations = validator.validate(date);
    assertTrue(violations.isEmpty(), "Validation should pass for valid date comparison");
  }

  @Test
  public void testInvalidDateComparison() {
    DateClass date = new DateClass(LocalDate.of(2024, 12, 31), LocalDate.of(2024, 1, 1));
    Set<ConstraintViolation<DateClass>> violations = validator.validate(date);
    assertFalse(violations.isEmpty(), "Validation should fail for invalid date comparison");

    String expectedMessage = "Start date must be before end date";
    assertEquals(expectedMessage, violations.iterator().next().getMessage());
  }

  @Test
  public void testValidationMethodThrowsException() {
    ExceptionTestClass testObj = new ExceptionTestClass(1, 2);

    assertThrows(InvocationTargetException.class, () -> {
      validator.validate(testObj);
    }, "Validation should throw ValidationException due to forced exception in comparison method");
  }

  @Compare(baseField = "field1", comparisonField = "field2", comparisonMethod = "compare1", message = "Field1 must be greater than Field2")
  @Compare(baseField = "field3", comparisonField = "field4", comparisonMethod = "compare2", message = "Field3 must be greater than Field4")
  private static class TestClass {
    public Integer field1;
    public Integer field2;
    public Integer field3;
    public Integer field4;

    public TestClass(Integer field1, Integer field2, Integer field3, Integer field4) {
      this.field1 = field1;
      this.field2 = field2;
      this.field3 = field3;
      this.field4 = field4;
    }

    private boolean compare1(Integer field1, Integer field2) {
      return field1 != null && field2 != null && field1 > field2;
    }

    private boolean compare2(Integer field1, Integer field2) {
      return field1 != null && field2 != null && field1 > field2;
    }
  }

  @Compare(baseField = "startDate", comparisonField = "endDate", comparisonMethod = "compare", message = "Start date must be before end date")
  private static class DateClass {
    public LocalDate startDate;
    public LocalDate endDate;

    public DateClass(LocalDate startDate, LocalDate endDate) {
      this.startDate = startDate;
      this.endDate = endDate;
    }

    private boolean compare(LocalDate startDate, LocalDate endDate) {
      return startDate != null && endDate != null && startDate.isBefore(endDate);
    }
  }

  @Compare(baseField = "field1", comparisonField = "field2", comparisonMethod = "compareThatThrows", message = "This comparison method will throw an exception")
  private static class ExceptionTestClass {
    public Integer field1;
    public Integer field2;

    public ExceptionTestClass(Integer field1, Integer field2) {
      this.field1 = field1;
      this.field2 = field2;
    }

    public boolean compareThatThrows(Integer field1, Integer field2) {
      throw new IllegalArgumentException("Forced exception for testing");
    }
  }

}
