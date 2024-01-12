package io.github.cooperlyt.mis.work.create;

import io.github.cooperlyt.mis.work.data.WorkAction;
import io.github.cooperlyt.mis.work.data.WorkDefine;
import io.github.cooperlyt.mis.work.data.WorkOperator;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface WorkOperatorPersistableHandler {


  Mono<Void> persist(WorkDefine define, long workId, WorkAction.ActionType type , WorkOperator operator,String dataSource);


}
