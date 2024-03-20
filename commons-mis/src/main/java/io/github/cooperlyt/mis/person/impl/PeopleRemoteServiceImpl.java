package io.github.cooperlyt.mis.person.impl;

import io.github.cooperlyt.commons.data.PeopleCardInfo;
import io.github.cooperlyt.mis.RemoteResponseService;
import io.github.cooperlyt.mis.person.PeopleRemoteService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static io.github.cooperlyt.mis.MisCommonsErrorDefine.PEOPLE_CARD_NOT_FOUND;

public class PeopleRemoteServiceImpl extends RemoteResponseService implements PeopleRemoteService {

  private final WebClient webClient;

  @Value("${mis.internal.person.serverName}")
  private String serverName;

  public PeopleRemoteServiceImpl(WebClient webClient) {
    this.webClient = webClient;
  }

  @Override
  public Mono<PeopleCardInfo> getPeopleCardInfo(String id, boolean must) {
    return webClient
        .get()
        .uri("http://" + serverName + "/people/card/{id}", id)
        .accept(MediaType.APPLICATION_JSON)
        .exchangeToMono(response -> sourceResponse(PeopleCardInfo.class, response))
        .switchIfEmpty(must ? Mono.error(PEOPLE_CARD_NOT_FOUND.exception()) : Mono.empty());
  }

}
