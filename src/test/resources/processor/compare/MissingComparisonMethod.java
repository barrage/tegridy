package validation.processor.compare.testClasses;

import java.time.LocalDateTime;
import net.barrage.tegridy.validation.annotation.compare.Compare;

@Compare(baseField = "startDate", comparisonField = "endDate", comparisonMethod = "isAfter")
public class MissingComparisonMethod {
  private LocalDateTime startDate;
  private LocalDateTime endDate;

  private boolean wrongName(LocalDateTime startDate, LocalDateTime endDate) {
    return true;
  }
}

