package net.barrage.tegridy.http.form;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to indicate a field should be included in the generated form data map. The class to which
 * this field belongs to must implement {@link FormBody}. If the annotation is given a value, the
 * value will be used as a key, otherwise the field name is used as the key. Multiple fields can be
 * annotated under the same key.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FormData {
  /**
   * The key the field will be used for in the multipart form request. A single key may have
   * multiple values.
   *
   * @return The key
   */
  String value() default "";
}
