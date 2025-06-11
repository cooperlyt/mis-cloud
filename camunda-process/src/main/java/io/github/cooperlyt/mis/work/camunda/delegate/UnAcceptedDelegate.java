package io.github.cooperlyt.mis.work.camunda.delegate;

import io.github.cooperlyt.mis.work.camunda.mq.ProcessChangeEventService;
import io.github.cooperlyt.mis.work.message.WorkStatus;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

public class UnAcceptedDelegate implements JavaDelegate, ExecutionListener {

  @Autowired
  private ProcessChangeEventService processChangeEventService;


  public UnAcceptedDelegate() {
    BeanInjectionHelper.autowireBean(this);
  }

  @Override
  public void notify(DelegateExecution execution) throws Exception {
    processChangeEventService.statusChange(execution, WorkStatus.UNACCEPTED);
  }

  @Override
  public void execute(DelegateExecution execution) throws Exception {
    processChangeEventService.statusChange(execution, WorkStatus.UNACCEPTED);
  }
}
