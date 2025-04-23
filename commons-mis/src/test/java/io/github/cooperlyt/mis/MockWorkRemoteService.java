package io.github.cooperlyt.mis;



import io.github.cooperlyt.mis.work.WorkRemoteService;
import io.github.cooperlyt.mis.work.data.WorkDefine;
import io.github.cooperlyt.mis.work.data.WorkDefineForCreate;
import io.github.cooperlyt.mis.work.data.WorkDefineForProcess;
import io.github.cooperlyt.mis.work.message.ProcessDocumentation;
import io.github.cooperlyt.mis.work.message.WorkCreateMessage;
import io.github.cooperlyt.mis.work.message.WorkEventMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.Supplier;

@Service
public class MockWorkRemoteService implements WorkRemoteService {
  @Override
  public Mono<WorkDefineForCreate> prepareCreate(String defineId) {
    System.out.println("--------------> prepare create work " + defineId);
    var define = new WorkDefineForCreate();
    define.setWorkId(99L);
    define.setDefineId(defineId);
    define.setWorkName("test");
    define.setEnabled(true);
    define.setProcess(true);
    return Mono.just(define);
  }

  @Override
  public Mono<WorkDefineForCreate> recreate(String defineId, long originalWorkId) {
    return null;
  }

  @Override
  public Mono<WorkDefineForProcess> prepareProcess(String defineId) {
    return null;
  }

  @Override
  public Mono<WorkDefine> define(String defineId) {
    return Mono.empty();
  }


  @Override
  public Mono<Long> applyWorkId() {
    return Mono.just(1l);
  }


  @NotNull
  @Override
  public Mono<Long> sendWorkEventMessage(@NotNull String messageName, @NotNull String defineId, long workId, @NotNull Map<String, ?> processData) {
    return Mono.just(workId)
        .doOnNext(id -> System.out.println("--------------> send work event message " + id + " with define :" + defineId));
  }

  @NotNull
  @Override
  public Supplier<Flux<Message<WorkCreateMessage>>> workCreateMessageSinks() {
    return null;
  }

  @NotNull
  @Override
  public Supplier<Flux<Message<WorkEventMessage>>> workEventMessageSinks() {
    return null;
  }

  @Override
  public @NotNull Mono<@NotNull Long> sendWorkCreateMessage(@NotNull String defineId, long workId, @NotNull Map<@NotNull String, ?> processData, @Nullable ProcessDocumentation documentation) {
    return Mono.just(workId)
        .doOnNext(id -> System.out.println("--------------> send work create message " + id + " with define :" + defineId));
  }
}
