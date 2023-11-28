package io.github.cooperlyt.mis.service.work.services;

import io.github.cooperlyt.mis.service.work.Application;
import io.github.cooperlyt.mis.service.work.model.WorkAttachmentModel;
import io.github.cooperlyt.mis.service.work.model.WorkFileModel;
import io.github.cooperlyt.cloud.uid.UidGenerator;
import io.github.cooperlyt.mis.service.work.repositories.WorkAttachmentDefineRepository;
import io.github.cooperlyt.mis.service.work.repositories.WorkAttachmentRepository;
import io.github.cooperlyt.mis.service.work.repositories.WorkDefineRepository;
import io.github.cooperlyt.mis.service.work.repositories.WorkFileRepository;
import io.github.cooperlyt.mis.work.data.*;
import io.github.cooperlyt.mis.work.message.WorkChangeMessage;
import io.github.cooperlyt.mis.work.message.WorkCreateMessage;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;

import static io.github.cooperlyt.mis.service.work.Application.ErrorDefine.WORK_NOT_EXISTS;

@Slf4j
@Service
public class WorkService {

  private final WorkDefineRepository workDefineRepository;

  private final WorkAttachmentDefineRepository workAttachmentDefineRepository;

  private final WorkAttachmentRepository workAttachmentRepository;

  private final WorkFileRepository workFileRepository;

  @Resource
  private UidGenerator defaultUidGenerator;

  public WorkService(WorkDefineRepository workDefineRepository,
                     WorkAttachmentDefineRepository workAttachmentDefineRepository,
                     WorkAttachmentRepository workAttachmentRepository,
                     WorkFileRepository workFileRepository) {
    this.workDefineRepository = workDefineRepository;
    this.workAttachmentDefineRepository = workAttachmentDefineRepository;
    this.workAttachmentRepository = workAttachmentRepository;
    this.workFileRepository = workFileRepository;
  }


//  public Mono<Work> getWork(long workId){
//    return workRepository.findById(workId)
//        .map(Work::new)
//        .flatMap(work -> workItemRepository.findAllByWorkId(work.getWorkId())
//            .collectList()
//            .map(work::setWorkItems)
//        )
//        .switchIfEmpty(Mono.error(WORK_NOT_EXISTS.exception()));
//  }

  public Mono<WorkDefine> workDefine(String defineId){
    return workDefineRepository.findById(defineId).map(WorkDefine.class::cast);
  }

  public Mono<WorkDefineForCreate> prepareCreate(String defineId){
    log.info("prepare work:" + defineId);
    return workDefineRepository.findById(defineId)
        .map(WorkDefineForCreate::new)
        .flatMap(workDefineForCreate -> defaultUidGenerator.getUID().map(workDefineForCreate::setPrepareWorkId));
  }

  public Mono<WorkDefineForProcess> prepareProcess(String defineId){
    log.info("prepare work for process:" + defineId);
    return defaultUidGenerator.getUID().flatMap(workId ->
            workAttachmentDefineRepository.findAllByDefineId(defineId)
                .flatMap(define -> defaultUidGenerator.getUID().map(uid -> new WorkAttachmentModel(uid,workId,define)))
                .collectList()
                .flatMap(attaches -> workAttachmentRepository.saveAll(attaches).cast(WorkAttachmentInfo.class).collectList())
                .flatMap(attaches -> Mono.just(WorkDefineForProcess.builder().attachments(attaches).workId(workId))
                    .flatMap(builder -> workDefineRepository.findById(defineId).map(builder::workDefine))
                    .map(WorkDefineForProcess.WorkDefineForProcessBuilder::build)
                )
        );
  }

  public Mono<Void> addWorkFile(long attachId, WorkFileImpl file){
    return Mono.just(new WorkFileModel(attachId,file))
        .flatMap(workFileRepository::save)
        .then();
  }

  @Transactional
  public Mono<Void> workCreated(Message<WorkCreateMessage> msg){
    WorkCreateMessage message = msg.getPayload();
    return workFileRepository.updateFileWorkItem(String.valueOf(message.getWorkId()),message.getWorkId())
        .then();
  }


  @Transactional
  public Mono<Void> workTaskChange(Message<WorkChangeMessage> msg){
    WorkChangeMessage message = msg.getPayload();
    return workFileRepository.updateFileWorkItem(message.getTaskId(),message.getWorkId())
        .then();
  }

  @Transactional
  public Mono<Void> removeWorkFile(String fileId){
    return workFileRepository.deleteByFid(fileId);
  }

  @Transactional
  public Mono<List<WorkFileInfo>> workFiles(long attachId){
    return workFileRepository.findAllByAttachId(attachId)
        .map(WorkFileInfo.class::cast)
        .collectList();
  }

  public Mono<List<WorkAttachment>> workAttachments(long workId){
    return workAttachmentRepository.findWorkAttachment(workId)
        .collectList();
  }

  @Transactional
  public Mono<Long> addWorkAttachment(long workId, String name){
    return defaultUidGenerator.getUID()
        .flatMap(uid -> workAttachmentRepository.save(new WorkAttachmentModel(uid,workId,name)))
        .map(WorkAttachmentModel::getId);
  }

  public Mono<Void> removeWorkAttachment(long attachId){
    return workFileRepository.deleteAllByAttachId(attachId)
        .then(workAttachmentRepository.deleteById(attachId));
  }

  public Mono<Void> renameWorkAttachment(long attachId, String name){
    return workAttachmentRepository.findById(attachId)
        .filter(attachment -> !attachment.isMust())
        .switchIfEmpty(Mono.error(Application.ErrorDefine.MUST_ATTACH_CANT_MODIFY.exception()))
        .map(attachment -> attachment.updateName(name))
        .flatMap(workAttachmentRepository::save)
        .then();
  }

  public Mono<List<WorkDefine>> defineByType(String type){
    return workDefineRepository.findAllByType(type)
        .map(WorkDefine.class::cast)
        .collectList();
  }
}
