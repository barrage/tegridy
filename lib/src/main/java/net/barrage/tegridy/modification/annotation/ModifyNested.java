package net.barrage.tegridy.modification.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import net.barrage.tegridy.modification.Modify;

/**
 * Valid on any type that implements {@link Modify}. Run modifications on the child as per its
 * Modify implementation.
 */
@Target({ FIELD })
@Retention(RUNTIME)
public @interface ModifyNested {
}
