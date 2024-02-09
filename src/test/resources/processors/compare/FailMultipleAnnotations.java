package validation.processors.compare.testClasses;

import java.time.LocalDateTime;
import net.barrage.tegridy.validation.annotation.compare.Compare;

@Compare(baseField = "startDate", comparisonField = "endDate", comparisonMethod = "isAfter")
@Compare(baseField = "test1", comparisonField = "test2", comparisonMethod = "testMethod")
@Compare(baseField = "test3", comparisonField = "test2", comparisonMethod = "isAfter")
public class FailMultipleAnnotations {
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private Integer test1;
  private Integer test2;
  private Integer test3;

  private Boolean isAfter(LocalDateTime startDate, LocalDateTime endDate) {
    return endDate.isAfter(startDate);
  }

  private Boolean testMethod(Integer test1, Integer test2) {
    return true;
  }
}
