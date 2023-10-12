package io.github.cooperlyt.mis.work.impl;

import io.github.cooperlyt.commons.cloud.keycloak.auth.ReactiveKeycloakSecurityContextHolder;
import io.github.cooperlyt.mis.ErrorDefine;
import io.github.cooperlyt.mis.RemoteResponseService;
import io.github.cooperlyt.mis.work.WorkRemoteService;
import io.github.cooperlyt.mis.work.data.WorkDefine;
import io.github.cooperlyt.mis.work.data.WorkDefineForCreate;
import io.github.cooperlyt.mis.work.message.WorkCreateMessage;
import io.github.cooperlyt.mis.work.message.WorkCreateType;
import io.github.cooperlyt.mis.work.message.WorkMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.MediaType;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
public class WorkRemoteServiceImpl extends RemoteResponseService implements WorkRemoteService {

  private final WebClient webClient;

  @Value("${mis.internal.work.serverName}")
  private String serverName;

  private final StreamBridge streamBridge;


  public WorkRemoteServiceImpl(WebClient webClient, StreamBridge streamBridge) {
    this.webClient = webClient;
    this.streamBridge = streamBridge;
  }

  @Override
  public Mono<WorkDefineForCreate> prepareCreate(String defineId) {
    log.debug("prepare create work {}" , defineId);
    return webClient
        .get()
        .uri("http://" + serverName + "/internal/create/{defineId}", defineId)
        .accept(MediaType.APPLICATION_JSON)
        .exchangeToMono(response -> sourceResponse(WorkDefineForCreate.class, response));
  }

  @Override
  public Mono<WorkDefine> define(String defineId) {
    return webClient
        .get()
        .uri("http://" + serverName + "/internal/define/{defineId}", defineId)
        .accept(MediaType.APPLICATION_JSON)
        .exchangeToMono(response -> sourceResponse(WorkDefine.class, response));
  }

//  @Override
//  @Transactional
//  public Mono<Long> runWork(String bindingName, String defineId, long orgId, long workId, Function<WorkDefine, Mono<Map<String,Object>>> dataProcess) {
//    return defaultUidGenerator.getUID().flatMap(id ->
//        define(defineId)
//            .flatMap(workDefine -> workDefine.isEnabled() ? Mono.just(workDefine) : Mono.error(ErrorDefine.BUSINESS_IS_DISABLED.exception()))
//            .flatMap(workDefine -> dataProcess.apply(workDefine)
//                .flatMap(result -> ReactiveKeycloakSecurityContextHolder.getContext()
//                    .map(context ->
//                        sendMessage(bindingName, defineId, WorkCreateType.RUNNING, String.valueOf(workId) ,
//                            WorkCreateMessage.builder()
//                                .workId(workId)
//                                .empId(context.getUserInfo().getId())
//                                .empName(context.getUserInfo().getName())
//                                .orgId(orgId)
//                                .data(result)
//                                .build())
//                    )
//                    .filter(ifSend -> ifSend)
//                    .map(ifSend -> workId)
//                    .switchIfEmpty(Mono.error(ErrorDefine.MESSAGE_SEND_FAIL.exception()))
//                )
//            )
//    );
//  }
//
//
//  @Override
//  public Mono<Void> createOptionalWork(String bindingName, String defineId, long orgId, WorkCreateType type, Function<WorkDefineForCreate, Mono<Boolean>> dataProcess) {
//    return defaultUidGenerator.getUID().flatMap(id ->
//        prepareCreate(defineId)
//            .flatMap(workDefine -> workDefine.isEnabled() ? Mono.just(workDefine) : Mono.error(ErrorDefine.BUSINESS_IS_DISABLED.exception()))
//            .flatMap(workDefine -> dataProcess.apply(workDefine)
//                .filter(ifCreate -> ifCreate)
//                .flatMap(ifCreate -> sendMessage(bindingName, defineId, orgId, type, workDefine))
//            )
//    );
//  }
//
//  @Override
//  @Transactional
//  public <T> Mono<T> createWork(String bindingName, String defineId, long orgId,
//                                WorkCreateType type, Function<WorkDefineForCreate, Mono<T>> dataProcess) {
//    return defaultUidGenerator.getUID().flatMap(id ->
//            prepareCreate(defineId)
//            .flatMap(workDefine -> workDefine.isEnabled() ? Mono.just(workDefine) : Mono.error(ErrorDefine.BUSINESS_IS_DISABLED.exception()))
//            .flatMap(workDefine -> dataProcess.apply(workDefine)
//                .flatMap(result -> sendMessage(bindingName, defineId, orgId, type, workDefine).thenReturn(result))
//                .switchIfEmpty(sendMessage(bindingName, defineId, orgId, type, workDefine).then(Mono.empty()))
//            )
//        );
//  }
//
//  private Mono<Void> sendMessage(String bindingName, String defineId, long orgId, WorkCreateType type, WorkDefineForCreate workDefine) {
//    return ReactiveKeycloakSecurityContextHolder.getContext()
//        .map(context ->
//            sendMessage(bindingName, defineId, type, String.valueOf(workDefine.getWorkId()),
//                WorkMessage.builder()
//                    .workId(workDefine.getWorkId())
//                    .empId(context.getUserInfo().getId())
//                    .empName(context.getUserInfo().getName())
//                    .orgId(orgId)
//                    .build())
//        )
//        .filter(ifSend -> ifSend)
//        .switchIfEmpty(Mono.error(ErrorDefine.MESSAGE_SEND_FAIL.exception()))
//        .then();
//  }

//  @Override
//  public Mono<Long> sendWorkMessage(String bindingName, String defineId, long workId){
//       return ReactiveKeycloakSecurityContextHolder.getContext()
//        .map(context ->
//            sendMessage(bindingName, defineId, WorkCreateType.COMPLETED, String.valueOf(workId) ,
//                WorkCreateMessage.builder()
//                    .workId(workId)
//                    .build())
//        )
//        .filter(ifSend -> ifSend)
//        .map(ifSend -> workId)
//        .switchIfEmpty(Mono.error(ErrorDefine.MESSAGE_SEND_FAIL.exception()));
//  }


