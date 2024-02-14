package net.barrage.tegridy.modification.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Valid only on Strings. Makes the first character of the String capitalized.
 */
@Target({ FIELD })
@Retention(RUNTIME)
public @interface ModifyCapitalize {
}
