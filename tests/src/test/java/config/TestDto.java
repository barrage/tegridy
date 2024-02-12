package config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.Data;
import net.barrage.tegridy.modification.annotation.Modifier;

@Data
@Valid
public class TestDto {
  @Size(min = 2, max = 4)
  String foo;
}
