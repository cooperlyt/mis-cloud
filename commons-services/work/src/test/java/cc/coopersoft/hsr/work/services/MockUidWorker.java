package cc.coopersoft.hsr.work.services;

import io.github.cooperlyt.cloud.uid.worker.WorkerIdAssigner;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Primary
@Component
public class MockUidWorker implements WorkerIdAssigner {


  @Override
  public Mono<Long> assignWorkerId() {
    return Mono.just(1L);
  }

  @Override
  public void releaseWorkerId(long id, long lastTime) {

  }
}
