package io.github.cooperlyt.mis;

import cc.coopersoft.common.cloud.work.WorkRemoteService;
import cc.coopersoft.construction.data.work.WorkDefine;
import cc.coopersoft.construction.data.work.WorkDefineForCreate;
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
  public Mono<WorkDefine> define(String defineId) {
    return Mono.empty();
  }


  @Override
  public Mono<Long> sendWorkMessage(String bindingName, String defineId, long workId, Map<String, Object> processData) {
    return Mono.just(workId)
        .doOnNext(id -> System.out.println("--------------> send work message " + id + " with binding :" + bindingName));
  }


}
