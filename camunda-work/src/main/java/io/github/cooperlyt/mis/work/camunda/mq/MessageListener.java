package io.github.cooperlyt.mis.work.camunda.mq;

import io.github.cooperlyt.mis.work.camunda.Constants;
import io.github.cooperlyt.mis.work.message.WorkCreateMessage;
import io.github.cooperlyt.mis.work.message.WorkMessage;
import io.github.cooperlyt.mis.work.message.WorkSignal;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.Map;
import java.util.function.Consumer;

@Slf4j
@Configuration
public class MessageListener {

  private final RuntimeService runtimeService;

  public MessageListener(RuntimeService runtimeService) {
    this.runtimeService = runtimeService;
  }

//  process_project_license
  @Bean
  public Consumer<Message<WorkCreateMessage>> processCreateChannel() {
    return msg -> {
      var arg = msg.getHeaders();
      log.info(Thread.currentThread().getName() + " Receive New Create Messages: " + msg.getPayload()+ " ARG:"
          + arg);
      String define = msg.getHeaders().get(WorkMessage.MESSAGE_HEADER_WORK_DEFINE, String.class);
      String workId = String.valueOf(msg.getPayload().getWorkId());

//      approval
      runtimeService.startProcessInstanceByKey(define,workId,workId,msg.getPayload().getData());

    };
  }


  @Bean
  public Consumer<Message<Map<String,Object>>> signalEventChannel() {
    return msg -> {
      var arg = msg.getHeaders();
      log.info(Thread.currentThread().getName() + " Receive New Signal Messages: " + msg.getPayload() + " ARG:"
          + arg);

      WorkSignal signal = arg.get(Constants.SIGNAL_KEY_NAME, WorkSignal.class);
      if (signal != null) {
        Map<String,Object> vars = msg.getPayload();
        if(vars.isEmpty()){
          runtimeService.signalEventReceived(signal.getRef());
        }else{
          runtimeService.signalEventReceived(signal.getRef(),vars);
        }
      }
    };
  }


}
