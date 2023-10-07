package io.github.cooperlyt.mis;

import io.github.cooperlyt.mis.work.create.WorkPrepareCreateHandler;
import io.github.cooperlyt.mis.work.data.WorkDefine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class MockWorkPrepareCreateHandler implements WorkPrepareCreateHandler {
  @Override
  public Mono<Void> prepareCreate(WorkDefine define, long workId, String operatorId, String operatorName) {
    System.out.println("1===================================> prepareCreate");
    return Mono.empty();
  }

  @Override
  public Mono<Void> prepareCreate(WorkDefine define,long workId, long orgId, long employeeId) {
    System.out.println("2===================================> prepareCreate");
    return Mono.empty();
  }
}
