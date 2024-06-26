package io.github.cooperlyt.mis;



import io.github.cooperlyt.mis.work.WorkRemoteService;
import io.github.cooperlyt.mis.work.data.WorkDefine;
import io.github.cooperlyt.mis.work.data.WorkDefineForCreate;
import io.github.cooperlyt.mis.work.data.WorkDefineForProcess;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class MockWorkRemoteService implements WorkRemoteService {
  @Override
  public Mono<WorkDefineForCreate> prepareCreate(String defineId) {
    System.out.println("--------------> prepare create work " + defineId);
    var define = new WorkDefineForCreate();
    define.setWorkId(99L);
    define.setDefineId(defineId);
    define.setWorkName("test");
    define.setEnabled(true);
    define.setProcess(true);
    return Mono.just(define);
  }

  @Override
  public Mono<WorkDefineForCreate> recreate(String defineId, long originalWorkId) {
    return null;
  }

  @Override
  public Mono<WorkDefineForProcess> prepareProcess(String defineId) {
    return null;
  }

  @Override
  public Mono<WorkDefine> define(String defineId) {
    return Mono.empty();
  }


  @Override
  public Mono<Long> sendWorkMessage(String bindingName, String defineId, long workId, Map<String, Object> processData) {
    return Mono.just(workId)
        .doOnNext(id -> System.out.println("--------------> send work message " + id + " with binding :" + bindingName));
  }

  @Override
  public Mono<Long> sendWorkEventMessage(String bindingName, String defineId, long workId, Map<String, Object> processData, String messageName) {
    return null;
  }

  @Override
  public Mono<Long> applyWorkId() {
    return Mono.just(1l);
  }


}
