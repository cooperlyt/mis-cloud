package io.github.cooperlyt.mis.work.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class WorkDefineForCreate extends WorkDefine{

  public WorkDefineForCreate(WorkDefine workDefine) {
    super(workDefine.getDefineId(),workDefine.getWorkName(), workDefine.getType(), workDefine.isProcess(),workDefine.isEnabled());
  }

  public WorkDefineForCreate(long workId,WorkDefine workDefine) {
    super(workDefine.getDefineId(),workDefine.getWorkName(),workDefine.getType(),workDefine.isProcess(),workDefine.isEnabled());
    this.workId = workId;
  }

  private long workId;

  public WorkDefineForCreate setPrepareWorkId(long workId){
    this.workId = workId;
    return this;
  }
}
