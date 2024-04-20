package io.github.cooperlyt.mis.work.impl;

import io.github.cooperlyt.commons.data.PowerBody;
import io.github.cooperlyt.mis.work.create.WorkOperatorPersistableHandler;
import io.github.cooperlyt.mis.work.data.*;
import io.github.cooperlyt.mis.work.impl.model.WorkApplicantModel;
import io.github.cooperlyt.mis.work.impl.model.WorkModel;
import io.github.cooperlyt.mis.work.impl.model.WorkActionModel;
import io.github.cooperlyt.mis.work.impl.model.WorkTaskModel;
import io.github.cooperlyt.mis.work.impl.repositories.WorkApplicantRepository;
import io.github.cooperlyt.mis.work.impl.repositories.WorkOperatorRepository;
import io.github.cooperlyt.mis.work.impl.repositories.WorkTaskRepository;
import io.github.cooperlyt.mis.work.message.WorkChangeMessage;
import io.github.cooperlyt.mis.work.message.WorkStatus;
import io.github.cooperlyt.mis.work.impl.repositories.WorkRepository;
import org.springframework.messaging.Message;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.github.cooperlyt.mis.work.Constant.ErrorDefine.WORK_NOT_EXISTS;

public class WorkDao implements WorkOperatorPersistableHandler {

  private final WorkOperatorRepository workOperatorRepository;

  private final WorkTaskRepository workTaskRepository;

  private final WorkApplicantRepository workApplicantRepository;

  private final WorkRepository workRepository;

  public WorkDao(WorkOperatorRepository workOperatorRepository,
                 WorkTaskRepository workTaskRepository,
                 WorkApplicantRepository workApplicantRepository,
                 WorkRepository workRepository) {
    this.workOperatorRepository = workOperatorRepository;
    this.workTaskRepository = workTaskRepository;
    this.workApplicantRepository = workApplicantRepository;
    this.workRepository = workRepository;
  }

  public Mono<Boolean> workIsExists(long workId){
    return workRepository.existsById(workId);
  }

  public Mono<List<WorkTask>> workTasks(long workId){
    return workOperatorRepository.workActions(workId).collectList()
        .map(list -> list.stream().sorted(Comparator.comparing(WorkAction::getWorkTime).reversed()).collect(Collectors.toList()));
  }

  public Mono<List<WorkTask>> workRejectTasks(long workId){
    return workOperatorRepository.workRejectActions(workId).collectSortedList(Comparator.comparing(WorkAction::getWorkTime));
  }

  public Mono<WorkInfo> workInfo(long workId){
    return workRepository.findById(workId)
        .switchIfEmpty(Mono.error(WORK_NOT_EXISTS.exception()))
        .cast(WorkInfo.class);
  }

  public Mono<Long> createWorkApplicant(long workId, PowerBody applicant){
    return workApplicantRepository.save(new WorkApplicantModel(true,applicant,workId))
        .map(WorkApplicantModel::getWorkId);
  }

  public Mono<Long> updateWorkApplicant(long workId, PowerBody applicant){
    return workApplicantRepository.save(new WorkApplicantModel(false,applicant,workId))
        .map(WorkApplicantModel::getWorkId);
  }

  public Mono<PowerBody> getWorkApplicant(long workId){
    return workApplicantRepository.findById(workId).cast(PowerBody.class);

  }

  public Mono<WorkOperator> getWorkCreateOperator(long workId){
    return workOperatorRepository
        .findFirstByWorkIdAndTypeIn(workId, EnumSet.of(WorkAction.ActionType.CREATE,WorkAction.ActionType.APPLY))
        .cast(WorkOperator.class);
  }

  @Transactional
  public Mono<Void> workStatusChange(long workId, WorkStatus status){
    return workRepository.findById(workId)
        .switchIfEmpty(Mono.error(WORK_NOT_EXISTS.exception()))
        .filter(work -> work.getStatus() != status)
        .doOnNext(work -> {
          work.setStatus(status);
          if (status.valid){
            work.setValidateAt(LocalDateTime.now());
            if (WorkStatus.COMPLETED.equals(status)){
              work.setCompletedAt(LocalDateTime.now());
            }
          }
        })
        .flatMap(workRepository::save)
        .then();

//    return workRepository.updateWorkStatus(workId,status)
//        .then();
  }

  @Transactional
  public Mono<Void> workChange(WorkChangeMessage message){
    return workOperatorRepository.save(WorkActionModel.actionBuilder()
                .id(UUID.randomUUID().toString().replace("-","").toLowerCase())
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

  @Transactional
  public Mono<Long> prepareWork(WorkDefineForCreate work){
    return workRepository.save(WorkModel.builder()
        .define(work)
        .workId(work.getWorkId())
        .dataSource(WorkInfo.SOURCE_FROM_OFFICE)
        .status(WorkStatus.PREPARE)
        .build())
        .thenReturn(work.getWorkId());
  }

  private Mono<Long> createWork(WorkDefineForCreate work, WorkStatus status, String source){
    return workRepository.save(WorkModel.builder()
            .define(work)
            .workId(work.getWorkId())
            .dataSource(source)
            .status(status)
            .build())
        .map(WorkModel::getWorkId);
  }

  @Transactional
  public Mono<Long> createRunningWork(WorkDefineForCreate work, String source){
    return createWork(work,WorkStatus.RUNNING,source);
  }

  private Mono<WorkActionModel> saveCreateOperator(long workId, WorkOperator operator){
    return workOperatorRepository.save(WorkActionModel.operatorBuilder()
        .id(String.valueOf(workId))
        .workId(workId)
        .type(WorkAction.ActionType.CREATE)
        .operator(operator)
        .build());
  }

  public Mono<Long> createCompletedWork(WorkDefineForCreate work, WorkOperator operator){
    return createWork(work,WorkStatus.COMPLETED,WorkInfo.SOURCE_FROM_OFFICE)
        .flatMap(workId -> saveCreateOperator(workId,operator))
        .thenReturn(work.getWorkId());
  }

  @Transactional
  public Mono<Long> createRunningWork(WorkDefineForCreate work, String source, WorkOperator operator){
    return createRunningWork(work,source)
        .flatMap(workId -> saveCreateOperator(workId,operator))
        .thenReturn(work.getWorkId());
  }

  @Transactional
  public Mono<Void> runWork(long workId, WorkOperator operator){
    return workRepository.updateWorkStatus(workId,WorkStatus.RUNNING)
        .then(saveCreateOperator(workId,operator))
        .then();
  }

  @Transactional
  public Mono<Void> cloneApplicant(long sourceWorkId, long newWorkId){
    return workApplicantRepository.cloneApplicant(newWorkId,sourceWorkId);

//        .findById(sourceWorkId)
//        .map(applicant -> new WorkApplicantModel(true,applicant,newWorkId))
//        .flatMap(workApplicantRepository::save)
//        .then();
  }

  @Transactional
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
