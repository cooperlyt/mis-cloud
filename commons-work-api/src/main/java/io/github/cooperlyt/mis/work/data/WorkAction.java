package io.github.cooperlyt.mis.work.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class WorkAction extends WorkOperator {

  private String taskId;

  private String message;

  private String taskName;

  private boolean pass;
}
