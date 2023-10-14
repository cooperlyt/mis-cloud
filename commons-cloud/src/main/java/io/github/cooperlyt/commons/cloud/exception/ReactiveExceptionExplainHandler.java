package io.github.cooperlyt.commons.cloud.exception;


import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;

import java.net.BindException;

@Slf4j
@Configuration
@RestControllerAdvice
@ConditionalOnWebApplication
@ConditionalOnClass(org.springframework.web.reactive.config.WebFluxConfigurer.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ReactiveExceptionExplainHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler({ResponseDefineException.class})
  public ResponseEntity<HttpStatusExplain> defineExceptionHandler(ResponseDefineException ex, ServerWebExchange exchange) {
    log.warn("path info:" + exchange.getRequest().getPath().pathWithinApplication().value());
    return super.defineExceptionHandler(ex,exchange.getRequest().getPath().pathWithinApplication().value());
  }

  @ExceptionHandler({BindException.class, ValidationException.class, MethodArgumentNotValidException.class})
  public ResponseEntity<ValidationHttpStatusExplain> MethodArgumentNotValidException(Exception e, ServerWebExchange exchange){
    log.warn("path info:" + exchange.getRequest().getPath().pathWithinApplication().value());
    return super.validationExceptionHandler(e,exchange.getRequest().getPath().pathWithinApplication().value());
  }


}
