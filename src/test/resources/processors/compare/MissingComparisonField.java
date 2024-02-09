package validation.processors.compare.testClasses;

import java.time.LocalDateTime;
import net.barrage.tegridy.validation.annotation.compare.Compare;

@Compare(baseField = "startDate", comparisonField = "endDate", comparisonMethod = "isAfter")
public class MissingComparisonField {
  private LocalDateTime endDate;

  private boolean isAfter(LocalDateTime startDate, LocalDateTime endDate) {
    return true;
  }
}

