package io.github.cooperlyt.mis.work.camunda.delegate;

import io.github.cooperlyt.mis.work.camunda.mq.ProcessChangeService;

import io.github.cooperlyt.mis.work.message.WorkStatus;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class WorkRejectDelegate implements JavaDelegate, ExecutionListener {

  public WorkRejectDelegate() {
    BeanInjectionHelper.autowireBean(this);
  }

  @Autowired
  private ProcessChangeService processChangeService;

  @Override
  public void execute(DelegateExecution delegateExecution) throws Exception {
    processChangeService.statusChange(delegateExecution, WorkStatus.REJECT);

  }

  @Override
  public void notify(DelegateExecution delegateExecution) throws Exception {
    processChangeService.statusChange(delegateExecution, WorkStatus.REJECT);
  }
}
