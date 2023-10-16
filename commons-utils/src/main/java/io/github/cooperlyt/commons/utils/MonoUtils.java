package io.github.cooperlyt.commons.utils;

import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

import java.util.Collection;

public class MonoUtils {

  public static Mono<String> justOrHasText(String optional) {
    if (StringUtils.isNotBlank(optional)) {
      return Mono.just(optional);
    }
    return Mono.empty();
  }

  public static  <T,E extends Collection<T>> Mono<E> justOrEmpty(E collection) {
    if (collection != null && !collection.isEmpty()) {
      return Mono.just(collection);
    }
    return Mono.empty();
  }

  public static <T> Mono<T> justOrEmpty(boolean valid, T value) {
    if (valid) {
      return Mono.just(value);
    }
    return Mono.empty();
  }

  public static <T> Mono<T> justOrEmpty(boolean valid, Mono<T> value) {
    if (valid) {
      return value;
    }
    return Mono.empty();
  }

  public static <T> Mono<T> just(boolean which , T right, T left) {
    return Mono.just(which ? right : left);
  }

}
