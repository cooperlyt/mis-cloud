package io.github.cooperlyt.mis.work.camunda.delegate;

import io.github.cooperlyt.mis.work.camunda.mq.ProcessChangeService;
import io.github.cooperlyt.mis.work.message.WorkStatus;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class CompleteDelegate implements JavaDelegate, ExecutionListener {

  @Autowired
  private ProcessChangeService processChangeService;


  public CompleteDelegate() {
    BeanInjectionHelper.autowireBean(this);
  }

  @Override
  public void notify(DelegateExecution delegateExecution) throws Exception {
    processChangeService.statusChange(delegateExecution, WorkStatus.COMPLETED);
  }

  @Override
  public void execute(DelegateExecution delegateExecution) throws Exception {
    processChangeService.statusChange(delegateExecution, WorkStatus.COMPLETED);
  }
}
