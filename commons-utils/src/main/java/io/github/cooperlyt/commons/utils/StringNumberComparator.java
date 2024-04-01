package io.github.cooperlyt.commons.utils;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class StringNumberComparator implements Comparator<String> {

  public static Comparator<String> fisrtNumberComparator() {
    return new RegexStringNumberComparator("\\d+");
  }

  public static Comparator<String> lastNumberComparator() {
    return new RegexStringNumberComparator("(\\d+)(?!.*\\d)");
  }

  public static class RegexStringNumberComparator extends StringNumberComparator {
    private final Pattern pattern;

    public RegexStringNumberComparator(String regex) {
      this.pattern = Pattern.compile(regex);
    }

    @Override
    protected Long extractNumber(String str) {
      Matcher matcher = pattern.matcher(str);
      if (matcher.find()) {
        return Long.parseLong(matcher.group());
      }
      return null;
    }
  }


  protected abstract Long extractNumber(String str);

  @Override
  public int compare(String s1, String s2) {
    Long num1 = extractNumber(s1);
    Long num2 = extractNumber(s2);

    if (num1 != null && num2 != null) {
      return num1.compareTo(num2);
    } else if (num1 != null) {
      return -1;
    } else if (num2 != null) {
      return 1;
    } else {
      return s1.compareTo(s2);
    }
  }
}
