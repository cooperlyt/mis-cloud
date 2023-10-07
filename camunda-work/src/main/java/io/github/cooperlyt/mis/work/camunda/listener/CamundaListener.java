package io.github.cooperlyt.mis.work.camunda.listener;

import io.github.cooperlyt.mis.work.camunda.mq.ProcessChangeService;
import io.github.cooperlyt.mis.work.message.WorkChangeMessage;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.history.event.HistoryEvent;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperties;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperty;
import org.camunda.bpm.spring.boot.starter.event.ExecutionEvent;
import org.camunda.bpm.spring.boot.starter.event.TaskEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;


@Slf4j
@Component
public class CamundaListener {

  private final TaskService taskService;

  private final ProcessChangeService processChangeService;

  private final RepositoryService repositoryService;

  private final IdentityService identityService;

  public CamundaListener(TaskService taskService,
                         ProcessChangeService processChangeService,
                         RepositoryService repositoryService, IdentityService identityService) {
    this.taskService = taskService;
    this.processChangeService = processChangeService;
    this.repositoryService = repositoryService;
    this.identityService = identityService;
  }

  private Collection<CamundaProperty> getCamundaProperties(String processDefinitionId, String taskDefinitionKey){

    BaseElement element = repositoryService.getBpmnModelInstance(processDefinitionId).getModelElementById(taskDefinitionKey);
    return element.getExtensionElements().getElementsQuery().filterByType(CamundaProperties.class).singleResult().getCamundaProperties();
  }

  @EventListener
  public void onTaskEvent(DelegateTask taskDelegate) {
    log.debug("mutable task event by taskDelegate: {}", taskDelegate.getEventName());
    // handle mutable task event
  }

  @EventListener(condition="#taskEvent.eventName=='complete'")
  public void onTaskEvent(TaskEvent taskEvent) {
    log.debug("immutable task event: {} by TaskEvent", taskEvent.getEventName());


    Task task = taskService.createTaskQuery().taskId(taskEvent.getId()).initializeFormKeys().singleResult();

    User user = identityService.createUserQuery().userId(taskEvent.getAssignee()).singleResult();

    boolean pass = Optional.ofNullable(taskService.getVariable(taskEvent.getId(),"approval"))
        .map(approval -> (Boolean)approval)
        .orElse(true);

    try {
      processChangeService.processChange(WorkChangeMessage.builder()
              .message((String) taskService.getVariable(taskEvent.getId(),"message"))
              .pass(pass)
              .workId(Long.parseLong(taskEvent.getCaseInstanceId()))
              .empId(taskEvent.getAssignee())
              .empName(user.getFirstName() + user.getLastName())
              .taskName(task.getName())
              .taskId(taskEvent.getId())
              .build(),
          taskEvent.getProcessDefinitionId());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }


//    getCamundaProperties(taskEvent.getProcessDefinitionId(),taskEvent.getTaskDefinitionKey())
//        .stream().filter(p -> p.getCamundaName().equals("type")).findFirst()
//        .ifPresent(type -> {
//
//        });
  }

  @EventListener
  public void onExecutionEvent(DelegateExecution executionDelegate) {
    log.debug("immutable executionDelegate event: {}", executionDelegate.getEventName());
    // handle mutable execution event
  }

  @EventListener
  public void onExecutionEvent(ExecutionEvent executionEvent) {
    log.debug("immutable execution event: {}", executionEvent.getEventName());
    // handle immutable execution event
  }

  @EventListener
  public void onHistoryEvent(HistoryEvent historyEvent) {
    log.debug("history event");
    // handle history event
  }

}
