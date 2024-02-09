package net.barrage.tegridy.utils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StringUtils {
  /**
   * Mostly used in validators that map strings to enums. This will only map
   * snake_case/SCREAM_CASE strings. Any string passed in that does not contains a '_'
   * will be left as is.
   *
   * @param input Input string
   * @return A lower camelCased version of the string.
   */
  public static String toLowerCamelCase(String input) {
    String[] parts = input.split("_");

    // Do not touch anything with no _
    if (parts.length <= 1) {
      return input;
    }

    String camelCaseString = Arrays.stream(parts, 1, parts.length)
        .map(part -> part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase())
        .collect(Collectors.joining());

    return parts[0].toLowerCase() + camelCaseString;
  }
}
