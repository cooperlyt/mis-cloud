package io.github.cooperlyt.mis.work.camunda.mq;

import io.github.cooperlyt.mis.work.camunda.Constants;
import io.github.cooperlyt.mis.work.message.StatusChangeMessage;
import io.github.cooperlyt.mis.work.message.WorkChangeMessage;
import io.github.cooperlyt.mis.work.message.WorkStatus;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProcessChangeService {

  private final StreamBridge streamBridge;

  private final RepositoryService repositoryService;


  public ProcessChangeService(StreamBridge streamBridge, RepositoryService repositoryService) {
    this.streamBridge = streamBridge;
    this.repositoryService = repositoryService;
  }

  public void statusChange(DelegateExecution delegateExecution, WorkStatus status) throws Exception {
    statusChange(Long.parseLong(delegateExecution.getProcessBusinessKey()),status,delegateExecution.getProcessDefinitionId());
  }

  public void statusChange(Long workId, WorkStatus status, String processDefinitionId) throws Exception {
    var msg = MessageBuilder.withPayload(StatusChangeMessage.builder().status(status).workId(workId).build())
        .setHeader(Constants.DEFINE_KEY_NAME,getDefineId(processDefinitionId))
        .setHeader(Constants.WORK_STATUS_KEY_NAME,status)
        .build();
    if (!streamBridge.send("statusChangeChannel-out-0",msg)){
      throw new Exception("message send fail!");
    }
    log.info("status change mq is send: {} -> {}",workId,status);
  }

  public void processChange(WorkChangeMessage changeMessage, String processDefinitionId) throws Exception {
    var msg = MessageBuilder.withPayload(changeMessage)
        .setHeader(Constants.DEFINE_KEY_NAME,getDefineId(processDefinitionId))
        .build();
    if (!streamBridge.send("processChangeChannel-out-0",msg)){
      throw new Exception("message send fail!");
    }
    log.info("process change  mq is send : {}",changeMessage);
  }

  private String getDefineId(String processDefinitionId){
    ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
        .processDefinitionId(processDefinitionId)
        .singleResult();
    log.info("process define id: {}, key: {}",definition.getId(),definition.getKey());
    return definition.getKey();
  }

}
