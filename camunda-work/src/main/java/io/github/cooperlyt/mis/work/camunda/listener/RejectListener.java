package io.github.cooperlyt.mis.work.camunda.listener;

import io.github.cooperlyt.mis.work.camunda.delegate.BeanInjectionHelper;
import io.github.cooperlyt.mis.work.camunda.mq.ProcessChangeService;
import io.github.cooperlyt.mis.work.message.WorkStatus;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;

@Deprecated
@Slf4j
public class RejectListener implements ExecutionListener {

  @Autowired
  private ProcessChangeService processChangeService;

  public RejectListener() {
    BeanInjectionHelper.autowireBean(this);
  }

  @Override
  public void notify(DelegateExecution execution) throws Exception {
    log.info("reject listener process: {}", execution.getProcessBusinessKey());

    processChangeService.statusChange(Long.parseLong(execution.getProcessBusinessKey()),
        WorkStatus.REJECT, execution.getProcessDefinitionId());
  }
}
