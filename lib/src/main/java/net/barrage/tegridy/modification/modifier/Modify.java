package net.barrage.tegridy.modification.modifier;

public interface Modify {
  default void modify() {
    var clazz = this.getClass();

    var fields = clazz.getDeclaredFields();

    for (var field : fields ) {
    }
  }
}
