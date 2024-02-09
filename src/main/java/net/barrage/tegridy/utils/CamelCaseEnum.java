package net.barrage.tegridy.utils;

public class CamelCaseEnum {
  public static <T extends Enum<T>> T variant(String camelCaseString, Class<T> e)
      throws IllegalArgumentException, NullPointerException {
    String[] words = camelCaseString.split("(?=[A-Z])"); // Split camelCaseString into words
    String underscoredString = String.join("_", words)
        .toUpperCase(); // Join words with underscores and convert to upper case
    return Enum.valueOf(e, underscoredString);
  }
}
