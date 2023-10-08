package io.github.cooperlyt.mis.work.impl;

import io.github.cooperlyt.mis.work.create.WorkPrepareCreateHandler;
import io.github.cooperlyt.mis.work.data.*;
import io.github.cooperlyt.mis.work.impl.model.WorkModel;
import io.github.cooperlyt.mis.work.impl.model.WorkOperatorModel;
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

import static io.github.cooperlyt.mis.ErrorDefine.WORK_NOT_EXISTS;

public class WorkPersistableService implements WorkPrepareCreateHandler {

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

  public Mono<List<WorkAction>> workActions(long workId){
    return workOperatorRepository.workActions(workId).collectList()
        .map(list -> list.stream().sorted(Comparator.comparing(WorkOperator::getWorkTime).reversed()).collect(Collectors.toList()))
        .cache();
  }

  public Mono<WorkInfo> workInfo(long workId){
    return workRepository.findById(workId)
        .switchIfEmpty(Mono.error(WORK_NOT_EXISTS.exception()))
        .cast(WorkInfo.class)
        .cache();
  }

  @Override
  public Mono<Void> prepareCreate(WorkDefine define, long workId, String operatorId, String operatorName) {
    return workRepository.save(WorkModel.builder()
        .define(define).workId(workId)
        .status(define.isProcess() ? WorkStatus.RUNNING : WorkStatus.COMPLETED)
        .build())
        .flatMap(work -> workOperatorRepository.save(WorkOperatorModel.operatorBuilder()
            .id(String.valueOf(work.getWorkId()))
            .workId(work.getWorkId())
            .type(WorkOperatorDetails.OperatorType.CREATE)
            .empId(operatorId)
            .empName(operatorName)
            .build()))
        .then();
  }

  @Override
  public Mono<Void> prepareCreate(WorkDefine define, long workId, long orgId, long employeeId) {
    //TODO search corp and employee
    return Mono.empty();
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
        workOperatorRepository.save(WorkOperatorModel.operatorBuilder()
                .id(message.getTaskId())
                .workId(message.getWorkId())
                .type(WorkOperatorDetails.OperatorType.TASK)
                .empId(message.getEmpId())
                .empName(message.getEmpName())
                .build())
            .flatMap(operator -> workTaskRepository.save(WorkTaskModel.builder()
                .id(operator.getId())
                .message(message.getMessage())
                .taskName(message.getTaskName())
                .pass(message.isPass())
                .build())
            ).then();
  }
}
