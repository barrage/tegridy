package validation.processor.compare.testClasses;

import java.time.LocalDateTime;
import net.barrage.tegridy.validation.annotation.Compare;

@Compare(baseField = "startDate", comparisonField = "endDate", comparisonMethod = "isAfter")
public class CorrectClass {
  private LocalDateTime startDate;
  private LocalDateTime endDate;

  public Boolean isAfter(LocalDateTime startDate, LocalDateTime endDate) {
    return endDate.isAfter(startDate);
  }
}
