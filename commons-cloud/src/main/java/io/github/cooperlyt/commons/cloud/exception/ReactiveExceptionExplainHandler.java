package io.github.cooperlyt.commons.cloud.exception;


import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ServerWebExchange;

import java.net.BindException;

@Slf4j
@Configuration
@ControllerAdvice
@ConditionalOnClass(org.springframework.web.reactive.config.WebFluxConfigurer.class)
@ConditionalOnMissingBean(annotation = ControllerAdvice.class)
public class ReactiveExceptionExplainHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler({ResponseDefineException.class})
  public ResponseEntity<HttpStatusExplain> defineExceptionHandler(ResponseDefineException ex, ServerWebExchange exchange) {
    log.debug("path info:" + exchange.getRequest().getPath().pathWithinApplication().value());
    return super.defineExceptionHandler(ex,exchange.getRequest().getPath().contextPath().value());
  }

  @ExceptionHandler({BindException.class, ValidationException.class, MethodArgumentNotValidException.class})
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ValidationHttpStatusExplain MethodArgumentNotValidException(Exception e, ServerWebExchange exchange){
    log.debug("path info:" + exchange.getRequest().getPath().pathWithinApplication().value());
    return super.validationExceptionHandler(e,exchange.getRequest().getPath().contextPath().value());
  }


}
