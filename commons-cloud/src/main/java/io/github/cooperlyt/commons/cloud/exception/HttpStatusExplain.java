package io.github.cooperlyt.commons.cloud.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Date;


@Getter
public class HttpStatusExplain implements DefineStatusCode{

  private final Date timestamp;

  private final String path;

  private int code;

  private String message;

  private String[] args;

  @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
  public HttpStatusExplain(@JsonProperty("timestamp") Date timestamp,
                           @JsonProperty("path") String path,
                           @JsonProperty("code") int code,
                           @JsonProperty("message") String msg) {
    this.timestamp = timestamp;
    this.path = path;
    this.code = code;
    this.message = msg;
  }

  protected HttpStatusExplain(String path){
    this.path = path;
    this.timestamp = new Date();
  }

  public HttpStatusExplain(DefineStatusCode statusCode, String[] args , String path) {
    this.code = statusCode.getCode();
    this.message = statusCode.getMessage();
    this.timestamp = new Date();
    this.path = path;
    this.args = args;
  }

}
