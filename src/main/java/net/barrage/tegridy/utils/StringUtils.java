package net.barrage.tegridy.utils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StringUtils {
  public static String toLowerCamelCase(String s) {
    String[] parts = s.split("_");
    String camelCaseString = Arrays.stream(parts, 1, parts.length)
        .map(part -> part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase())
        .collect(Collectors.joining());
    return parts[0].toLowerCase() + camelCaseString;
  }
}
