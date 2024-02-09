package validation.processors.compare.testClasses;

import java.time.LocalDateTime;
import java.lang.Integer;
import net.barrage.tegridy.validation.annotations.compare.Compare;

@Compare(baseField = "startDate", comparisonField = "endDate", comparisonMethod = "isAfter")
public class IllegalParameters {
  private LocalDateTime startDate;
  private LocalDateTime endDate;

  public boolean isAfter(LocalDateTime startDate, Integer endDate) {
    return endDate.isAfter(startDate);
  }
}
