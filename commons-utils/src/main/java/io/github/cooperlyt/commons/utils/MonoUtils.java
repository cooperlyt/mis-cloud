package io.github.cooperlyt.commons.utils;

import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

public class MonoUtils {

  public static Mono<String> justOrHasText(String optional) {
    if (StringUtils.hasText(optional)) {
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
