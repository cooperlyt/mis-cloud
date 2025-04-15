package io.github.cooperlyt.mis.work.message;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class WorkEventMessage {

  public static final String MESSAGE_HEADER_EVENT_MESSAGE = "message";

  public WorkEventMessage(String name, String businessKey, Map<String, Object> args) {
    this.name = name;
    this.args = args;
    this.businessKey = businessKey;
  }

  private Map<String,Object> args;

  private String businessKey;

  private String name;

}
