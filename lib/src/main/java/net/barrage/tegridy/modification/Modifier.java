package net.barrage.tegridy.modification;

import net.barrage.tegridy.modification.annotation.ModifyCustom;

/**
 * Interface that must be implemented when one requires custom modification.
 * <p>
 * Any <strong>public</strong> class with an empty constructor that implements this
 * interface is valid in {@link ModifyCustom}.
 * <p>
 * Note, the class must be public and the Modifier generic must match the type of the field,
 * otherwise a compilation error is thrown.
 *
 * <pre>
 *
 * {@code
 * public class MyDTO implements Modify {
 *    @ModifyCustom(MyModifier.class)
 *    String foo;
 *
 *    public static class MyModifier implements Modifier<String>{
 *      @Override
 *      public String doModify(String input) {
 *       return "I have been modified";
 *      }
 *    }
 * }
 * }
 * </pre>
 *
 * @param <T> The type to modify.
 */
public interface Modifier<T> {
  T doModify(T input);
}
