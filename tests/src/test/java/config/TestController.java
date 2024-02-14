package config;

import jakarta.validation.Validator;
import jakarta.validation.groups.Default;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

  @Autowired
  Validator validator;

  @GetMapping("/regular")
  public ResponseEntity<Void> get(TestDto dto) {
    dto.modify();
    System.out.println("MY DTO: " + dto);
    var violations = validator.validate(dto, Default.class);
    System.out.println("MY VIOL: " + violations);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/recursive")
  public ResponseEntity<Void> getRecursive(TestDto dto) {
    dto.modify();
    System.out.println("MY DTO: " + dto);
    var violations = validator.validate(dto, Default.class);
    System.out.println("MY VIOL: " + violations);
    return ResponseEntity.ok().build();
  }
}
