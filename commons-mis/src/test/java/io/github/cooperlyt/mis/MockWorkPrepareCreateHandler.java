package io.github.cooperlyt.mis;

import io.github.cooperlyt.mis.work.create.WorkOperatorPersistableHandler;
import io.github.cooperlyt.mis.work.data.WorkAction;
import io.github.cooperlyt.mis.work.data.WorkDefine;
import io.github.cooperlyt.mis.work.data.WorkOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class MockWorkPrepareCreateHandler implements WorkOperatorPersistableHandler {

  @Override
  public Mono<Void> persist(WorkDefine define, long workId, WorkAction.ActionType type, WorkOperator operator, String dataSource) {
    System.out.println("2===================================> prepareCreate");
    return Mono.empty();
  }
}
