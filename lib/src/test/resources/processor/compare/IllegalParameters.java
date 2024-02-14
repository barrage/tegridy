package validation.processor.compare.testClasses;

import java.time.LocalDateTime;
import net.barrage.tegridy.validation.annotation.Compare;

@Compare(baseField = "startDate", comparisonField = "endDate", comparisonMethod = "isAfter")
public class IllegalParameters {
  private LocalDateTime startDate;
  private LocalDateTime endDate;

  public boolean isAfter(LocalDateTime startDate, Integer endDate) {
    return endDate.isAfter(startDate);
  }
}
