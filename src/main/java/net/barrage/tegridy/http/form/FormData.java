package net.barrage.tegridy.http.form;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FormData {
  /**
   * The key the field will be used for in the multipart form request.
   *
   * @return The key
   */
  String value() default "";
}