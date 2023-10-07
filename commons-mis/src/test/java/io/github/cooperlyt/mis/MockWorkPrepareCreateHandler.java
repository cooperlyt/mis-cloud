package io.github.cooperlyt.mis;

import cc.coopersoft.common.cloud.work.WorkPrepareCreateHandler;
import cc.coopersoft.construction.data.work.WorkDefine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class MockWorkPrepareCreateHandler implements WorkPrepareCreateHandler {
  @Override
  public Mono<Void> prepareCreate(WorkDefine define,long workId, String operatorId, String operatorName) {
    System.out.println("1===================================> prepareCreate");
    return Mono.empty();
  }

  @Override
  public Mono<Void> prepareCreate(WorkDefine define,long workId, long orgId, long employeeId) {
    System.out.println("2===================================> prepareCreate");
    return Mono.empty();
  }
}
