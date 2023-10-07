package io.github.cooperlyt.utils;

public class BigDecimalUtils {

  public static boolean equals(java.math.BigDecimal a, java.math.BigDecimal b) {
    if (a == null && b == null) {
      return true;
    }
    if (a == null || b == null) {
      return false;
    }
    return a.compareTo(b) == 0;
  }
}
