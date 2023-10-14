package io.github.cooperlyt.mis.work.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkActionDetails extends WorkAction {





  private String message;

  private String taskName;

  private boolean pass;
}
