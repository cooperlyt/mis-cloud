package io.github.cooperlyt.commons.utils;

public class OptionalUtils {

  public static java.util.Optional<String> justOrEmpty(String optional) {
    if (org.springframework.util.StringUtils.hasText(optional)) {
      return java.util.Optional.of(optional);
    }
    return java.util.Optional.empty();
  }
}
