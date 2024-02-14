import static org.junit.jupiter.api.Assertions.assertEquals;

import config.App;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = App.class)
public class ControllerTests {
  @Autowired
  TestRestTemplate restTemplate;

  @Test
  void works() {
    ResponseEntity<Void> response =
        restTemplate.getForEntity("/test/regular?foo=agileframeworks&custom=hello", Void.class);
    assertEquals(200, response.getStatusCode().value());
  }
}
