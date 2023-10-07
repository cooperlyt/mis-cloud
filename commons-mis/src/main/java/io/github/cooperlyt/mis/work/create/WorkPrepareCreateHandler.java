package io.github.cooperlyt.mis.work.create;

import io.github.cooperlyt.mis.work.data.WorkDefine;
import reactor.core.publisher.Mono;

//@FunctionalInterface
public interface WorkPrepareCreateHandler {


  Mono<Void> prepareCreate(WorkDefine define, long workId, String operatorId, String operatorName);

  Mono<Void> prepareCreate(WorkDefine define, long workId, long orgId, long employeeId);

}
