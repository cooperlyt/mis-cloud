package io.github.cooperlyt.commons.cloud.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
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

import java.net.BindException;

@Configuration
@ControllerAdvice
@ConditionalOnClass(jakarta.servlet.http.HttpServletRequest.class)
@ConditionalOnMissingBean(annotation = ControllerAdvice.class)
public class ExceptionExplainHandler extends ResponseEntityExceptionHandler{


  @ExceptionHandler({BindException.class, ValidationException.class, MethodArgumentNotValidException.class})
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public HttpStatusExplain validationExceptionHandler(Exception ex, HttpServletRequest request)  {
    return super.validationExceptionHandler(ex,request.getContextPath());
  }

  @ExceptionHandler( { ResponseDefineException.class})
  public ResponseEntity<HttpStatusExplain> defineExceptionHandler(ResponseDefineException ex, HttpServletRequest request){
    return super.defineExceptionHandler(ex,request.getContextPath());
  }

}
