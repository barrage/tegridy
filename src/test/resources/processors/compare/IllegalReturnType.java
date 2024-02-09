package validation.processors.compare.testClasses;

import java.time.LocalDateTime;
import net.barrage.tegridy.validation.annotations.compare.Compare;

@Compare(baseField = "startDate", comparisonField = "endDate", comparisonMethod = "isAfter")
public class IllegalReturnType {
  private LocalDateTime startDate;
  private LocalDateTime endDate;

  public String isAfter(LocalDateTime startDate, LocalDateTime endDate) {
    return "Illegal";
  }
}
