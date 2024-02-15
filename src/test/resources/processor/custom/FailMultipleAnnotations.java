package validation.processor.compare.testClasses;

import java.time.LocalDateTime;
import net.barrage.tegridy.validation.annotation.Custom;

@Custom(baseField = "startDate", argumentFields = { "endDate" }, method = "isAfter")
@Custom(baseField = "test1", argumentFields = { "test2", "test3" }, method = "testMethod")
@Custom(baseField = "endDate", method = "oneArgument")
@Custom(baseField = "test2", argumentFields = { "test1" }, method = "isAfter")
public class FailMultipleAnnotations {
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private Integer test1;
  private Integer test2;
  private Integer test3;

  private Boolean isAfter(LocalDateTime startDate, LocalDateTime endDate) {
    return endDate.isAfter(startDate);
  }

  private Boolean testMethod(Integer test1, Integer test2, Integer test3) {
    return true;
  }

  private Boolean oneArgument(LocalDateTime endDate) {
    return true;
  }
}
