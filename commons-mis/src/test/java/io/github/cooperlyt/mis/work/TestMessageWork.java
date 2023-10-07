package io.github.cooperlyt.mis.work;

import io.github.cooperlyt.mis.work.invocation.MessageWork;
import io.github.cooperlyt.mis.work.invocation.WorkProvide;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@MessageWork(value = "test_stream_binding", transactionalRequired = false)
public interface TestMessageWork {

  @WorkProvide(value ="func.test", provider = TestMessageWorkImpl.class)
  Mono<BigDecimal> test(String name);

}
