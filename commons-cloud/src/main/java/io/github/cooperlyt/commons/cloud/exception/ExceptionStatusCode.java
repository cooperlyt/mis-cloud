package io.github.cooperlyt.commons.cloud.exception;

import org.springframework.http.HttpStatus;

public interface ExceptionStatusCode extends DefineStatusCode {

  HttpStatus getHttpStatus();

  default ResponseDefineException exception(){
    return new ResponseDefineException(this);
  }

  default ResponseDefineException exception(String... args){
    return new ResponseDefineException(this,args);
  }

  default ResponseDefineException exception(Throwable cause){
    return new ResponseDefineException(this, cause);
  }

  default ResponseDefineException exception(Throwable cause, String... args){
    return new ResponseDefineException(this, cause, args);
  }

  default ResponseDefineException exception(String reason, String... args){

    return new ResponseDefineException(this, reason, args);
  }

  default ResponseDefineException exception(String reason, Throwable cause){
    return new ResponseDefineException(this, reason, cause);
  }

  default ResponseDefineException exception(String reason, Throwable cause, String... args){
    return new ResponseDefineException(this, reason, cause, args);
  }

}
