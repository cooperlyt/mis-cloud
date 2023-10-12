package io.github.cooperlyt.commons.cloud.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class ValidationHttpStatusExplain extends HttpStatusExplain{

  private final Map<String,String> fields;
  public ValidationHttpStatusExplain(Map<String,String> fields, String path) {
    super(path);
    this.fields = fields;
  }


  @Override
  public int getCode() {
    return 100001;
  }

  @Override
  public String getMessage() {
    return "validation_fail";
  }
}
