package io.github.cooperlyt.mis.service.work.model;

import io.github.cooperlyt.mis.work.data.WorkDefine;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

@EqualsAndHashCode(callSuper = true)
@Table("work_define")
@Data
public class WorkDefineModel extends WorkDefine {

  @Id
  @Override
  public String getDefineId() {
    return super.getDefineId();
  }

  @Version
  private Long version;
}
