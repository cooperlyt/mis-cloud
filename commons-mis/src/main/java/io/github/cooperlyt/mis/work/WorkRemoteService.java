package io.github.cooperlyt.mis.work;


import io.github.cooperlyt.mis.work.data.WorkDefine;
import io.github.cooperlyt.mis.work.data.WorkDefineForCreate;
import io.github.cooperlyt.mis.work.data.WorkDefineForProcess;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface WorkRemoteService {


  Mono<WorkDefineForCreate> prepareCreate(String defineId);

  Mono<WorkDefineForCreate> recreate(String defineId, long originalWorkId);

  Mono<WorkDefineForProcess> prepareProcess(String defineId);

  Mono<WorkDefine> define(String defineId);

//  @Transactional
//  Mono<Long> runWork(String bindingName, String defineId, long orgId, long workId, Function<WorkDefine, Mono<Map<String,Object>>> dataProcess);
//
////  @Transactional
////  <T> Mono<T> createWork(String defineId, String bindingName, Function<WorkCreateProvide, Mono<T>> dataProcess);
//
//  @Transactional
//  <T> Mono<T> createWork(String bindingName, String defineId, long orgId,
//                         WorkCreateType type, Function<WorkDefineForCreate, Mono<T>> dataProcess);
//
//
//  Mono<Void> createOptionalWork(String bindingName, String defineId, long orgId,
//                                WorkCreateType type, Function<WorkDefineForCreate, Mono<Boolean>> dataProcess);
//
//
//  Mono<Long> sendWorkMessage(String bindingName, String defineId, long workId);


  Mono<Long> sendWorkMessage(String bindingName, String defineId,
                             long workId, Map<String,Object> processData);

  Mono<Long> applyWorkId();

}
