package io.github.cooperlyt.mis.work;


import io.github.cooperlyt.mis.work.invocation.MessageWork;
import reactor.core.publisher.Mono;

@MessageWork(binding = "test")
public interface TestMessageWork2 {

  Mono<String> test(String name);
}
