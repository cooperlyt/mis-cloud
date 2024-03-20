package io.github.cooperlyt.mis.person;

import io.github.cooperlyt.commons.data.PeopleCardInfo;
import io.github.cooperlyt.mis.RemoteResponseService;
import reactor.core.publisher.Mono;

public interface PeopleRemoteService{

  Mono<PeopleCardInfo> getPeopleCardInfo(String id, boolean must);

  default Mono<PeopleCardInfo> getPeopleCardInfo(String id){
    return getPeopleCardInfo(id, false);
  }
}
