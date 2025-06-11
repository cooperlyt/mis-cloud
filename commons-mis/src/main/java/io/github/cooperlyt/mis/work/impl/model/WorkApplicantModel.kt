package io.github.cooperlyt.mis.work.impl.model;

import io.github.cooperlyt.commons.data.PowerBody;
import io.github.cooperlyt.commons.data.PowerBodyImpl;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@EqualsAndHashCode(callSuper = true)
@Table("work_applicant")
@NoArgsConstructor
public class WorkApplicantModel extends PowerBodyImpl implements Persistable<Long> {


  @Transient
  private boolean _new = false;

  public WorkApplicantModel(boolean _new, PowerBody powerBody, long workId) {
    super(powerBody);
    this.workId = workId;
    this._new = _new;
  }


  @Getter
  @Setter
  @Id
  private Long workId;


  @Override
  public Long getId() {
    return workId;
  }

  @Override
  public boolean isNew() {
    return _new;
  }
}
