package validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.barrage.tegridy.validation.annotation.Ip4;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class IpTests {
  private static Validator validator;

  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void validIp4Pass() {
    var foo = new Foo("127.0.0.1");
    var violations = validator.validate(foo);
    assertTrue(violations.isEmpty());

    var bar = new Foo("255.163.0.1");
    var violations2 = validator.validate(bar);
    assertTrue(violations2.isEmpty());

    var qux = new Foo("0.0.0.0");
    var violations3 = validator.validate(qux);
    assertTrue(violations3.isEmpty());
  }

  @Test
  void invalidIp4Fail() {
    var foo = new Foo("invalid");
    var violations = validator.validate(foo);
    assertEquals(1, violations.size());

    var bar = new Foo("256.420.69.0");
    var violations2 = validator.validate(bar);
    assertEquals(1, violations2.size());

    var qux = new Foo("0.0.0.0.0.0");
    var violations3 = validator.validate(qux);
    assertEquals(1, violations3.size());

    var quz = new Foo("192.168.0.69/24");
    var violations4 = validator.validate(quz);
    assertEquals(1, violations4.size());
    assertEquals("Invalid IPv4 address", violations4.stream().toList().get(0).getMessage());
  }

  @Test
  void invalidIp4Message() {
    var foo = new FooMessage("nevalja");
    var violations = validator.validate(foo);
    assertEquals(1, violations.size());
    assertEquals("Jel ti treba to u zivotu?", violations.stream().toList().get(0).getMessage());
  }

  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  static class Foo {
    @Ip4 String ip;
  }

  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  static class FooMessage {
    @Ip4(message = "Jel ti treba to u zivotu?") String ip;
  }
}
