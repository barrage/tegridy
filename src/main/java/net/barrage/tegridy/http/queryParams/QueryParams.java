package net.barrage.tegridy.http.queryParams;

import java.util.Collection;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Provides a single method to convert the implementing class to a query parameter string.
 */
public interface QueryParams {

  /**
   * Convert this instance to query parameters
   *
   * @return The query string
   */
  default String toQueryParams() {

    var fields = this.getClass().getDeclaredFields();

    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

    for (var field : fields) {
      try {
        var queryParamData = field.getAnnotation(QueryParam.class);

        field.setAccessible(true);
        Object value = field.get(this);

        if (queryParamData == null || value == null) {
          continue;
        }

        // Construct query key

        var queryKey =
            queryParamData.value().isBlank() ? field.getName() : queryParamData.value();

        if (!queryParamData.formatKey().isBlank()) {
          queryKey = String.format(queryParamData.formatKey(), queryKey);
        }

        Class<?> type = field.getType();

        // Check for arrays/collections

        if (Object[].class.isAssignableFrom(type)) {
          var actual = (Object[]) value;
          for (var val : actual) {
            map.add(queryKey, val.toString());
          }
          continue;
        }

        if (Collection.class.isAssignableFrom(type)) {
          var actual = (Collection<?>) value;
          for (var val : actual) {
            map.add(queryKey, val.toString());
          }
          continue;
        }

        map.add(queryKey, value.toString());
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }

    var query = UriComponentsBuilder.newInstance().queryParams(map).build().encode().getQuery();

    return query == null ? "" : query;
  }
}
