package net.barrage.tegridy.http.queryparams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface QueryParam {

  /**
   * The key the field will be used for in the query parameters. If not provided,
   * the field name is used as the key.
   *
   * @return The key that should be used as the query key
   */
  String value() default "";

  /**
   * A string template with which to format the key with. Useful if the key needs to
   * contain additional symbols.
   * <h3>
   * Example
   * </h3>
   * <br>
   * <p>
   * The following annotation appends '[]' to the key during query string generation. All chars
   * will be URL encoded.
   * </p>
   * <br>
   * <pre>
   * {@code
   * // Results in "?myField[]=myFieldValue", note the brackets will be url encoded
   * @QueryParamData(formatKey = "%s[]")
   * String myField;
   * }
   * </pre>
   *
   * @return A string template used to format the query key
   */
  String formatKey() default "";

  enum __NoEnum {

  }

}
