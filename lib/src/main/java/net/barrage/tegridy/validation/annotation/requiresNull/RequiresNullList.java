package net.barrage.tegridy.validation.annotation.requiresNull;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ TYPE, ANNOTATION_TYPE })
@Documented
public @interface RequiresNullList {
  RequiresNull[] value();
}
