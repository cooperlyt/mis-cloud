package io.github.cooperlyt.mis.work.camunda.delegate;

import io.github.cooperlyt.mis.work.camunda.mq.ProcessChangeService;

import io.github.cooperlyt.mis.work.message.WorkStatus;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class WorkRejectDelegate implements JavaDelegate {

  public WorkRejectDelegate() {
    BeanInjectionHelper.autowireBean(this);
  }

  @Autowired
  private ProcessChangeService processChangeService;

  @Override
  public void execute(DelegateExecution delegateExecution) throws Exception {
    log.info("work reject define: {}", delegateExecution.getProcessBusinessKey());
    processChangeService.statusChange(Long.parseLong(delegateExecution.getProcessBusinessKey()),
        WorkStatus.REJECT, delegateExecution.getProcessDefinitionId());

  }
}
