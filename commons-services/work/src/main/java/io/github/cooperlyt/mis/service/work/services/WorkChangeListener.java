package io.github.cooperlyt.mis.service.work.services;


import io.github.cooperlyt.mis.work.message.StatusChangeMessage;
import io.github.cooperlyt.mis.work.message.WorkRecreateMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Slf4j
@Configuration
public class WorkChangeListener {

  private final WorkService workService;

  public WorkChangeListener(WorkService workService) {
    this.workService = workService;
  }



//  @Bean
//  public Function<Flux<Message<WorkCreateMessage>>, Mono<Void>> workCreateChannel(){
//    return Flux::then;
//  }
//
//
//
//  @Bean
//  public Function<Flux<Message<WorkChangeMessage>>,Mono<Void>> workChangeChannel(){
//    return Flux::then;
//  }

//  @Bean
//  public Function<Flux<Message<StatusChangeMessage>>,Mono<Void>> workStatusChannel(){
//    return flux -> flux
//        .map(Message::getPayload)
//        .doOnNext(msg -> log.info("work file clone: {}",msg))
//        .flatMap(msg -> workService.cloneWorkFile(msg.getOriginalWorkId(),msg.getTargetWorkId()))
//        .then();
//  }
}
