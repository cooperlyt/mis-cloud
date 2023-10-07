package io.github.cooperlyt.mis.work;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
public class TestMessageWorkImpl {


  public Mono<BigDecimal> test(String name, long workId) {
    return Mono.just(new BigDecimal("88")).doOnNext(id -> System.out.println("yea  yea  run me test " + id));
  }
}