  @Override
  public Mono<Long> sendWorkMessage(String bindingName, String defineId,
                             long workId, Map<String,Object> processData){
    return ReactiveKeycloakSecurityContextHolder.getContext()
        .map(context ->
            sendMessage(bindingName, defineId, WorkCreateType.RUNNING, String.valueOf(workId) ,
                WorkCreateMessage.builder()
                    .workId(workId)
                    .data(processData)
                    .build())
        )
        .filter(ifSend -> ifSend)
        .map(ifSend -> workId)
        .switchIfEmpty(Mono.error(ErrorDefine.MESSAGE_SEND_FAIL.exception()));
  }





  /**
   * 发送消息
   * @param bindingName Spring streamBridge 绑定名称
   * @param define 操作定义ID
   * @param type 消息类型
   * @param workId 数据ID 用与消息事务回查
   * @param message 消息内容
   * @return 是否发送成功
   */

  private boolean sendMessage(String bindingName, String define, WorkCreateType type, String workId, WorkCreateMessage message) {
    log.info("send message {} {} {} {}", bindingName, define, type, workId);
    var msg = MessageBuilder.withPayload(message)
        .setHeader(WorkMessage.MESSAGE_HEADER_WORK_TYPE, type.name())
        .setHeader(WorkMessage.MESSAGE_HEADER_WORK_DEFINE, define)
        .setHeader(WorkMessage.MESSAGE_HEADER_DATA_ID, workId)
        .build();
    return streamBridge.send(bindingName, msg);
  }

//  @Override
//  public Mono<Long> runWork(long workId, long orgId, String defineId, String bindingName) {
//
//    return ReactiveKeycloakSecurityContextHolder.getContext()
//        .map(context ->
//            sendMessage(bindingName, defineId, WorkStatus.RUNNING,String.valueOf(workId),
//                WorkMessage.builder()
//                    .workId(workId)
//                    .empId(context.getUserInfo().getId())
//                    .empName(context.getUserInfo().getName())
//                    .orgId(orgId)
//                    .build())
//        )
//        .flatMap(ifSend -> ifSend ? Mono.just(workId) : Mono.error(ErrorDefine.MESSAGE_SEND_FAIL.exception()));
//  }
}
