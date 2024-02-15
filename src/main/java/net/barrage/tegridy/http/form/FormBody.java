package net.barrage.tegridy.http.form;

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

      field.setAccessible(true);
      try {
        var fieldValue = field.get(this);
        if (fieldValue != null) {
          map.add(formKey, field.get(this));
        }
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }

    return map;
  }
}
