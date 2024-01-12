package io.github.cooperlyt.mis.work.data;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class WorkTask extends WorkAction {

  @Builder(builderMethodName = "taskBuilder", builderClassName = "TaskBuilder")
  public WorkTask(WorkAction action, String taskId, String message, String taskName, boolean pass) {
    super(action);
    this.taskId = taskId;
    this.message = message;
    this.taskName = taskName;
    this.pass = pass;
  }

  private String taskId;

  private String message;

  private String taskName;

  private boolean pass;
}
