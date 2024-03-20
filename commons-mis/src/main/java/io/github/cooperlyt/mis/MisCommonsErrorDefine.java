package io.github.cooperlyt.mis;

import io.github.cooperlyt.commons.cloud.exception.ExceptionStatusCode;
import org.springframework.http.HttpStatus;

public enum MisCommonsErrorDefine implements ExceptionStatusCode {

  PEOPLE_CARD_NOT_FOUND(HttpStatus.NOT_FOUND,1),

  DISTRICT_NOT_FOUND(HttpStatus.NOT_FOUND,1),

  DICTIONARY_NOT_FOUND(HttpStatus.NOT_FOUND,3);

  private static final int CODE_ROOT = 8010200;
  private final HttpStatus status;
  private final int code;

  @Override
  public HttpStatus getHttpStatus() {
    return status;
  }

  MisCommonsErrorDefine(HttpStatus status, int code) {
    this.status = status;
    this.code = CODE_ROOT + code;
  }

  @Override
  public int getCode() {
    return code;
  }

  @Override
  public String getMessage() {
    return this.name().toLowerCase();
  }
}
