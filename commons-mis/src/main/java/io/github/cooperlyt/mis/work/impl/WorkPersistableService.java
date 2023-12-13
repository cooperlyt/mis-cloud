package io.github.cooperlyt.mis.work.impl;

import io.github.cooperlyt.mis.work.create.WorkOperatorPersistableHandler;
import io.github.cooperlyt.mis.work.data.*;
import io.github.cooperlyt.mis.work.impl.model.WorkModel;
import io.github.cooperlyt.mis.work.impl.model.WorkActionModel;
import io.github.cooperlyt.mis.work.impl.model.WorkTaskModel;
import io.github.cooperlyt.mis.work.impl.repositories.WorkOperatorRepository;
import io.github.cooperlyt.mis.work.impl.repositories.WorkTaskRepository;
import io.github.cooperlyt.mis.work.message.WorkChangeMessage;
import io.github.cooperlyt.mis.work.message.WorkStatus;
import io.github.cooperlyt.mis.work.impl.repositories.WorkRepository;
import org.springframework.messaging.Message;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.cooperlyt.mis.work.Constant.ErrorDefine.WORK_NOT_EXISTS;

public class WorkPersistableService implements WorkOperatorPersistableHandler {

  private final WorkOperatorRepository workOperatorRepository;

  private final WorkTaskRepository workTaskRepository;

  private final WorkRepository workRepository;

  public WorkPersistableService(WorkOperatorRepository workOperatorRepository,
                                WorkTaskRepository workTaskRepository,
                                WorkRepository workRepository) {
    this.workOperatorRepository = workOperatorRepository;
    this.workTaskRepository = workTaskRepository;
    this.workRepository = workRepository;
  }

  public Mono<List<WorkTask>> workTasks(long workId){
    return workOperatorRepository.workActions(workId).collectList()
        .map(list -> list.stream().sorted(Comparator.comparing(WorkAction::getWorkTime).reversed()).collect(Collectors.toList()))
        .cache();
  }

  public Mono<WorkInfo> workInfo(long workId){
    return workRepository.findById(workId)
        .switchIfEmpty(Mono.error(WORK_NOT_EXISTS.exception()))
        .cast(WorkInfo.class)
        .cache();
  }

  @Transactional
  public Mono<Void> workStatusChange(long workId, WorkStatus status){
    return workRepository.findById(workId)
        .switchIfEmpty(Mono.error(WORK_NOT_EXISTS.exception()))
        .map(work -> work.updateStatus(status))
        .flatMap(workRepository::save)
        .then();
  }

  @Transactional
  public Mono<Void> workChange(Message<WorkChangeMessage> msg){
    WorkChangeMessage message = msg.getPayload();
    return
        workOperatorRepository.save(WorkActionModel.actionBuilder()
                .id(message.getTaskId())
                .workId(message.getWorkId())
                .type(WorkAction.ActionType.TASK)
                .userId(message.getUserId())
                .userName(message.getUserName())
                .build())
            .flatMap(operator -> workTaskRepository.save(WorkTaskModel.builder()
                .id(operator.getId())
                .message(message.getMessage())
                .taskName(message.getTaskName())
                .pass(message.isPass())
                .build())
            ).then();
  }

  @Override
  public Mono<Void> persist(WorkDefine define, long workId,
                            WorkAction.ActionType type, WorkOperator operator,String dataSource) {
    return workRepository.save(WorkModel.builder()
            .define(define).workId(workId).dataSource(dataSource)
            .status(define.isProcess() ? WorkStatus.RUNNING : WorkStatus.COMPLETED)
            .build())
        .flatMap(work -> workOperatorRepository.save(WorkActionModel.operatorBuilder()
            .id(String.valueOf(work.getWorkId()))
            .workId(work.getWorkId())
            .type(type)
            .operator(operator)
            .build()))
        .then();
  }
}
