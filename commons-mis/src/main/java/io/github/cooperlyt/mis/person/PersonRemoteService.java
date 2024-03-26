package io.github.cooperlyt.mis.person;

import io.github.cooperlyt.commons.data.PeopleCardInfo;
import reactor.core.publisher.Mono;

public interface PersonRemoteService {

  Mono<PeopleCardInfo> getPeopleCardInfo(String id, boolean must);

  default Mono<PeopleCardInfo> getPeopleCardInfo(String id){
    return getPeopleCardInfo(id, false);
  }
}
