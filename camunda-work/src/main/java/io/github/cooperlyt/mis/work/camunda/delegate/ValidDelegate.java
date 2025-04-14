package io.github.cooperlyt.mis.work.camunda.delegate;

import io.github.cooperlyt.mis.work.camunda.mq.ProcessChangeEventService;
import io.github.cooperlyt.mis.work.message.WorkStatus;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class ValidDelegate implements JavaDelegate, ExecutionListener {

  @Autowired
  private ProcessChangeEventService processChangeEventService;

  public ValidDelegate() {
    BeanInjectionHelper.autowireBean(this);
  }

  @Override
  public void execute(DelegateExecution delegateExecution) throws Exception {
    log.info("work valid define: {}", delegateExecution.getProcessBusinessKey());
    processChangeEventService.statusChange(delegateExecution, WorkStatus.VALID);
  }

  @Override
  public void notify(DelegateExecution delegateExecution) throws Exception {
    processChangeEventService.statusChange(delegateExecution, WorkStatus.VALID);
  }
}
