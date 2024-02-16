package net.barrage.tegridy.util;

import java.util.Arrays;
import java.util.stream.Collectors;
import net.barrage.tegridy.validation.annotation.EnumList;
import net.barrage.tegridy.validation.annotation.EnumString;

/**
 * A STRing enUM variant mapper. Provides classed for remapping SCREAM_CASE enum variants to any
 * case as well as static methods for the inverse. Useful when arbitrary strings must conform to one
 * of many enumerated types.
 *
 * <p>Mainly used for the {@link EnumString} and {@link EnumList} validators.
 */
public abstract class Strum {

  public abstract String remap(String input);

  /** Noop mapper. The default for {@link EnumString}. See {@link Strum} for usage details. */
  public static class NoMap extends Strum {
    @Override
    public String remap(String input) {
      return input;
    }
  }

  /** Remaps the enum variants to `camelCase`. See {@link Strum} for usage details. */
  public static class CamelCase extends Strum {
    @Override
    public String remap(String input) {
      String[] parts = input.split("_");

      String camelCaseString =
          Arrays.stream(parts, 1, parts.length)
              .map(part -> part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase())
              .collect(Collectors.joining());
      return parts[0].toLowerCase() + camelCaseString;
    }
  }

  /** Remaps the enum variants to `PascalCase`. See {@link Strum} for usage details. */
  public static class PascalCase extends Strum {
    @Override
    public String remap(String input) {
      String[] parts = input.split("_");

      String camelCaseString =
          Arrays.stream(parts, 1, parts.length)
              .map(part -> part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase())
              .collect(Collectors.joining());
      return parts[0].charAt(0) + parts[0].substring(1).toLowerCase() + camelCaseString;
    }
  }

  /** Remaps the enum variants to `snake_case`. See {@link Strum} for usage details. */
  public static class SnakeCase extends Strum {
    @Override
    public String remap(String input) {
      return input.toLowerCase();
    }
  }

  /** Remaps the enum variants to `kebab-case`. See {@link Strum} for usage details. */
  public static class KebabCase extends Strum {
    @Override
    public String remap(String input) {
      return input.toLowerCase().replaceAll("_", "-");
    }
  }

  /** Remaps the enum variants to `SCREAMING-KEBAB-CASE`. See {@link Strum} for usage details. */
  public static class ScreamingKebabCase extends Strum {
    @Override
    public String remap(String input) {
      return input.replaceAll("_", "-");
    }
  }

  /**
   * Attempts to convert a camelCase string to a variant of the given enum. The enum variants must
   * be in SCREAM_CASE. Useful in conjunction with the {@link EnumString} and {@link EnumList}
   * validators so one can be sure they are working with legitimate enum values.
   *
   * @param input The string to convert. Must be in camelCase.
   * @param e The enum class that holds the desired variant.
   * @param <T> The enum type.
   * @return A variant of the given enum class.
   * @throws IllegalArgumentException If the string cannot be transformed to any of the enum's
   *     variants.
   * @throws NullPointerException If any of the inputs are `null`.
   */
  public static <T extends Enum<T>> T camelToVariant(String input, Class<T> e)
      throws IllegalArgumentException, NullPointerException {
    String[] words = input.split("(?=[A-Z])");
    String str = String.join("_", words).toUpperCase();
    return Enum.valueOf(e, str);
  }

  /**
   * Attempts to convert a PascalCase string to a variant of the given enum. The enum variants must
   * be in SCREAM_CASE. Useful in conjunction with the {@link EnumString} and {@link EnumList}
   * validators so one can be sure they are working with legitimate enum values.
   *
   * @param input The string to convert. Must be in PascalCase.
   * @param e The enum class that holds the desired variant.
   * @param <T> The enum type.
   * @return A variant of the given enum class.
   * @throws IllegalArgumentException If the string cannot be transformed to any of the enum's
   *     variants.
   * @throws NullPointerException If any of the inputs are `null`.
   */
  public static <T extends Enum<T>> T pascalToVariant(String input, Class<T> e)
      throws IllegalArgumentException, NullPointerException {
    String[] words = input.split("(?=[A-Z])");
    String str = String.join("_", words).toUpperCase();
    return Enum.valueOf(e, str);
  }

  /**
   * Attempts to convert a kebab-case string to a variant of the given enum. The enum variants must
   * be in SCREAM_CASE. Useful in conjunction with the {@link EnumString} and {@link EnumList}
   * validators so one can be sure they are working with legitimate enum values.
   *
   * @param input The string to convert. Must be in kebab-case.
   * @param e The enum class that holds the desired variant.
   * @param <T> The enum type.
   * @return A variant of the given enum class.
   * @throws IllegalArgumentException If the string cannot be transformed to any of the enum's
   *     variants.
   * @throws NullPointerException If any of the inputs are `null`.
   */
  public static <T extends Enum<T>> T kebabToVariant(String input, Class<T> e)
      throws IllegalArgumentException, NullPointerException {
    var str = input.replaceAll("-", "_").toUpperCase();
    return Enum.valueOf(e, str);
  }

  /**
   * Attempts to convert a SCREAMING-KEBAB-CASE string to a variant of the given enum. The enum
   * variants must be in SCREAM_CASE. Useful in conjunction with the {@link EnumString} and {@link
   * EnumList} validators so one can be sure they are working with legitimate enum values.
   *
   * @param input The string to convert. Must be in SCREAMING-KEBAB-CASE.
   * @param e The enum class that holds the desired variant.
   * @param <T> The enum type.
   * @return A variant of the given enum class.
   * @throws IllegalArgumentException If the string cannot be transformed to any of the enum's
   *     variants.
   * @throws NullPointerException If any of the inputs are `null`.
   */
  public static <T extends Enum<T>> T screamingKebabToVariant(String input, Class<T> e)
      throws IllegalArgumentException, NullPointerException {
    var str = input.replaceAll("-", "_");
    return Enum.valueOf(e, str);
  }

  /**
   * Attempts to convert a snake_case string to a variant of the given enum. The enum variants must
   * be in SCREAM_CASE. Useful in conjunction with the {@link EnumString} and {@link EnumList}
   * validators so one can be sure they are working with legitimate enum values.
   *
   * @param input The string to convert. Must be in snake_case.
   * @param e The enum class that holds the desired variant.
   * @param <T> The enum type.
   * @return A variant of the given enum class.
   * @throws IllegalArgumentException If the string cannot be transformed to any of the enum's
   *     variants.
   * @throws NullPointerException If any of the inputs are `null`.
   */
  public static <T extends Enum<T>> T snakeToVariant(String input, Class<T> e)
      throws IllegalArgumentException, NullPointerException {
    var str = input.toUpperCase();
    return Enum.valueOf(e, str);
  }
}
