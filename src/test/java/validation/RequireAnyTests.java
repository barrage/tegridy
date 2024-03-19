package validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.barrage.tegridy.validation.annotation.RequireAny;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class RequireAnyTests {
  private static Validator validator;

  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void correctlyValidatesNonNull() {
    var foo = new Foo("foo", 420, null);
    var violations = validator.validate(foo);
    assertTrue(violations.isEmpty());
  }

  @Test
  void correctlyValidatesNull() {
    var foo = new Foo();
    var violations = validator.validate(foo);
    String expectedMessage = "Object must have at least one non-null field";
    assertEquals(expectedMessage, violations.iterator().next().getMessage());
  }

  @Test
  void correctlyValidatesNullWithMessage() {
    var foo = new FooMessage();
    var violations = validator.validate(foo);
    String expectedMessage = "No null 4 U";
    assertEquals(expectedMessage, violations.iterator().next().getMessage());
  }

  @AllArgsConstructor
  @NoArgsConstructor
  @RequireAny
  static class Foo {
    String foo;
    Integer bar;
    List<String> qux;
  }

  @NoArgsConstructor
  @RequireAny(message = "No null 4 U")
  static class FooMessage {
    String foo;
  }
}
