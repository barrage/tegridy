package net.barrage.tegridy.modification.annotation;

import java.lang.reflect.Method;

public enum Modifier {
  LOWERCASE,
  UPPERCASE,
  TRIM,
  CAPITALIZE,
  CUSTOM();

  public static interface ModifierFn<T> {
    void modify(T arg);
  }
}
