package io.github.cooperlyt.mis.work.camunda.delegate;

import io.github.cooperlyt.mis.work.camunda.mq.ProcessChangeEventService;
import io.github.cooperlyt.mis.work.message.WorkStatus;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 即将生效
 *
 * 用于预售许可证公示期， 此状态业务并不生效， 公示后生效
 */
@Slf4j
public class ImminentDelegate implements JavaDelegate, ExecutionListener {

  @Autowired
  private ProcessChangeEventService processChangeEventService;

  public ImminentDelegate() {
    BeanInjectionHelper.autowireBean(this);
  }

  @Override
  public void execute(DelegateExecution delegateExecution) throws Exception {
    processChangeEventService.statusChange(delegateExecution, WorkStatus.IMMINENT);
  }

  @Override
  public void notify(DelegateExecution delegateExecution) throws Exception {
    processChangeEventService.statusChange(delegateExecution, WorkStatus.IMMINENT);
  }
}
