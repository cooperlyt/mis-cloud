package io.github.cooperlyt.mis.work.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkDefine implements java.io.Serializable {

  private String defineId;
  private String workName;
  private String type;
  private boolean process;
  private boolean enabled;
}
