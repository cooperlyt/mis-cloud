package io.github.cooperlyt.commons.cloud.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ResponseEntityExceptionHandler {

  protected ResponseEntity<ValidationHttpStatusExplain> validationExceptionHandler(Exception e, String path)  {
    Map<String, String> resultMap = new HashMap<>();
    if (e instanceof MethodArgumentNotValidException) {
      BindingResult bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
      bindingResult.getFieldErrors().forEach(fieldError -> resultMap.put(fieldError.getField(),fieldError.getDefaultMessage()));
    } else if (e instanceof ConstraintViolationException) {
      ((ConstraintViolationException) e).getConstraintViolations().forEach(constraintViolation ->
          resultMap.put(constraintViolation.getPropertyPath().toString(),constraintViolation.getMessageTemplate())
      );
    }
    log.warn("Valid exception:" +  resultMap, e);
    return new ResponseEntity<>(new ValidationHttpStatusExplain(resultMap,path) , HttpStatus.BAD_REQUEST);
  }


  protected ResponseEntity<HttpStatusExplain> defineExceptionHandler(ResponseDefineException ex, String path) {
    log.warn("define exception:" + ex.getDefineStatusCode(), ex);
    return new ResponseEntity<>(new HttpStatusExplain(ex.getDefineStatusCode(),ex.getArgs(),path),ex.getStatusCode());
  }
}
