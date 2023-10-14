package io.github.cooperlyt.mis.work;

import io.github.cooperlyt.commons.cloud.exception.ExceptionStatusCode;
import org.springframework.http.HttpStatus;

public final class Constant {

  public static final String WORK_ID_PARAM = "workID";


  public enum ErrorDefine implements ExceptionStatusCode {

    WORK_DEFINE_NOT_FOUND(HttpStatus.NOT_FOUND,5),

    WORK_IS_DISABLED(HttpStatus.FORBIDDEN,4),

    WORK_NOT_EXISTS(HttpStatus.NOT_FOUND,1),

    DICTIONARY_FIELD_REQUIRED(HttpStatus.BAD_REQUEST,6),

    DICTIONARY_VALUE_INVALID(HttpStatus.BAD_REQUEST,7),

    BUSINESS_IS_DISABLED(HttpStatus.FORBIDDEN,2),
    MESSAGE_SEND_FAIL(HttpStatus.FORBIDDEN,1),

    CORP_INFO_NOT_FOUND(HttpStatus.FORBIDDEN,3);




    private static final int CODE_ROOT = 8000100;
    private final HttpStatus status;
    private final int code;

    @Override
    public HttpStatus getHttpStatus() {
      return status;
    }

    ErrorDefine(HttpStatus status, int code) {
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
}
