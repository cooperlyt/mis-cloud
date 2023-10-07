package io.github.cooperlyt.mis;

import io.github.cooperlyt.commons.cloud.exception.DefineStatusCode;
import io.github.cooperlyt.commons.cloud.exception.HttpStatusExplain;
import io.github.cooperlyt.commons.cloud.exception.ResponseDefineException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

@Slf4j
public abstract class RemoteResponseService {

  protected <T> Mono<T> responseError(ClientResponse response) {
    log.warn("request return error. http code:" + response.statusCode().value());
    return response.bodyToMono(HttpStatusExplain.class)
        .switchIfEmpty(Mono.error(new ResponseDefineException(HttpStatus.BAD_GATEWAY, new DefineStatusCode() {
          @Override
          public int getCode() {
            return 8000001;
          }

          @Override
          public String getMessage() {
            return "REMOTE_RESPONSE_ERROR";
          }
        })))
        .flatMap(explain -> {
              log.warn(explain.toString());
              log.warn("code:" + explain.getCode());
              log.warn("path:" + explain.getPath());
              log.warn("msg:" + explain.getMessage());
              return Mono.error(
                  new ResponseDefineException(HttpStatus.valueOf(response.statusCode().value()), explain)
              );
            });

  }

  protected <T> Mono<T> sourceResponse(Class<? extends T> elementClass, ClientResponse response) {
    if (response.statusCode().isError()) {
      return responseError(response);
    }
    return response.bodyToMono(elementClass);
  }

  protected <T> Mono<T> sourceResponse(ParameterizedTypeReference<T> elementTypeRef, ClientResponse response){
    if (response.statusCode().isError()) {
      return responseError(response);
    }
    return response.bodyToMono(elementTypeRef);
  }
}
