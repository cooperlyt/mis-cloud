package io.github.cooperlyt.mis.service.work.services;


import io.github.cooperlyt.mis.work.message.WorkChangeMessage;
import io.github.cooperlyt.mis.work.message.WorkCreateMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Configuration
public class WorkChangeListener {

  private final WorkService workService;

  public WorkChangeListener(WorkService workService) {
    this.workService = workService;
  }



  @Bean
  public Function<Flux<Message<WorkCreateMessage>>, Mono<Void>> workCreateChannel(){
    return flux -> flux.flatMap(workService::workCreated).then();
  }



  @Bean
  public Function<Flux<Message<WorkChangeMessage>>,Mono<Void>> workChangeChannel(){
    return flux -> flux.flatMap(workService::workTaskChange).then();
  }
}
