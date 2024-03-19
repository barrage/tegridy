import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import net.barrage.tegridy.Tegridy;
import net.barrage.tegridy.modification.Modify;
import net.barrage.tegridy.modification.annotation.ModifyTrim;
import org.junit.jupiter.api.Test;

public class TegridyTests {
  @Test
  void validateValidWorks() {
    var test = new ValidateTest("yeah");
    assertDoesNotThrow(() -> Tegridy.validate(test));
  }

  @Test
  void validateInvalidWorks() {
    var test = new ValidateTest("yeahahsjahsa");
    assertThrows(ConstraintViolationException.class, () -> Tegridy.validate(test));
  }

  @Test
  void validifyValidWorks() {
    var test = new ValidifyTest("      yeah       ");
    assertDoesNotThrow(() -> Tegridy.validify(test));
  }

  @Test
  void validifyInvalidWorks() {
    var test = new ValidifyTest("      yeahahsjahsa       ");
    assertThrows(ConstraintViolationException.class, () -> Tegridy.validify(test));
  }

  @AllArgsConstructor
  static class ValidateTest {
    @Size(min = 2, max = 5)
    String foo;
  }

  @AllArgsConstructor
  static class ValidifyTest implements Modify {
    @ModifyTrim
    @Size(min = 2, max = 5)
    String foo;
  }
}
