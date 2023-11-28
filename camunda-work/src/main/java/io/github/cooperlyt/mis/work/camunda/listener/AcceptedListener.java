package io.github.cooperlyt.mis.work.camunda.listener;

import io.github.cooperlyt.mis.work.camunda.delegate.BeanInjectionHelper;
import io.github.cooperlyt.mis.work.camunda.mq.ProcessChangeService;
import io.github.cooperlyt.mis.work.message.WorkStatus;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class AcceptedListener implements ExecutionListener {

  @Autowired
  private ProcessChangeService processChangeService;

  public AcceptedListener() {
    BeanInjectionHelper.autowireBean(this);
  }
  @Override
  public void notify(DelegateExecution delegateExecution) throws Exception {

    log.info("complete listener process: {}", delegateExecution.getProcessBusinessKey());

    processChangeService.statusChange(Long.parseLong(delegateExecution.getProcessBusinessKey()),
        WorkStatus.ACCEPTED, delegateExecution.getProcessDefinitionId());
  }
}
