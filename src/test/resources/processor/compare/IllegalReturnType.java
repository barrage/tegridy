package validation.processor.compare.testClasses;

import java.time.LocalDateTime;
import net.barrage.tegridy.validation.annotation.compare.Compare;

@Compare(baseField = "startDate", comparisonField = "endDate", comparisonMethod = "isAfter")
public class IllegalReturnType {
  private LocalDateTime startDate;
  private LocalDateTime endDate;

  public String isAfter(LocalDateTime startDate, LocalDateTime endDate) {
    return "Illegal";
  }
}
