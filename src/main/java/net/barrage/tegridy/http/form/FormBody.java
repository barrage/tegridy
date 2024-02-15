package net.barrage.tegridy.http.form;

import java.util.Collection;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Implement on classes that need to be transformed into multipart payloads. Only non null values of
 * the classes will get mapped with the exception of primitives, which take on their default value.
 */
public interface FormBody {
  default MultiValueMap<String, Object> intoFormParts() {
    var clazz = this.getClass();

    MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();

    var fields = clazz.getDeclaredFields();

    for (var field : fields) {

      var formData = field.getAnnotation(FormData.class);
      if (formData == null) {
        continue;
      }

      var formKey = formData.value();

      if (formKey.isBlank()) {
        formKey = field.getName();
      }

      try {
        field.setAccessible(true);
        var value = field.get(this);

        if (value == null) {
          continue;
        }

        // Check for arrays/collections
        Class<?> type = field.getType();

        if (Object[].class.isAssignableFrom(type)) {
          var actual = (Object[]) value;
          for (var val : actual) {
            map.add(formKey, val.toString());
          }
          continue;
        }

        if (Collection.class.isAssignableFrom(type)) {
          var actual = (Collection<?>) value;
          for (var val : actual) {
            map.add(formKey, val.toString());
          }
          continue;
        }

        map.add(formKey, value);
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }

    return map;
  }
}
