import java.time.LocalDateTime;
import net.barrage.tegridy.validation.annotation.Compare;

@Compare(baseField = "startDate", comparisonField = "endDate", comparisonMethod = "isAfter")
public class MissingBaseField {
  private LocalDateTime startDate;

  private boolean isAfter(LocalDateTime startDate, LocalDateTime endDate) {
    return true;
  }
}
