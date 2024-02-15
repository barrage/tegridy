package net.barrage.tegridy.modification.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import net.barrage.tegridy.modification.Modifier;

/**
 * Valid on any type. Executes custom modification using the provided class. The provided class must
 * implement {@link Modifier} and have an empty constructor.
 */
@Target({FIELD})
@Retention(RUNTIME)
public @interface ModifyCustom {

  /**
   * The class to perform the modification with - must implement {@link Modifier}.
   *
   * @return The class to execute the modification with.
   */
  Class<? extends Modifier<?>> value();
}
