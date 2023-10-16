package io.github.cooperlyt.commons.utils;

import org.apache.commons.lang3.StringUtils;

public class OptionalUtils {

  public static java.util.Optional<String> justOrEmpty(String optional) {
    if (StringUtils.isNotBlank(optional)) {
      return java.util.Optional.of(optional);
    }
    return java.util.Optional.empty();
  }
}
