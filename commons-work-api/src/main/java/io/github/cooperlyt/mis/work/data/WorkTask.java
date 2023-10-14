package io.github.cooperlyt.mis.work.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class WorkTask extends WorkAction {

  private String taskId;

  private String message;

  private String taskName;

  private boolean pass;
}
